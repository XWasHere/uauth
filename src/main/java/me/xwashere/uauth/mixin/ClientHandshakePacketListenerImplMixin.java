package me.xwashere.uauth.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import me.xwashere.uauth.auth.uauth_authentication_service;
import me.xwashere.uauth.gui.untrusted_ss_screen;
import me.xwashere.uauth.io.*;
import me.xwashere.uauth.uauth;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.BanNoticeScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class ClientHandshakePacketListenerImplMixin {
    @Shadow public abstract Connection getConnection();

    @Shadow @Final private Connection connection;

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private Consumer<Component> updateStatus;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandshakePacketListenerImplMixin.class);

    public handshake_submode __mode = handshake_submode.INVALID;

    public ClientboundHelloPacket __t_hello = null;

    public Component __t_status = null;

    @Inject(
            method = "handleHello",
            at     = @At("HEAD")
    )
    void handleHello(ClientboundHelloPacket p_104549_, CallbackInfo ci) {
        LOGGER.info("SERVER: HELLO");
    }

    @Inject(
            method      = "handleCustomQuery",
            at          = @At("HEAD"),
            cancellable = true
    )
    void a(ClientboundCustomQueryPacket p_10045_, CallbackInfo ci) {
        if (p_10045_.getName().equals(new ResourceLocation("uauth", "ua_handshake"))) {
            ci.cancel();

            byte opcode = p_10045_.getData().getByte(0);
            switch (opcode) {
                case 0: {
                    LOGGER.info("SERVER: UA_BEGIN");

                    minecraft.execute(()->{
                        this.updateStatus.accept(Component.literal("Reconfiguring"));
                    });
                    uauth_authentication_service.fill_from_config();

                    ua_begin_c2s p = new ua_begin_c2s();
                    connection.send(new ServerboundCustomQueryPacket(0, p.encode()));

                    break;
                }
                case 1: {
                    LOGGER.info("SERVER: UA_CONFIGURE");

                    ua_configure_s2c p = ua_configure_s2c.decode(p_10045_.getData());

                    LOGGER.info("  SESSION SERVER URL: " + p.session_server_url.toString());

                    if (p.session_server_url.isPresent()) {
                        minecraft.execute(() -> {
                            if (uauth.c_config.check_session_server(p.session_server_url.get())) {
                                accept_session_server(p.session_server_url.get());
                                this.updateStatus.accept(Component.translatable("connect.negotiating"));
                            } else {
                                Screen old = minecraft.screen;

                                minecraft.setScreen(new untrusted_ss_screen(minecraft.screen, Component.literal(p.session_server_url.get()), (boolean res) -> {
                                    if (res) {
                                        accept_session_server(p.session_server_url.get());
                                    } else {
                                        connection.disconnect(Component.literal("Rejected session server"));
                                    }

                                    minecraft.execute(() -> {
                                        minecraft.setScreen(old);
                                        this.updateStatus.accept(Component.translatable("connect.negotiating"));
                                    });
                                }));
                            }
                        });
                    } else connection.send(new ServerboundCustomQueryPacket(0, new ua_continue_c2s().encode()));

                    break;
                }
            }
        }
    }

    void accept_session_server(String url) {
        LOGGER.info("session server url set to \"" + url + "\"");
        uauth_authentication_service.fill_from_string(url);
        connection.send(new ServerboundCustomQueryPacket(0, new ua_continue_c2s().encode()));
    }
}
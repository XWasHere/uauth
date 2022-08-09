package me.xwashere.uauth.mixin;

import com.mojang.logging.LogUtils;
import me.xwashere.uauth.io.*;
import me.xwashere.uauth.uauth;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class ClientHandshakePacketListenerImplMixin {
    @Shadow public abstract Connection getConnection();

    @Shadow @Final private Connection connection;

    private static final Logger LOGGER = LogUtils.getLogger();

    public handshake_submode __mode = handshake_submode.INVALID;

    public ClientboundHelloPacket __t_hello = null;

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

                    me.xwashere.uauth.auth.uauth_authentication_service.fill_from_config();

                    ua_begin_c2s p = new ua_begin_c2s();
                    connection.send(new ServerboundCustomQueryPacket(0, p.encode()));

                    break;
                }
                case 1: {
                    LOGGER.info("SERVER: UA_CONFIGURE");

                    ua_configure_s2c p = ua_configure_s2c.decode(p_10045_.getData());

                    LOGGER.info("  SESSION SERVER URL: " + p.session_server_url.toString());

                    if (p.session_server_url.isPresent()) {
                        LOGGER.info("session server url set to \"" + p.session_server_url.get() + "\"");
                        me.xwashere.uauth.auth.uauth_authentication_service.fill_from_string(p.session_server_url.get());
                    }

                    ua_continue_c2s res = new ua_continue_c2s();
                    connection.send(new ServerboundCustomQueryPacket(0, res.encode()));

                    break;
                }
            }
        }
    }
}
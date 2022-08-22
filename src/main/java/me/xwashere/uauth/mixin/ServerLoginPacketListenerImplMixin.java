package me.xwashere.uauth.mixin;

import com.mojang.logging.LogUtils;
import me.xwashere.uauth.config.server_config;
import me.xwashere.uauth.io.handshake_submode;
import me.xwashere.uauth.io.ua_begin_s2c;
import me.xwashere.uauth.io.ua_configure_s2c;
import me.xwashere.uauth.uauth;
import me.xwashere.uauth.config.client_config;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin {
    @Shadow public abstract Connection getConnection();

    @Shadow @Final public Connection connection;

    @Shadow public abstract void handleHello(ServerboundHelloPacket p_10047_);

    private static final Logger LOGGER = LogUtils.getLogger();

    public handshake_submode __mode = handshake_submode.INVALID;

    public ServerboundHelloPacket __t_hello = null;

    @Inject(
            method = "handleHello",
            at     = @At("HEAD"),
            cancellable = true
    )
    void handle_hello(ServerboundHelloPacket p_10047_, CallbackInfo ci) {
        System.out.println("HELLO");
        switch (__mode) {
            case INVALID: {
                System.out.println("ENTER SUBMODE BEGIN");
                __mode = handshake_submode.BEGIN;
                __t_hello = p_10047_;

                ci.cancel();

                ua_begin_s2c p = new ua_begin_s2c();
                connection.send(new ClientboundCustomQueryPacket(0, new ResourceLocation(uauth.MODID, "ua_handshake"), p.encode()));

                return;
            }
            case CONTINUE: {
                System.out.println("ENTER SUBMODE INVALID");
                __mode = handshake_submode.INVALID;
                LOGGER.info("CLIENT: HELLO");
                return;
            }
        }
    }

    @Inject(
            method      = "handleKey",
            at          = @At("HEAD")
    )
    void handle_key(ServerboundKeyPacket p_10049_, CallbackInfo ci) {
        LOGGER.info("CLIENT: KEY");
    }

    @Inject(
            method      = "handleCustomQueryPacket",
            at          = @At("HEAD"),
            cancellable = true
    )
    void handle_custom_query(ServerboundCustomQueryPacket p_10045_, CallbackInfo ci) {
        if (__mode == handshake_submode.BEGIN) {
            ci.cancel();
            if (p_10045_.getData() == null) {
                __mode = handshake_submode.CONTINUE;
                return;
            }

            switch (p_10045_.getData().getByte(0)) {
                case 0: {
                    System.out.println("CLIENT: UA_BEGIN");

                    ua_configure_s2c p = new ua_configure_s2c();
                    if (uauth.s_config.session_server_url != "") p.session_server_url = Optional.of(uauth.s_config.session_server_url);
                    connection.send(new ClientboundCustomQueryPacket(0, new ResourceLocation("uauth", "ua_handshake"), p.encode()));

                    break;
                }
                case 1: {
                    System.out.println("CLIENT: UA_CONTINUE");

                    __mode = handshake_submode.CONTINUE;

                    handleHello(__t_hello);

                    break;
                }
            }
        }
    }
}
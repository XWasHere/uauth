package me.xwashere.uauth.mixin;

import me.xwashere.uauth.config;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
abstract public class ClientPacketListenerMixin {
    @Redirect(
            method = "Lnet/minecraft/client/multiplayer/ClientPacketListener;handleServerData(Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;)V",
            at = @At(
                    value   = "INVOKE",
                    target  = "Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;enforcesSecureChat()Z",
                    ordinal = 1
            )
    )
    public boolean disable_chat_toast(ClientboundServerDataPacket instance) {
        if (config.hide_unsigned_toast.get()) {
            return true;
        } else {
            return instance.enforcesSecureChat();
        }
    }
}

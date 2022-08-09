package me.xwashere.uauth.io;

import io.netty.buffer.ByteBufAllocator;
import net.minecraft.network.FriendlyByteBuf;

public class ua_continue_c2s {
    public FriendlyByteBuf encode() {
        FriendlyByteBuf buf = new FriendlyByteBuf(ByteBufAllocator.DEFAULT.buffer(128));

        buf.writeByte(1);

        return buf;
    }

    public static ua_continue_c2s decode(FriendlyByteBuf buf) {
        if (buf.getByte(1) != 0) return null;

        return new ua_continue_c2s();
    }
}

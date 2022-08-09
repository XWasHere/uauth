package me.xwashere.uauth.io;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.EmptyByteBuf;
import net.minecraft.network.FriendlyByteBuf;

public class ua_begin_s2c {
    public FriendlyByteBuf encode() {
        FriendlyByteBuf buf = new FriendlyByteBuf(ByteBufAllocator.DEFAULT.buffer(128));

        buf.writeByte(0);

        return buf;
    }

    public static ua_begin_s2c decode(FriendlyByteBuf buf) {
        if (buf.getByte(0) != 0) return null;

        return new ua_begin_s2c();
    }
}

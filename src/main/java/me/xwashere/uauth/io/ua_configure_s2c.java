package me.xwashere.uauth.io;

import io.netty.buffer.ByteBufAllocator;
import net.minecraft.network.FriendlyByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class ua_configure_s2c {
    public Optional<String> session_server_url = Optional.empty();

    public FriendlyByteBuf encode() {
        FriendlyByteBuf buf = new FriendlyByteBuf(ByteBufAllocator.DEFAULT.buffer(128));

        buf.writeByte(1);
        buf.writeOptional(session_server_url, (FriendlyByteBuf b, String s) -> {
            b.writeInt(s.length());
            b.writeCharSequence(s, StandardCharsets.UTF_8);
        });

        return buf;
    }

    public static ua_configure_s2c decode(FriendlyByteBuf buf) {
        if (buf.readByte() != 1) return null;

        ua_configure_s2c p = new ua_configure_s2c();

        p.session_server_url = buf.readOptional((b) -> b.readCharSequence(b.readInt(), StandardCharsets.UTF_8).toString());

        return p;
    }
}

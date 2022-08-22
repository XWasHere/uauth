package me.xwashere.uauth.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public class untrusted_ss_screen extends ConfirmScreen {
    public untrusted_ss_screen(Screen screen, Component url, BooleanConsumer cb) {
        super((boolean res) -> { cb.accept(!res); },
                Component.literal("Untrusted session server").withStyle(ChatFormatting.BOLD),
                Component.literal("""
                    This server uses a custom session server [%s] that is not on your whitelist.
                    Would you like to join anyways?""".formatted(url.getString())),
                Component.literal("Disconnect"), Component.literal("Continue")
        );
    }
}

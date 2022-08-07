package me.xwashere.uauth.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(net.minecraft.client.gui.screens.TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    /**
     *  @author XWasHere
     *  @reason bypass ban on title screen
     */
    @Nullable @Overwrite public Component getMultiplayerDisabledReason() {
        return null;
    }
}

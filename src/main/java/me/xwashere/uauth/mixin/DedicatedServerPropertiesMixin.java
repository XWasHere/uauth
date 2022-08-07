package me.xwashere.uauth.mixin;

import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.dedicated.Settings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Properties;

@Mixin(DedicatedServerProperties.class)
abstract public class DedicatedServerPropertiesMixin extends Settings<DedicatedServerProperties>  {
    public DedicatedServerPropertiesMixin(Properties p_139801_) {
        super(p_139801_);
    }

    @Override
    protected boolean get(String nm, boolean def) {
        return super.get(nm, nm == "enforce-secure-profile" ? false : def);
    }
}

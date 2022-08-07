package me.xwashere.uauth.mixin;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import me.xwashere.uauth.auth.uauth_authentication_service;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.MalformedURLException;
import java.net.Proxy;

@Mixin(net.minecraft.client.Minecraft.class)
public class MinecraftMixin {
    @Shadow @Final private Proxy proxy;

    @Redirect(
            method = "<init>",
            at     = @At(
                    value  = "NEW",
                    args   = "class=com/mojang/authlib/yggdrasil/YggdrasilAuthenticationService"
            )
    )
    public YggdrasilAuthenticationService auth_service_new(Proxy proxy) throws MalformedURLException, NoSuchMethodException {
        return new uauth_authentication_service(proxy);
    }
}

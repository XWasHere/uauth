package me.xwashere.uauth.mixin;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import me.xwashere.uauth.auth.uauth_authentication_service;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.MalformedURLException;
import java.net.Proxy;

@Mixin(net.minecraft.server.Main.class)
public class MainMixin {
    @Redirect(
            method = "main",
            at     = @At(
                    value  = "NEW",
                    args   = "class=com/mojang/authlib/yggdrasil/YggdrasilAuthenticationService"
            )
    )
    private static YggdrasilAuthenticationService auth_service_new(Proxy proxy) throws MalformedURLException, NoSuchMethodException {
        return new uauth_authentication_service(proxy);
    }
}

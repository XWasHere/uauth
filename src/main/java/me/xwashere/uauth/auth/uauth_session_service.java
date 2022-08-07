package me.xwashere.uauth.auth;

import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.InsecurePublicKeyException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.util.Map;

public class uauth_session_service extends YggdrasilMinecraftSessionService {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected uauth_session_service(YggdrasilAuthenticationService service, Environment env) {
        super(service, env);
    }
}

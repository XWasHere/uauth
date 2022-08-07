package me.xwashere.uauth.auth;

import com.google.gson.*;
import com.mojang.authlib.*;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.UserBannedException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.ServicesKeyInfo;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import com.mojang.authlib.yggdrasil.response.Response;
import com.mojang.logging.LogUtils;
import com.mojang.util.UUIDTypeAdapter;
import me.xwashere.uauth.config;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.UUID;

public class uauth_authentication_service extends YggdrasilAuthenticationService {
    String base_url;
    URL    join_url;
    URL    has_joined_url;

    private static final Logger LOGGER = LogUtils.getLogger();
    private Gson gson;

    public Method _determineEnvironment;

    public uauth_authentication_service(Proxy proxy) throws NoSuchMethodException, MalformedURLException {
        super(proxy);

        _determineEnvironment = YggdrasilAuthenticationService.class.getDeclaredMethod("determineEnvironment");
        _determineEnvironment.setAccessible(true);

        gson = new GsonBuilder()
            .registerTypeAdapter(GameProfile.class, new game_profile_serializer())
            .registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer())
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .registerTypeAdapter(ProfileSearchResultsResponse.class, new ProfileSearchResultsResponse.Serializer())
            .create();
    }

    public void maybe_init() {
        if (base_url == null) {
            base_url = config.session_server_url.get();
            if (base_url.equals("")) base_url = "http://71.90.213.158:81";

            join_url = HttpAuthenticationService.constantURL(base_url + "/join");
            has_joined_url = HttpAuthenticationService.constantURL(base_url + "/has_joined");
        }
    }

    @Override
    public MinecraftSessionService createMinecraftSessionService() {
//        LOGGER.info("createMinecraftSessionService");
        try {
            return new uauth_session_service(this, (Environment)_determineEnvironment.invoke(this));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected <T extends Response> T makeRequest(URL url, Object input, Class<T> classOfT, @Nullable String authentication) throws AuthenticationException {
        AuthenticationException err = null;

        if (uauth_auth_common.mojank) {
            try {
                return super.makeRequest(url, input, classOfT, authentication);
            } catch (AuthenticationException e) { err = e; }
        }

        String res; T result;

        maybe_init();
        System.out.println(base_url);

        if (url.toString().endsWith("/join")) {
            try {
                if (input == null) throw new AuthenticationException("nu");

                res = performPostRequest(join_url, gson.toJson(input), "application/json");
            } catch (IOException e) { throw new RuntimeException(e); }
        } else if (url.toString().contains("sessionserver.mojang.com/session/minecraft/hasJoined")) {
            try {
                res = performGetRequest(new URL("http://" + has_joined_url.getHost() + ":" + has_joined_url.getPort() + has_joined_url.getPath() + "?" + url.getQuery()), authentication);
            } catch (IOException e) { throw new RuntimeException(e); }
        } else return super.makeRequest(url, input, classOfT, authentication);

        result = gson.fromJson(res, classOfT);

        return result;
    }

    public static class game_profile_serializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {
        @Override
        public JsonElement serialize(GameProfile p, Type type, JsonSerializationContext ctx) {
            JsonObject o = new JsonObject();

            UUID   id;   if ((id = p.getId()) != null)     o.add("id", ctx.serialize(id));
            String name; if ((name = p.getName()) != null) o.addProperty("name", name);

            return o;
        }

        @Override
        public GameProfile deserialize(JsonElement j, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            JsonObject o = (JsonObject)j;

            UUID   id   = o.has("id")   ? ctx.deserialize(o.get("id"),   UUID.class)   : null;
            String name = o.has("name") ? ctx.deserialize(o.get("name"), String.class) : null;

            return new GameProfile(id, name);
        }
    }
}

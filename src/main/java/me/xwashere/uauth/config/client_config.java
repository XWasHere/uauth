package me.xwashere.uauth.config;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import org.codehaus.plexus.util.ReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class client_config {
    public static Logger __LOGGER = LogUtils.getLogger();
    public static Gson __GSON = new GsonBuilder()
            .registerTypeAdapter(client_config.class, new client_config.serializer())
            .registerTypeAdapter(session_server_identity.class, new session_server_identity.serializer())
            .setPrettyPrinting()
            .create();

    public boolean hide_unsigned_toast = false;
    public String  session_server_url  = "";
    public List<session_server_identity> trusted_session_servers = new ArrayList<>();

    public static client_config read(String filename) {
        try {
            if (new File(filename).exists()) {
                client_config c = __GSON.fromJson(new FileReader(filename), client_config.class);

                __LOGGER.info(c.toString());

                return c;
            } else {
                client_config c = new client_config();

                return c;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String filename) throws IOException {
        String s = __GSON.toJson(this);
        FileWriter fw = new FileWriter(filename);
        fw.write(s);
        fw.close();
    }

    public boolean check_session_server(String server) {
        for (int i = 0; i < trusted_session_servers.size(); i++) {
            session_server_identity id = trusted_session_servers.get(i);
            if (id.type == session_server_identity.id_type.URL && id.validate(server)) return true;
        }

        return false;
    }

    public static class serializer implements JsonSerializer<client_config>, JsonDeserializer<client_config> {
        @Override
        public JsonElement serialize(client_config src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject o = new JsonObject();

            o.addProperty("hide_unsigned_toast",     src.hide_unsigned_toast);
            o.addProperty("session_server_url",      src.session_server_url);

            JsonArray a = new JsonArray();
            for (int i = 0; i < src.trusted_session_servers.size(); i++) a.add(context.serialize(src.trusted_session_servers.get(i)));
            o.add("trusted_session_servers", a);

            return o;
        }

        @Override
        public client_config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            client_config c = new client_config();

            JsonObject o = json.getAsJsonObject();

            if (o.has("hide_unsigned_toast")) c.hide_unsigned_toast = o.get("hide_unsigned_toast").getAsBoolean();
            if (o.has("session_server_url") && !o.get("session_server_url").isJsonNull()) c.session_server_url = o.get("session_server_url").getAsString();
            if (o.has("trusted_session_servers") && o.get("trusted_session_servers").isJsonArray()) {
                List<session_server_identity> l = new ArrayList<>();
                JsonArray    a = o.get("trusted_session_servers").getAsJsonArray();

                for (int i = 0; i < a.size(); i++) l.add(context.deserialize(a.get(i), session_server_identity.class));

                c.trusted_session_servers = l;
            }

            return c;
        }
    }
}
package me.xwashere.uauth.config;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import org.codehaus.plexus.util.ReaderFactory;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Type;

public class server_config {
    public static Logger __LOGGER = LogUtils.getLogger();
    public static Gson __GSON = new GsonBuilder()
        .registerTypeAdapter(server_config.class, new serializer())
        .setPrettyPrinting()
        .create();

    public String session_server_url;

    public static server_config read(String filename) {
        try {
            if (new File(filename).exists()) {
                server_config c = __GSON.fromJson(new FileReader(filename), server_config.class);

                __LOGGER.info(c.toString());

                return c;
            } else {
                server_config c = new server_config();

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

    public static class serializer implements JsonSerializer<server_config>, JsonDeserializer<server_config> {
        @Override
        public JsonElement serialize(server_config src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject o = new JsonObject();

            o.addProperty("session_server_url",  src.session_server_url);

            return o;
        }

        @Override
        public server_config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            server_config c = new server_config();

            JsonObject o = json.getAsJsonObject();

            if (o.has("session_server_url") && !o.get("session_server_url").isJsonNull()) c.session_server_url = o.get("session_server_url").getAsString();

            return c;
        }
    }
}

package me.xwashere.uauth.config;

import com.google.gson.*;

import java.lang.reflect.Type;

public class session_server_identity {
    public id_type type;

    public static enum id_type { URL };

    String url = null;
    public boolean validate(String url) {
        return url.equals(this.url);
    }

    public static class serializer implements JsonSerializer<session_server_identity>, JsonDeserializer<session_server_identity> {
        @Override
        public session_server_identity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            session_server_identity id = new session_server_identity();
            JsonObject o = json.getAsJsonObject();

            if (o.has("id_type") && o.get("id_type").getAsString().equals("url") && o.has("url")) {
                id.type = id_type.URL;
                id.url  = o.get("url").getAsString();
            }

            return id;
        }

        @Override
        public JsonElement serialize(session_server_identity src, Type typeOfSrc, JsonSerializationContext context) {
            if (src.type == id_type.URL) {
                JsonObject o = new JsonObject();

                o.addProperty("id_type", "url");
                o.addProperty("url",     src.url);

                return o;
            } else {
                return new JsonObject();
            }
        }
    }
}
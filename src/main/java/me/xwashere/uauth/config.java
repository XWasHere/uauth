package me.xwashere.uauth;

import net.minecraftforge.common.ForgeConfigSpec;

public class config {
    public static final ForgeConfigSpec               CFG_CLIENT;
        public static ForgeConfigSpec.BooleanValue        hide_unsigned_toast;
        public static ForgeConfigSpec.ConfigValue<String> session_server_url;
    static {
        ForgeConfigSpec.Builder c_builder = new ForgeConfigSpec.Builder();
        cfg_client(c_builder);
        CFG_CLIENT = c_builder.build();
    }

    public static void cfg_client(ForgeConfigSpec.Builder b) {
        b.push("QOL");
            hide_unsigned_toast = b.comment("hide \"insecure server\" toast").define("hide_unsigned_toast", true);
        b.pop();
        b.push("auth");
            session_server_url  = b.comment("session server to use")         .define("session_server_url",  "");
        b.pop();
    }
}

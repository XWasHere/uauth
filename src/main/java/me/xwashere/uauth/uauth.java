package me.xwashere.uauth;

import com.mojang.logging.LogUtils;
import com.sun.security.auth.login.ConfigFile;
import me.xwashere.uauth.config.client_config;
import me.xwashere.uauth.config.server_config;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.targets.FMLServerLaunchHandler;
import net.minecraftforge.fml.mclanguageprovider.MinecraftModContainer;
import net.minecraftforge.fml.server.ServerMain;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.loading.ServerModLoader;
import org.slf4j.Logger;

import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(uauth.MODID)
public class uauth {
    public static final String MODID = "uauth";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static String c_config_path = null;
    public static client_config c_config = null;

    public static String s_config_path = null;
    public static server_config s_config = null;

    public uauth() {
        IEventBus eb = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        eb.addListener(this::on_client_setup);
        eb.addListener(this::on_server_setup);
    }

    public void on_client_setup(FMLClientSetupEvent ev) {
        LOGGER.info("loading client config");
        c_config = client_config.read(c_config_path = FMLPaths.CONFIGDIR.get().toString() + "/ua_client.json");

        try {
            c_config.write(c_config_path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void on_server_setup(FMLDedicatedServerSetupEvent ev) {
        LOGGER.info("loading server config");
        s_config = server_config.read(s_config_path = FMLPaths.CONFIGDIR.get().toString() + "/ua_server.json");

        if (s_config.session_server_url != null) LOGGER.info("session server set to \"" + s_config.session_server_url + "\"");

        try {
            s_config.write(s_config_path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

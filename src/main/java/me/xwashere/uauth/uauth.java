package me.xwashere.uauth;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(uauth.MODID)
public class uauth {
    public static final String MODID = "uauth";

    public static SimpleChannel SERVERIO = null;

    private static final Logger LOGGER = LogUtils.getLogger();

    public uauth() {
        IEventBus eb = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, config.CFG_CLIENT, "ua_client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, config.CFG_SERVER, "ua_server.toml");

        /*eb.addListener((FMLCommonSetupEvent ev) -> {
            int id = 0;

            SERVERIO = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MODID, "ua_handshake"),
                () -> "1",
                (String ver) -> {
                    System.out.println("SERVER: " + ver);
                    return true;
                },
                (String ver) -> {
                    System.out.println("CLIENT: " + ver);
                    return true;
                }
            );

            SERVERIO.registerMessage(id++, ua_begin_s2c.class,
                (p, buf) -> {
                    buf.writeByte(0);
                },
                (buf) -> {
                    return new ua_begin_s2c();
                },
                (p, ctx) -> {
                    LOGGER.info("SERVER: UA_BEGIN_S2C");
                    ctx.get().enqueueWork(()->{
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                            SERVERIO.sendTo(new ua_begin_c2s(), ctx.get().getNetworkManager(), NetworkDirection.LOGIN_TO_SERVER);
                        });
                    });
                    ctx.get().setPacketHandled(true);
                },
                Optional.of(NetworkDirection.LOGIN_TO_CLIENT)
            );

            SERVERIO.registerMessage(id++, ua_begin_c2s.class,
                (p, buf) -> {
                    buf.writeChar('a');
                    buf.writeChar('b');
                    buf.writeChar('c');
                },
                (buf) -> {
                    return new ua_begin_c2s();
                },
                (p, ctx) -> {
                    LOGGER.info("CLIENT: UA_BEGIN_C2S");
                    ctx.get().enqueueWork(()->{
                        // SERVERIO.sendTo(p, ctx.get().getNetworkManager(), NetworkDirection.LOGIN_TO_CLIENT);
                    });
                    ctx.get().setPacketHandled(true);
                },
                Optional.of(NetworkDirection.LOGIN_TO_SERVER)
            );

            SERVERIO.registerMessage(id++, ua_trust_s2c.class,
                (p, buf) -> {
                    buf.writeByte(1);
                },
                (buf) -> {
                    return null;
                },
                (p, ctx) -> {
                    LOGGER.info("SERVER: UA_TRUST_S2C: SS_URL: " + p);
                    ctx.get().setPacketHandled(true);
                }
            );
        });*/
    }
}

package dev.domkss;

import dev.domkss.blocks.ModBlocks;
import dev.domkss.config.ModConfig;
import dev.domkss.items.ModItems;
import dev.domkss.jconfiglib.ConfigLoader;
import dev.domkss.networking.ServerNetworkHandler;
import net.fabricmc.api.ModInitializer;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnderGround implements ModInitializer {
    public static final String MOD_ID = "underground";

    public static Logger LOGGER;

    public static ModConfig config;

    @Override
    public void onInitialize() {
        initLogger();

        String CONFIG_FILE_PATH = "config/Underground.yaml";
        ConfigLoader configLoader = new ConfigLoader(CONFIG_FILE_PATH, LOGGER);
        try {
            config = configLoader.loadConfig(ModConfig.class);
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }

        ModBlocks.registerAll();
        ModItems.registerAll();
        ServerNetworkHandler.register();

        LOGGER.info("Successfully loaded!");
    }

    private void initLogger() {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$s] [UnderGround] %5$s %n");
        LOGGER = Logger.getLogger("UnderGround");
        LOGGER.setLevel(Level.INFO);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(LOGGER.getLevel());
        LOGGER.addHandler(handler);
        LOGGER.setUseParentHandlers(false);
    }


}
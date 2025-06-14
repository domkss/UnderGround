package dev.domkss;

import dev.domkss.blocks.ModBlocks;
import dev.domkss.config.ModConfig;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnderGround implements ModInitializer {
	public static final String MOD_ID = "underground";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ModConfig config;

	@Override
	public void onInitialize() {
		config=new ModConfig();
		ModBlocks.registerAll();
		LOGGER.info("[UndergroundMod] Initialized!");
	}






}
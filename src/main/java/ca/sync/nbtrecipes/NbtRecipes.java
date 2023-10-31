package ca.sync.nbtrecipes;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NbtRecipes implements ModInitializer {
	public static final String MOD_ID = "nbt-recipes";
	public static final String MOD_NAME = "Nbt Recipes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info(MOD_NAME + " is initializing");
		LOGGER.info(MOD_NAME + " has initialized");
	}
}
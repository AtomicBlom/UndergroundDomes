package net.binaryvibrance.undergrounddomes.helpers;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.binaryvibrance.undergrounddomes.Constants;
import net.binaryvibrance.undergrounddomes.DeveloperOptions;
import cpw.mods.fml.common.FMLLog;

public class LogHelper {
	private static final Logger LOGGER = Logger.getLogger(Constants.Mod.MOD_ID);

	public static void init() {

		LOGGER.setParent(FMLLog.getLogger());
		if (DeveloperOptions.DETAILED_LOGGING) {
			LOGGER.setLevel(Level.ALL);
		}
	}

	public static Logger getLogger() {
		return LOGGER;
	}
}

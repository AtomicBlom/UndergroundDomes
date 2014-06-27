package net.binaryvibrance.undergrounddomes.helpers;

import net.binaryvibrance.undergrounddomes.Constants;
import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class LogHelper
{
	public static void log(Level logLevel, Object object)
	{

		FMLLog.log(Constants.Mod.MOD_ID, logLevel, String.valueOf(object));
	}

	public static void all(Object object)
	{
		log(Level.ALL, object);
	}

	public static void debug(Object object)
	{
		log(Level.DEBUG, object);
	}

	public static void error(Object object)
	{
		log(Level.ERROR, object);
	}

	public static void fatal(Object object)
	{
		log(Level.FATAL, object);
	}

	public static void info(Object object)
	{
		log(Level.INFO, object);
	}

	public static void off(Object object)
	{
		log(Level.OFF, object);
	}

	public static void trace(Object object)
	{
		log(Level.TRACE, object);
	}

	public static void warn(Object object)
	{
		log(Level.WARN, object);
	}
}

/*public class LogHelper {
	private static final Logger LOGGER = Logger.getLogger();

	public static void init() {

		//LOGGER.setParent(FMLLog.getLogger());
		if (DeveloperOptions.DETAILED_LOGGING) {
			LOGGER.setLevel(Level.ALL);
		}
	}

	public static Logger getLogger() {
		return LOGGER;
	}
}*/

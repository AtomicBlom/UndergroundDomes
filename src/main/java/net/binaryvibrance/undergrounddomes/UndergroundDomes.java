package net.binaryvibrance.undergrounddomes;

import net.binaryvibrance.undergrounddomes.commands.DomeHeightCommand;
import net.binaryvibrance.undergrounddomes.configuration.ConfigurationHandler;
import net.binaryvibrance.undergrounddomes.generation.WorldGenerator;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Constants.Mod.MOD_ID, name = Constants.Mod.MOD_NAME, version = Constants.Mod.MOD_VERSION, guiFactory=Constants.Mod.GUI_FACTORY_CLASS)
public class UndergroundDomes {
	@Mod.Instance(Constants.Mod.MOD_ID)
	public static UndergroundDomes instance;

	IWorldGenerator worldGenerator = new WorldGenerator();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LogHelper.setLog(event.getModLog());
		ConfigurationHandler configurationHandler = ConfigurationHandler.createInstance(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(configurationHandler);
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(worldGenerator);
		GameRegistry.registerWorldGenerator(worldGenerator, 0);
    }

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
	    event.registerServerCommand(new DomeHeightCommand());

    }

}

package net.binaryvibrance.undergrounddomes;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.binaryvibrance.undergrounddomes.commands.DomeHeightCommand;
import net.binaryvibrance.undergrounddomes.generation.WorldGenerator;
import net.binaryvibrance.undergrounddomes.proxy.CommonProxy;

@Mod(modid = Constants.Mod.MOD_ID, name = Constants.Mod.MOD_NAME, version = Constants.Mod.MOD_VERSION)
public class UndergroundDomes {
	@Mod.Instance(Constants.Mod.MOD_ID)
	public static UndergroundDomes instance;

	@SidedProxy(clientSide = Constants.Mod.CLIENT_SIDE_PROXY, serverSide = Constants.Mod.SERVER_SIDE_PROXY)
	public static CommonProxy proxy;

	//IWorldGenerator worldGenerator = new DomeGenerator();
	IWorldGenerator worldGenerator = new WorldGenerator();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(worldGenerator);
		GameRegistry.registerWorldGenerator(worldGenerator, 0);
		Constants.Blocks.LIGHT_RECEPTOR.selfRegister();
		proxy.registerTileEntities();
		proxy.initRenderingAndTextures();
    }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
	    event.registerServerCommand(new DomeHeightCommand());

    }

}

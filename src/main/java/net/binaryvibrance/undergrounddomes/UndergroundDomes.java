package net.binaryvibrance.undergrounddomes;

import net.binaryvibrance.undergrounddomes.generation.DomeGenerator;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.binaryvibrance.undergrounddomes.proxy.CommonProxy;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Constants.Mod.MOD_ID, name = Constants.Mod.MOD_NAME, version = Constants.Mod.MOD_VERSION)
public class UndergroundDomes {
	@Mod.Instance(Constants.Mod.MOD_ID)
	public static UndergroundDomes instance;

	@SidedProxy(clientSide = Constants.Mod.CLIENT_SIDE_PROXY, serverSide = Constants.Mod.SERVER_SIDE_PROXY)
	public static CommonProxy proxy;

	IWorldGenerator worldGenerator = new DomeGenerator();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LogHelper.init();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		GameRegistry.registerWorldGenerator(worldGenerator, 0);
		Constants.Blocks.LIGHT_RECEPTOR.selfRegister();
		proxy.registerTileEntities();
		proxy.initRenderingAndTextures();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}

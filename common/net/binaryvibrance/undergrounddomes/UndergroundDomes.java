package net.binaryvibrance.undergrounddomes;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.binaryvibrance.undergrounddomes.generation.DomeGenerator;
import net.binaryvibrance.undergrounddomes.proxy.CommonProxy;
import net.minecraft.block.Block;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=Constants.Mod.MOD_ID, name=Constants.Mod.MOD_NAME, version=Constants.Mod.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class UndergroundDomes {
	private static Logger log = FMLLog.getLogger();
	
	@cpw.mods.fml.common.Mod.Instance(Constants.Mod.MOD_ID)
	public static UndergroundDomes Instance;
	
	@SidedProxy(clientSide = Constants.Mod.CLIENT_SIDE_PROXY, serverSide=Constants.Mod.SERVER_SIDE_PROXY)
	public static CommonProxy proxy;
	
	public static Block lightReceptor;
	
	IWorldGenerator worldGenerator = new DomeGenerator(); 
	
	@PreInit
    public void preInit(FMLPreInitializationEvent event) {
		log.setLevel(Level.ALL);
    }
    
    @Init
    public void load(FMLInitializationEvent event) {
    	GameRegistry.registerWorldGenerator(worldGenerator);

    	Constants.Blocks.LightReceptor.selfRegister();
    	
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event) {

    }
	
}

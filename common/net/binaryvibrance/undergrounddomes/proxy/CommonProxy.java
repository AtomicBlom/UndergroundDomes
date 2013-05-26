package net.binaryvibrance.undergrounddomes.proxy;

import net.binaryvibrance.undergrounddomes.Constants;
import net.binaryvibrance.undergrounddomes.tileentitity.TileLightReceptor;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy {
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileLightReceptor.class, Constants.Blocks.LightReceptor.getModLocalizedName());
	}

	public void initRenderingAndTextures() {
		// TODO Auto-generated method stub
		
	}
}

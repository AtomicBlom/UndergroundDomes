package net.binaryvibrance.undergrounddomes.proxy;

import net.binaryvibrance.undergrounddomes.RenderIds;
import net.binaryvibrance.undergrounddomes.block.BlockLightReceptor;
import net.binaryvibrance.undergrounddomes.tileentitity.TileLightReceptor;
import net.binaryvibrance.undergrounddomes.tileentitity.renderer.LightReceptorRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void initRenderingAndTextures() {
		RenderIds.LightReceptorRenderId = RenderingRegistry.getNextAvailableRenderId();
		MinecraftForgeClient.registerItemRenderer(BlockLightReceptor.BlockId, new LightReceptorRenderer());
	}
	
	@Override
	public void registerTileEntities() {
		super.registerTileEntities();
		ClientRegistry.bindTileEntitySpecialRenderer(TileLightReceptor.class, new LightReceptorRenderer());
	}
}

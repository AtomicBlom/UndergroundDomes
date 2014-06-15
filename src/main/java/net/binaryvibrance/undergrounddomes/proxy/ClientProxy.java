package net.binaryvibrance.undergrounddomes.proxy;

import net.binaryvibrance.undergrounddomes.Constants;
import net.binaryvibrance.undergrounddomes.RenderIds;
import net.binaryvibrance.undergrounddomes.block.BlockLightReceptor;
import net.binaryvibrance.undergrounddomes.client.renderer.item.ItemLightReceptorRenderer;
import net.binaryvibrance.undergrounddomes.client.renderer.tileentity.TileEntityLightReceptorRenderer;
import net.binaryvibrance.undergrounddomes.tileentitity.TileLightReceptor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void initRenderingAndTextures() {
		RenderIds.LIGHT_RECEPTOR_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		/*MinecraftForgeClient.registerItemRenderer(BlockLightReceptor.BLOCK_ID, new ItemLightReceptorRenderer());
        MinecraftForgeClient.registerItemRenderer(Constants.Blocks.LIGHT_RECEPTOR, new ItemLightReceptorRenderer());
        RenderingRegistry.registerEntityRenderingHandler(TileLightReceptor.class, new ItemLightReceptorRenderer());*/
	}

	@Override
	public void registerTileEntities() {
		super.registerTileEntities();
		ClientRegistry.bindTileEntitySpecialRenderer(TileLightReceptor.class, new TileEntityLightReceptorRenderer());
	}
}

package net.binaryvibrance.undergrounddomes.client.renderer.tileentity;

import net.binaryvibrance.undergrounddomes.Constants;
import net.binaryvibrance.undergrounddomes.client.model.ModelLightReceptor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class TileEntityLightReceptorRenderer extends TileEntitySpecialRenderer {

	private final ModelLightReceptor model = new ModelLightReceptor();

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glRotatef(180, 0, 0, 1);
		GL11.glTranslatef(-0.5F, -1.5f, 0.5F);
		// GL11.glPushMatrix();

		//
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(Constants.Textures.MODEL_LIGHT_RECEPTOR);
		model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		// GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}

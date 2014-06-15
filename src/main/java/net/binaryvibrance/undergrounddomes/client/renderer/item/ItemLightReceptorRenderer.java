package net.binaryvibrance.undergrounddomes.client.renderer.item;

import net.binaryvibrance.undergrounddomes.Constants;
import net.binaryvibrance.undergrounddomes.client.model.ModelLightReceptor;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class ItemLightReceptorRenderer implements IItemRenderer {

	private final ModelLightReceptor model = new ModelLightReceptor();


	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {

		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

		switch (type) {
		case ENTITY: {
			renderItem(-0.5F, -0.38F, 0.5F, 1.0F);
			return;
		}
		case EQUIPPED: {
			renderItem(0.0F, 0.0F, 1.0F, 1.0F);
			return;
		}
		case INVENTORY: {
			renderItem(-1.0F, -0.9F, 0.0F, 1.0F);
			return;
		}
		default:
			return;
		}
	}

	private void renderItem(float x, float y, float z, float scale) {

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);

		// Scale, Translate, Rotate
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(x + 1f, y + 2, z);
		GL11.glRotatef(180F, 1F, 0, 0);

		// Bind texture
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(Constants.Textures.MODEL_LIGHT_RECEPTOR);		

		// Render
		model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}

package net.binaryvibrance.undergrounddomes.tileentitity.renderer;

import net.binaryvibrance.undergrounddomes.Constants;
import net.binaryvibrance.undergrounddomes.block.ModelLightReceptor;
import net.binaryvibrance.undergrounddomes.tileentitity.TileLightReceptor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class LightReceptorRenderer extends TileEntitySpecialRenderer implements
		IItemRenderer {

	public ModelLightReceptor model;

	public LightReceptorRenderer() {
		model = new ModelLightReceptor();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return true;
	}

	/*public void renderTileEntityAt(TileEntity tileentity, double d, double d1,
			double d2, float f) {
		if (tileentity instanceof TileLightReceptor) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);

			// Scale, Translate, Rotate
			GL11.glScalef(1, 1, 1);
			GL11.glTranslatef((float) d, (float) d1, (float) d2);
			GL11.glRotatef(-90F, 1F, 0, 0);

			FMLClientHandler.instance().getClient().renderEngine
					.func_98187_b(Constants.Textures.MODEL_LIGHT_RECEPTOR);
			model.render(0.0f);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}*/

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		float x, y, z;

		switch (type) {
		case ENTITY:
			x = -0.5f;
			y = 0.0f;
			z = 0.5f;
			break;

		case EQUIPPED: {
			x = 0.0f;
			y = 0.0f;
			z = 1.0f;
			break;
		}
		/*
		 * case EQUIPPED_FIRST_PERSON: { x = 0.0f; y = 0.0f; z = 1.0f; return; }
		 */
		case INVENTORY: {
			x = 0.0f;
			y = -0.1f;
			z = 1.0f;
			break;
		}
		default:
			break;
		}

		x = y = z = 0;
		// TODO Auto-generated method stub
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(Constants.Textures.MODEL_LIGHT_RECEPTOR);
		// bindTextureByName(Constants.Textures.MODEL_LIGHT_RECEPTOR);//
		GL11.glPushMatrix(); // start
		GL11.glTranslatef(x, y, z); // size
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(-90, 0, 1, 0);
		model.render(0);
		GL11.glPopMatrix(); // end
	}
	
	 //This method is called when minecraft renders a tile entity
    public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
        GL11.glPushMatrix();
         //This will move our renderer so that it will be on proper place in the world
         GL11.glTranslatef((float)d, (float)d1, (float)d2);
         TileLightReceptor tileEntityYour = (TileLightReceptor)tileEntity;
         /*Note that true tile entity coordinates (tileEntity.xCoord, etc) do not match to render coordinates (d, etc) that are calculated as [true coordinates] - [player coordinates (camera coordinates)]*/
         renderBlockYour(tileEntityYour, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, Constants.Blocks.LightReceptor);
        GL11.glPopMatrix();
    }
    //And this method actually renders your tile entity
    public void renderBlockYour(TileLightReceptor tl, World world, int i, int j, int k, Block block) {
        Tessellator tessellator = Tessellator.instance;
        //This will make your block brightness dependent from surroundings lighting.
        float f = block.getBlockBrightness(world, i, j, k);
        int l = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int l1 = l % 65536;
        int l2 = l / 65536;
        tessellator.setColorOpaque_F(f, f, f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)l1, (float)l2); 
        
        /*This will rotate your model corresponding to player direction that was when you placed the block. If you want this to work, 
        add these lines to onBlockPlacedBy method in your block class.
        int dir = MathHelper.floor_double((double)((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, dir, 0);*/

        int dir = world.getBlockMetadata(i, j, k);
        
        GL11.glPushMatrix();
         GL11.glTranslatef(0.5F, 0, 0.5F);
         //This line actually rotates the renderer.
         GL11.glRotatef(dir * (-90F), 0F, 1F, 0F);
         GL11.glTranslatef(-0.5F, 0, -0.5F);
         bindTextureByName(Constants.Textures.MODEL_LIGHT_RECEPTOR);
         /*
         Place your rendering code here.
         */
         this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
         
        GL11.glPopMatrix();
    }
}

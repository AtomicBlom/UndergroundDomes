package net.binaryvibrance.undergrounddomes.block;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.net.undergrounddomes.api.IPhotonEmitter;
import net.binaryvibrance.undergrounddomes.Constants;
import net.binaryvibrance.undergrounddomes.RenderIds;
import net.binaryvibrance.undergrounddomes.tileentitity.TileLightReceptor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightReceptor extends BVBlock implements ITileEntityProvider, IPhotonEmitter {

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		// TODO Auto-generated method stub
		iconRegister.registerIcon(Constants.Mod.MOD_ID + ":" + "textures/model/lightreceptor");
	}

	public static final String UNLOCALIZED_NAME = "lightReceptor";
	//public static final String NAME = "Light Receptor";

	public BlockLightReceptor() {
		super(Material.sand);
        setBlockName(UNLOCALIZED_NAME);
		setCreativeTab(CreativeTabs.tabBlock);
		setHardness(10.0f);
		setResistance(1f);
	}

    //@Override
    //public TileEntity createNewTileEntity(World world) {
    //    return createTileEntity(world, 0);
    //}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {

		return new TileLightReceptor();
	}

	@Override
	public boolean renderAsNormalBlock() {

		return false;
	}

	@Override
	public boolean isOpaqueCube() {

		return false;
	}

	// This will tell minecraft not to render any side of our cube.
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	@Override
	public int getRenderType() {
		return RenderIds.LIGHT_RECEPTOR_RENDER_ID;
	}

	public void updateLightLevel(Point3D location) {

	}

	@Override
	public void GetPhotonLevel(Direction inverseDirection, float powerLevel) {

	}
}

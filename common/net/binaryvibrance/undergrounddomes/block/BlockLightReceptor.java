package net.binaryvibrance.undergrounddomes.block;

import net.binaryvibrance.net.undergrounddomes.api.IPhotonEmitter;
import net.binaryvibrance.undergrounddomes.RenderIds;
import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.binaryvibrance.undergrounddomes.tileentitity.TileLightReceptor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightReceptor extends BVBlock implements ITileEntityProvider, IPhotonEmitter {

	public static final String UNLOCALIZED_NAME = "lightReceptor";
	public static final String NAME = "Light Receptor";
	public static final int BLOCK_ID = 2376;
	
	public BlockLightReceptor() {
		super(BLOCK_ID, Material.sand);
		setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(UNLOCALIZED_NAME);
		setHardness(10.0f);
		setResistance(1f);
	}


    @Override
    public TileEntity createTileEntity(World world, int metadata) {

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
    
  //This will tell minecraft not to render any side of our cube.
    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
       return false;
    }

    
    @Override
    public int getRenderType() {
        return RenderIds.LIGHT_RECEPTOR_RENDER_ID;
    }

	@Override
	protected String getBlockName() {
		// TODO Auto-generated method stub
		return NAME;
	}


	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return createTileEntity(world, 0);
	}

	public void updateLightLevel(Point3D location) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void GetPhotonLevel(Direction inverseDirection, float powerLevel) {
		// TODO Auto-generated method stub
		
	}
}

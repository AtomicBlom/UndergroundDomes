package net.binaryvibrance.undergrounddomes.block;

import net.binaryvibrance.undergrounddomes.Constants;
import net.binaryvibrance.undergrounddomes.RenderIds;
import net.binaryvibrance.undergrounddomes.tileentitity.TileLightReceptor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightReceptor extends BVBlock {

	public static final String UnlocalizedName = "lightReceptor";
	public static final String Name = "Light Receptor";
	public static final int BlockId = 2376;
	
	public BlockLightReceptor() {
		super(BlockId, Material.sand);
		setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(UnlocalizedName);
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
        return RenderIds.LightReceptorRenderId;
    }
	
	public String getModLocalizedName() {
		// TODO Auto-generated method stub
		return Constants.Mod.MOD_ID + "." + UnlocalizedName;
	}

	@Override
	protected String getBlockName() {
		// TODO Auto-generated method stub
		return Name;
	}
}

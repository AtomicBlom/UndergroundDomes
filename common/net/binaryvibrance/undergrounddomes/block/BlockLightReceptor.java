package net.binaryvibrance.undergrounddomes.block;

import net.binaryvibrance.undergrounddomes.Constants;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockLightReceptor extends BVBlock {

	public static final String UnlocalizedName = "lightReceptor";
	public static final String Name = "Light Receptor";
	public static final int BlockId = 2376;
	
	public BlockLightReceptor() {
		super(BlockId, Material.sand);
		setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(UnlocalizedName);
	}

	@Override
	protected String getModLocalizedName() {
		// TODO Auto-generated method stub
		return Constants.Mod.MOD_ID + "." + UnlocalizedName;
	}

	@Override
	protected String getBlockName() {
		// TODO Auto-generated method stub
		return Name;
	}
}

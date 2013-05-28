package net.binaryvibrance.undergrounddomes.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockArgonLight extends BVBlock {
	public static final String UnlocalizedName = "argonLight";
	public static final String Name = "Argon Light";
	public static final int BlockId = 2376;
	
	public BlockArgonLight() {
		super(BlockId, Material.glass);
		setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(UnlocalizedName);
		setHardness(10.0f);
		setResistance(1f);
	}

	@Override
	protected String getBlockName() {
		// TODO Auto-generated method stub
		return Name;
	}
}

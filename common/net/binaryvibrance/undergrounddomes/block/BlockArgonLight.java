package net.binaryvibrance.undergrounddomes.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockArgonLight extends BVBlock {
	public static final String UNLOCALIZED_NAME = "argonLight";
	public static final String NAME = "Argon Light";
	public static final int BLOCK_ID = 2376;
	
	public BlockArgonLight() {
		super(BLOCK_ID, Material.glass);
		setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName(UNLOCALIZED_NAME);
		setHardness(10.0f);
		setResistance(1f);
	}

	@Override
	protected String getBlockName() {
		// TODO Auto-generated method stub
		return NAME;
	}
}

package net.binaryvibrance.undergrounddomes.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockArgonLight extends BVBlock {
	public static final String UNLOCALIZED_NAME = "argonLight";

	public BlockArgonLight() {
		super(Material.glass);
		setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName(UNLOCALIZED_NAME);
		setHardness(10.0f);
		setResistance(1f);
	}
}

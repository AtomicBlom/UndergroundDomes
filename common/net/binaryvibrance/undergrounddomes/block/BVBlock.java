package net.binaryvibrance.undergrounddomes.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BVBlock extends Block {

	public BVBlock(int id, Material material) {
		super(id, material);
		// TODO Auto-generated constructor stub
	}
	
	protected abstract String getModLocalizedName();
	protected abstract String getBlockName();
	
	public void selfRegister() {
		GameRegistry.registerBlock(this, getModLocalizedName());
    	LanguageRegistry.addName(this, getBlockName());
	}

}

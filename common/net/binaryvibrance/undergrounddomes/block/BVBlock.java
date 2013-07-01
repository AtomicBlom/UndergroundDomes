package net.binaryvibrance.undergrounddomes.block;

import net.binaryvibrance.undergrounddomes.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class BVBlock extends Block {

	public BVBlock(int id, Material material) {
		super(id, material);
	}

	protected abstract String getBlockName();

	public void selfRegister() {
		GameRegistry.registerBlock(this, getModLocalizedName());
		LanguageRegistry.addName(this, getBlockName());
	}

	public String getModLocalizedName() {
		return Constants.Mod.MOD_ID + "." + getUnlocalizedName2();
	}
}

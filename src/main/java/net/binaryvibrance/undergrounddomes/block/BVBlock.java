package net.binaryvibrance.undergrounddomes.block;

import net.binaryvibrance.undergrounddomes.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class BVBlock extends Block {

	public BVBlock(Material material) {
		super(material);
	}

	public void selfRegister() {
		GameRegistry.registerBlock(this, getModLocalizedName());
	}

	public String getModLocalizedName() {
		return Constants.Mod.MOD_ID + "." + this.getUnlocalizedName().substring(5);
	}
}

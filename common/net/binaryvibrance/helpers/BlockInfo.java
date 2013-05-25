package net.binaryvibrance.helpers;

import net.binaryvibrance.undergrounddomes.Constants;

public final class BlockInfo {
	private int blockId;
	private String blockName;
	private String unlocalizedName;

	public BlockInfo(int blockId, String blockName, String unlocalizedName) {
		this.blockId = blockId;
		this.blockName = blockName;
		this.unlocalizedName = unlocalizedName;	
	}
	
	public int getBlockId() {
		return blockId;
	}
	
	public String getBlockName() {
		return blockName;
	}
	
	public String getUnlocalizedName() {
		return unlocalizedName;
	}
	
	public String getModLocalName() {
		return Constants.Mod.MOD_ID + "." + unlocalizedName;
	}
}

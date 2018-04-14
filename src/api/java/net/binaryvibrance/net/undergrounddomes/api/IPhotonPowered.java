package net.binaryvibrance.net.undergrounddomes.api;

import net.minecraft.util.EnumFacing;

public interface IPhotonPowered {
	public void OnAdjacentPhotonLevelChanged(EnumFacing direction, float powerLevel);

}

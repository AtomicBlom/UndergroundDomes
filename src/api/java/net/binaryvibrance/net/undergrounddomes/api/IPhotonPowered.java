package net.binaryvibrance.net.undergrounddomes.api;

import net.minecraft.util.Direction;

public interface IPhotonPowered {
	public void OnAdjacentPhotonLevelChanged(Direction direction, float powerLevel);

}

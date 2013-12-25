package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.helpers.maths.Vector3;
import net.minecraft.util.Vec3;

public enum CompassDirection {
	NORTH,
	SOUTH,
	EAST,
	WEST;

	public Vec3 ToVec3() {
		switch (this) {
		case NORTH:
			return Vector3.NORTH;
		case SOUTH:
			return Vector3.SOUTH;
		case EAST:
			return Vector3.EAST;
		case WEST:
		default:				
			return Vector3.WEST;
		}
	}
}

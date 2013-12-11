package net.binaryvibrance.helpers.maths;

import net.minecraft.util.Vec3;

public class Vector3 {
	public static final Vec3 NORTH = Vec3.createVectorHelper(0, 0, -1);
	public static final Vec3 SOUTH = Vec3.createVectorHelper(0, 0, 1);
	public static final Vec3 EAST = Vec3.createVectorHelper(1, 0, 0);
	public static final Vec3 WEST = Vec3.createVectorHelper(-1, 0, 0);
	public static final Vec3 UP = Vec3.createVectorHelper(0, 1, 0);
	public static final Vec3 DOWN = Vec3.createVectorHelper(0, -1, 0);

	public static final Vec3[] NEIGHBOURS = new Vec3[] { UP, DOWN, EAST, WEST, SOUTH, NORTH };

	public static Vec3 multiply(Vec3 vector, double multiplier) {
		return Vec3.createVectorHelper(vector.xCoord * multiplier, vector.yCoord * multiplier, vector.zCoord * multiplier);
	}
}

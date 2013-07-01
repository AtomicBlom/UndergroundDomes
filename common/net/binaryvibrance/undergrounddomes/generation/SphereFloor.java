package net.binaryvibrance.undergrounddomes.generation;

import java.util.HashMap;

import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.maths.Vector3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class SphereFloor {
	public final int level;
	private final HashMap<EnumFacing, SphereEntrance> entrances = new HashMap<EnumFacing, SphereEntrance>();

	// TODO: Floor Purpose
	// TODO: Render floor purpose
	public SphereFloor(SphereInstance instance, int level) {
		this.level = level;
		createEntrances(instance);
	}

	private void createEntrances(SphereInstance instance) {
		double radius = instance.getRadius() + 1;
		Point3D entrance;
		entrance = findEntranceLocation(instance.add(Vector3.multiply(Vector3.NORTH, radius)), Vector3.SOUTH);
		entrances.put(EnumFacing.NORTH, new SphereEntrance(entrance));

		entrance = findEntranceLocation(instance.add(Vector3.multiply(Vector3.SOUTH, radius)), Vector3.NORTH);
		entrances.put(EnumFacing.SOUTH, new SphereEntrance(entrance));

		entrance = findEntranceLocation(instance.add(Vector3.multiply(Vector3.EAST, radius)), Vector3.WEST);
		entrances.put(EnumFacing.EAST, new SphereEntrance(entrance));

		entrance = findEntranceLocation(instance.add(Vector3.multiply(Vector3.WEST, radius)), Vector3.EAST);
		entrances.put(EnumFacing.WEST, new SphereEntrance(entrance));
	}

	private Point3D findEntranceLocation(Point3D startPoint, Vec3 searchVector) {
		// TODO: This should ideally figure out how far back the entrance needs
		// to go into the sphere.
		return startPoint;
	}

	public SphereEntrance getEntrance(EnumFacing direction) {
		if (direction == EnumFacing.UP || direction == EnumFacing.DOWN)
			return null;
		return entrances.get(direction);
	}
}

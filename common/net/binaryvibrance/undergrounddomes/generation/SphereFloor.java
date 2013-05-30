package net.binaryvibrance.undergrounddomes.generation;

import java.util.HashMap;

import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.maths.Vector3;
import net.minecraft.util.EnumFacing;

public class SphereFloor {
	public final int level;
	private final HashMap<EnumFacing, SphereEntrance> entrances = new HashMap<EnumFacing, SphereEntrance>();
	
	//TODO: Floor Purpose
	//TODO: Render floor purpose
	public SphereFloor(SphereInstance instance, int level) {
		this.level = level;
		createEntrances(instance);
	}

	private void createEntrances(SphereInstance instance) {
		int radius = (int)instance.getRadius();
		Point3D entrance;
		entrance = findEntranceLocation(instance.add(new Vector3(radius, 0, 0)), new Vector3(-1, 0, 0));
		entrances.put(EnumFacing.NORTH, new SphereEntrance(entrance));		
		
		entrance = findEntranceLocation(instance.add(new Vector3(-radius, 0, 0)), new Vector3(1, 0, 0));
		entrances.put(EnumFacing.SOUTH, new SphereEntrance(entrance));
		
		entrance = findEntranceLocation(instance.add(new Vector3(0, 0, radius)), new Vector3(0, 0, -1));
		entrances.put(EnumFacing.EAST, new SphereEntrance(entrance));
		
		entrance = findEntranceLocation(instance.add(new Vector3(0, 0, -radius)), new Vector3(0, 0, 1));
		entrances.put(EnumFacing.WEST, new SphereEntrance(entrance));
	}
	
	private Point3D findEntranceLocation(Point3D startPoint, Vector3 searchVector) {
		// FIXME: Implement this.
		return startPoint;		
	}

	public SphereEntrance getEntrance(EnumFacing direction) {
		if (direction == EnumFacing.UP || direction == EnumFacing.DOWN) return null;
		return entrances.get(direction);
	}
}

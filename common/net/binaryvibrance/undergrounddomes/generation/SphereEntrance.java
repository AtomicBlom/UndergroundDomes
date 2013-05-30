package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;

public class SphereEntrance {

	public final Point3D location;
	private boolean inUse = false;

	public SphereEntrance(Point3D location) {
		this.location = location;
	}

	public void markInUse() {
		inUse = true;
	}

	public boolean isInUse() {
		return inUse;
	}

}

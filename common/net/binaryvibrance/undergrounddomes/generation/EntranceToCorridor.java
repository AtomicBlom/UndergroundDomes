package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.undergrounddomes.generation.maths.Line;
import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.minecraft.util.Vec3;

public class EntranceToCorridor {
	public final Line lineToCorridor;
	public final Line lineToOrigin;
	public final Point3D entranceLocation;
	public final Point3D destinationLocation;
	private boolean applied = false;
	private SphereEntrance entrance;

	public EntranceToCorridor() {
		Point3D corridorStart = new Point3D(0, 0, 0);
		Point3D originStart = new Point3D(0, 0, 0);
		Point3D join = new Point3D(0, 0, 0);

		lineToCorridor = new Line(corridorStart, join);
		lineToOrigin = new Line(join, originStart);
		entranceLocation = new Point3D(0, 0, 0);
		destinationLocation = originStart;
	}

	public void markApplied() {
		applied = true;
		entrance.corridorPath = this;
	}

	public boolean isApplied() {
		return applied;
	}

	public void setAdjustmentVector(Vec3 vector) {
		// TODO Auto-generated method stub
		entranceLocation.set(lineToCorridor.start);
		entranceLocation.move(vector);
	}

	public void setEntrance(SphereEntrance entrance) {
		// TODO Auto-generated method stub
		this.entrance = entrance;
	}
}
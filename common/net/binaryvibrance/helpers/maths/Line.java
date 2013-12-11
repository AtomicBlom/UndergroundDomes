package net.binaryvibrance.helpers.maths;

import net.minecraft.util.Vec3;

public class Line {
	@Override
	public String toString() {
		return "Line [" + start + "] => [" + end + "]";
	}

	public final Point3D start;
	public final Point3D end;

	public Line(Point3D start, Point3D end) {
		this.start = start;
		this.end = end;
	}

	public Line() {
		this.start = new Point3D(0, 0, 0);
		this.end = new Point3D(0, 0, 0);
	}

	public Vec3 getRenderVector() {
		Vec3 vector = end.subtract(start);
		return vector.normalize();
	}

}

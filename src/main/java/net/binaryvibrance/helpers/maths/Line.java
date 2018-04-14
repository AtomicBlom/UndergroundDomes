package net.binaryvibrance.helpers.maths;

import net.binaryvibrance.undergrounddomes.generation.contracts.ILineIntersectable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Line implements ILineIntersectable {
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

	public Vec3i getRenderVector() {
		Vec3d vector = new Vec3d(end.subtract(start)).normalize();
		return new Vec3i(vector.x, vector.y, vector.z);
	}

	@Override
	public boolean intersects(Line line) {
		return false;
	}
}

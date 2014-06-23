package net.binaryvibrance.helpers.maths;

import net.binaryvibrance.undergrounddomes.generation2.contracts.ILineIntersectable;
import net.minecraft.util.Vec3;

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

	public Vec3 getRenderVector() {
		Vec3 vector = end.subtract(start);
		return vector.normalize();
	}

	@Override
	public boolean intersects(Line line) {
		return false;
	}
}

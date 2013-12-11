package net.binaryvibrance.helpers.maths;

import net.minecraft.util.Vec3;

public class GeometryHelper {
	public static boolean lineIntersectsSphere(Line line, Point3D sphereLocation, double sphereRadius) {
		Vec3 d = line.end.subtract(line.start);
		Vec3 f = line.start.subtract(sphereLocation);
		double r = sphereRadius;

		double a = d.dotProduct(d);
		double b = 2 * f.dotProduct(d);
		double c = f.dotProduct(f) - r * r;

		double discriminant = b * b - 4 * a * c;
		if (discriminant < 0) {
			// no intersection
			return false;
		}

		// ray didn't totally miss sphere,
		// so there is a solution to
		// the equation.

		discriminant = Math.sqrt(discriminant);

		// either solution may be on or off the ray so need to test both
		// t1 is always the smaller value, because BOTH discriminant and
		// a are nonnegative.
		double t1 = (-b - discriminant) / (2 * a);
		double t2 = (-b + discriminant) / (2 * a);

		// 3x HIT cases:
		// -o-> --|--> | | --|->
		// Impale(t1 hit,t2 hit), Poke(t1 hit,t2>1), ExitWound(t1<0, t2 hit),

		// 3x MISS cases:
		// -> o o -> | -> |
		// FallShort (t1>1,t2>1), Past (t1<0,t2<0), CompletelyInside(t1<0, t2>1)

		if (t1 >= 0 && t1 <= 1) {
			// t1 is an intersection, and if it hits,
			// it's closer than t2 would be
			// Impale, Poke
			return true;
		}

		// here t1 didn't intersect so we are either started
		// inside the sphere or completely past it
		if (t2 >= 0 && t2 <= 1) {
			// ExitWound
			return true;
		}

		// no intn: FallShort, Past, CompletelyInside
		return false;

	}
}

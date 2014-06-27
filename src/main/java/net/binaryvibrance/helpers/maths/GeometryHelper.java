package net.binaryvibrance.helpers.maths;

import net.binaryvibrance.undergrounddomes.generation.model.CompassDirection;
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

	public static Point3D getMidPoint(Point3D firstPoint, Point3D secondPoint, CompassDirection initialDirection) {
		Vec3 vector = initialDirection.ToVec3().normalize();

		Point3D maskedPoint = new Point3D(
				(firstPoint.xCoord * (1 - Math.abs(vector.xCoord))) + secondPoint.xCoord * Math.abs(vector.xCoord),
				(firstPoint.yCoord * (1 - Math.abs(vector.yCoord))) + secondPoint.yCoord * Math.abs(vector.yCoord),
				(firstPoint.zCoord * (1 - Math.abs(vector.zCoord))) + secondPoint.zCoord * Math.abs(vector.zCoord)
		);

		return maskedPoint;				
	}

/*	int get_line_intersection(float p0_x, float p0_y, float p1_x, float p1_y,
	                          float p2_x, float p2_y, float p3_x, float p3_y, float *i_x, float *i_y)
	{
		float s02_x, s02_y, s10_x, s10_y, s32_x, s32_y, s_numer, t_numer, denom, t;
		s10_x = p1_x - p0_x;
		s10_y = p1_y - p0_y;
		s32_x = p3_x - p2_x;
		s32_y = p3_y - p2_y;

		denom = s10_x * s32_y - s32_x * s10_y;
		if (denom == 0)
			return 0; // Collinear
		boolean denomPositive = denom > 0;

		s02_x = p0_x - p2_x;
		s02_y = p0_y - p2_y;
		s_numer = s10_x * s02_y - s10_y * s02_x;
		if ((s_numer < 0) == denomPositive)
			return 0; // No collision

		t_numer = s32_x * s02_y - s32_y * s02_x;
		if ((t_numer < 0) == denomPositive)
			return 0; // No collision

		if (((s_numer > denom) == denomPositive) || ((t_numer > denom) == denomPositive))
			return 0; // No collision
		// Collision detected
		t = t_numer / denom;
		if (i_x != NULL)
		*i_x = p0_x + (t * s10_x);
		if (i_y != NULL)
		*i_y = p0_y + (t * s10_y);

		return 1;
	}*/

	public static Point3D getLineIntersectionXZ(Line lineA, Line lineB) {
		Point3D s10 = new Point3D(
				lineA.end.x - lineA.start.x,
				lineA.end.y - lineA.start.y,
				lineA.end.z - lineA.start.z
		);
		Point3D s32 = new Point3D(
				lineB.end.x - lineB.start.x,
				lineB.end.y - lineB.start.y,
				lineB.end.z - lineB.start.z
		);

		//FIXME: 3Derize this?
		double denom = s10.x * s32.z - s32.x * s10.z;
		if (denom == 0) {
			return null; //Collinear
		}
		boolean denomPositive = denom > 0;

		Point3D s02 = new Point3D(
			lineA.start.x - lineB.start.x,
			lineA.start.y - lineB.start.y,
			lineA.start.z - lineB.start.z
		);
		double s_numer = s10.x * s02.z - s10.z * s02.x;
		if ((s_numer < 0) == denomPositive) {
			return null;
		}
		double t_numer = s32.x * s02.z - s32.z * s02.x;
		if ((t_numer < 0) == denomPositive) {
			return null;
		}

		if (((s_numer > denom) == denomPositive) || ((t_numer > denom) == denomPositive)) {
			return null;
		}

		double t = t_numer / denom;
		return new Point3D(
				lineA.start.x + (t * s10.x),
				lineA.start.y + (t * s10.y),
				lineA.start.z + (t * s10.z)
		);
	}

	private static final float EPSILON = 0.001f;

	public static boolean IsPointOnLine(Point3D linePointA, Point3D linePointB, Point3D point)
	{
		double a = (linePointB.y - linePointA.y) / (linePointB.x - linePointB.x);
		double b = linePointA.y - a * linePointA.x;
		if ( Math.abs(point.y - (a * point.x + b)) < EPSILON)
		{
			return true;
		}

		return false;
	}
}

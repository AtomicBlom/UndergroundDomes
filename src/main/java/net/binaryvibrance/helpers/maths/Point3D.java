package net.binaryvibrance.helpers.maths;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Point3D {
	@Override
	public String toString() {
		return "Point3D [" + x + "," + y + "," + z + "]";
	}

	public double x;
	public double y;
	public double z;
	public final World world;
	private final int dimension;

	public int xCoord;
	public int yCoord;
	public int zCoord;

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		world = null;
		dimension = Integer.MIN_VALUE;

		setCoords();
	}

	private void setCoords() {
		xCoord = (int) x;
		yCoord = (int) y;
		zCoord = (int) z;
	}

	public Point3D(double x, double y, double z, World world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		dimension = world != null ? world.getWorldInfo().getVanillaDimension() : Integer.MIN_VALUE;
		setCoords();
	}

	public void move(Vec3 vector) {
		x += vector.xCoord;
		y += vector.yCoord;
		z += vector.zCoord;
		setCoords();
	}

	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		setCoords();
	}

	public void set(Point3D point) {
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
		setCoords();
	}

	public Point3D add(Vec3 vector) {
		return new Point3D(x + vector.xCoord, y + vector.yCoord, z + vector.zCoord, world);
	}

	public Vec3 subtract(Point3D point) {
		return Vec3.createVectorHelper(x - point.x, y - point.y, z - point.z);
	}

	public double distance(Point3D point) {
		return Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2) + Math.pow(z - point.z, 2);
	}

	public static Point3D average(Point3D... points) {
		double count = points.length;
		long xSum = 0;
		long ySum = 0;
		long zSum = 0;
		for (Point3D point : points) {
			xSum += point.x;
			ySum += point.y;
			zSum += point.z;
		}

		return new Point3D((int) (xSum / count), (int) (ySum / count), (int) (zSum / count));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimension;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point3D other = (Point3D) obj;
		if (dimension != other.dimension)
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	/*
	 * @Override public int hashCode() { final int prime = 31; int result = 1;
	 * result = prime * result + dimension; result = prime * result + x; result
	 * = prime * result + y; result = prime * result + z; return result; }
	 * 
	 * @Override public boolean equals(Object obj) { if (this == obj) return
	 * true; if (obj == null) return false; if (getClass() != obj.getClass())
	 * return false; Point3DF other = (Point3DF) obj; if (dimension !=
	 * other.dimension) return false; if (x != other.x) return false; if (y !=
	 * other.y) return false; if (z != other.z) return false; return true; }
	 */
}

package net.binaryvibrance.undergrounddomes.generation.maths;

import net.minecraft.world.World;

public class Point3D {
	public final int x;
	public final int y;
	public final int z;
	public final World world;
	private final int dimension;
	public Point3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = null;
		dimension = Integer.MIN_VALUE;
	}
	
	public Point3D(int x, int y, int z, World world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		dimension = world != null ? world.getWorldInfo().getDimension() : Integer.MIN_VALUE;
	}
	
	public Point3D add(Vector3 vector) {
		return new Point3D(x + vector.x, y + vector.y, z + vector.z, world);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimension;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
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
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

}

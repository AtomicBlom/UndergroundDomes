package net.binaryvibrance.undergrounddomes.generation.maths;

public class Box {
	
	public final Point3D start;
	public final Point3D end;
	
	public Box(Point3D start, Point3D end) {
		this.start = new Point3D(
				Math.min(start.x, end.x),
				Math.min(start.y, end.y),
				Math.min(start.z, end.z)
				);
		this.end = new Point3D(
				Math.max(start.x, end.x),
				Math.max(start.y, end.y),
				Math.max(start.z, end.z)
				);
	}
}

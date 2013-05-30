package net.binaryvibrance.undergrounddomes.generation.maths;

public class Vector3 {
	public int x;
	public int y;
	public int z;

	public Vector3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static final Vector3[] NEIGHBOURS = new Vector3[] {
		new Vector3(0, 1, 0),
		new Vector3(0, -1, 0),
		new Vector3(1, 0, 0),
		new Vector3(-1, 0, 0),
		new Vector3(0, 0, 1),
		new Vector3(0, 0, -1) };
	
	public static final Vector3 NORTH = new Vector3(-1, 0, 0);
	public static final Vector3 SOUTH = new Vector3(1, 0, 0);
	public static final Vector3 EAST = new Vector3(0, 0, 1);
	public static final Vector3 WEST = new Vector3(0, 0, -1);
}

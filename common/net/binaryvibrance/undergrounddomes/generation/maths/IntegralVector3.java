package net.binaryvibrance.undergrounddomes.generation.maths;

public class IntegralVector3 {
	public int x;
	public int y;
	public int z;

	public IntegralVector3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	private static IntegralVector3[] neighbours;
	public static IntegralVector3[] getNeighbours() {
		if (neighbours == null) {
			neighbours = new IntegralVector3[] {
					new IntegralVector3(0, 1, 0),
					new IntegralVector3(0, -1, 0),
					new IntegralVector3(1, 0, 0),
					new IntegralVector3(-1, 0, 0),
					new IntegralVector3(0, 0, 1),
					new IntegralVector3(0, 0, -1)
			};
		}
		return neighbours;
	}
}

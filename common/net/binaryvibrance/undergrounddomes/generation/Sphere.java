package net.binaryvibrance.undergrounddomes.generation;

public class Sphere {
	private XYZTuple<Integer> location;
	private int diameter;

	public Sphere(XYZTuple<Integer> location, int diameter) {
		this.setLocation(location);
		this.setDiameter(diameter);
	}

	public XYZTuple<Integer> getLocation() {
		return location;
	}

	public void setLocation(XYZTuple<Integer> location) {
		this.location = location;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}
}
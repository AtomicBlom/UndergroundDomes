package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.undergrounddomes.generation.maths.IntegralVector3;

public class SphereInstance extends IntegralVector3 {
	private int diameter;
	private float radius;

	public SphereInstance(IntegralVector3 location, int diameter) {
		super(location.x,location.y,location.z);
		this.diameter = diameter;
		this.radius = diameter / 2.0f;
	}

	public int getDiameter() {
		return diameter;
	}
	
	public double getRadius() {
		return radius;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}
}
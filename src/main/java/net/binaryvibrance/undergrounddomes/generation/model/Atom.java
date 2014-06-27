package net.binaryvibrance.undergrounddomes.generation.model;

import net.binaryvibrance.helpers.maths.Point3D;

public class Atom extends Point3D {
	private AtomElement atomElement;

	public Atom(AtomElement initialType, int x, int y, int z) {
		super(x, y, z);
		this.setAtomElement(initialType);
	}

	public AtomElement getAtomElement() {
		return atomElement;
	}

	public void setAtomElement(AtomElement atomElement) {
		this.atomElement = atomElement;
	}
}
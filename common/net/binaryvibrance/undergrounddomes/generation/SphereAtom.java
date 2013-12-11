package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.helpers.maths.Point3D;

public class SphereAtom extends Point3D {
	private ParticleType particleType;

	public SphereAtom(ParticleType initialType, int x, int y, int z) {
		super(x, y, z);
		this.setParticleType(initialType);
	}

	public ParticleType getParticleType() {
		return particleType;
	}

	public void setParticleType(ParticleType particleType) {
		this.particleType = particleType;
	}
}
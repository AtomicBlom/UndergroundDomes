package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.undergrounddomes.generation.maths.IntegralVector3;

public class SphereAtom extends IntegralVector3 {
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
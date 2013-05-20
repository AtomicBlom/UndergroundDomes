package net.binaryvibrance.undergrounddomes.generation;

public class SphereParticle {
	private ParticleType particleType;
	private XYZTuple<Integer> location;

	public SphereParticle(ParticleType initialType, int x, int y, int z) {
		this.setParticleType(initialType);
		this.setLocation(new XYZTuple<Integer>(x, y, z));
	}

	public ParticleType getParticleType() {
		return particleType;
	}

	public void setParticleType(ParticleType particleType) {
		this.particleType = particleType;
	}

	public XYZTuple<Integer> getLocation() {
		return location;
	}

	public void setLocation(XYZTuple<Integer> location) {
		this.location = location;
	}

}
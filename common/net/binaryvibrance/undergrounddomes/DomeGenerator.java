package net.binaryvibrance.undergrounddomes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IWorldGenerator;

public class DomeGenerator implements IWorldGenerator {

	@SuppressWarnings("unused")
	private Logger log;

	private ArrayList<XYZTuple<Integer>> xyzScan;
	public final int baseLevel = 96;
	public DomeGenerator() {
		log = FMLLog.getLogger();
		xyzScan = new ArrayList<XYZTuple<Integer>>();
		xyzScan.add(new XYZTuple<Integer>(0, 1, 0));
		xyzScan.add(new XYZTuple<Integer>(0, -1, 0));
		xyzScan.add(new XYZTuple<Integer>(1, 0, 0));
		xyzScan.add(new XYZTuple<Integer>(-1, 0, 0));
		xyzScan.add(new XYZTuple<Integer>(0,0,1));
		xyzScan.add(new XYZTuple<Integer>(0,0,-1));
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId) {
			case 0: 
				if (chunkX % 8 == 0 && chunkZ % 8 == 0) {
					generateSurface(world, random, chunkX * 16, chunkZ * 16, chunkGenerator, chunkProvider);
				}
				break;
		}		
	}

	private void generateSurface(World world, Random random, int x, int z, IChunkProvider generator, IChunkProvider provider) {
		
		int sphereChain = random.nextInt(14) + 2;
		LinkedList<Sphere> generatedSpheres = new LinkedList<Sphere>();		
		
		int originX = random.nextInt(16) + x;
		int originZ = random.nextInt(16) + z;
		int originY = baseLevel + (random.nextInt(16) - 8);
		XYZTuple<Integer> origin = new XYZTuple<Integer>(originX, originY, originZ);
		int diameter = random.nextInt(16) + 10;
		
		Sphere sphere = new Sphere(origin, diameter);
		
		provideChunks(sphere, provider);
		generateSphere(sphere, world);
		generatedSpheres.add(sphere);
		
		Sphere previousSphere = sphere;
		
		double twoPi = Math.PI * 2;
		
		for (int sphereIndex = 0; sphereIndex < sphereChain; ++sphereIndex) {
			for (int tries = 0; tries < 10; ++tries) {
				
				double angle = random.nextDouble() * twoPi;
				int spacing = (random.nextInt(5) + 1);
				diameter = random.nextInt(16) + 10;
				int distance = previousSphere.getDiameter() + (spacing) + diameter;
								
				originX = previousSphere.getLocation().x + (int)(Math.sin(angle) * distance);
				originZ = previousSphere.getLocation().z + (int)(Math.cos(angle) * distance);
				originY = previousSphere.getLocation().y + (random.nextInt(16) - 8);
				
				boolean canGenerate = true;
				for (Sphere existingSphere : generatedSpheres) {
					XYZTuple<Integer> location = existingSphere.getLocation();
					double checkDistance = Math.pow(originX - location.x, 2) + Math.pow(originY - location.y, 2) + Math.pow(originZ - location.z, 2);
					double minimumDistance = Math.pow(diameter + existingSphere.getDiameter(), 2);
					if (checkDistance < minimumDistance) {
						canGenerate = false;
						break;
					}					
				}
				
				if (canGenerate) {
					origin = new XYZTuple<Integer>(originX, originY, originZ);
					sphere = new Sphere(origin, diameter);
					
					provideChunks(sphere, provider);
					generateSphere(sphere, world);
					generatedSpheres.add(sphere);
				}						
			}			
		}	
	}
	
	private void provideChunks(Sphere sphere, IChunkProvider provider) {
		float radius = sphere.getDiameter() / 2;
		XYZTuple<Integer> origin = sphere.getLocation();
		int minChunkX = (int)(origin.x - radius) % 16;
		int maxChunkX = (int)(origin.x + radius) % 16;
		int minChunkZ = (int)(origin.z - radius) % 16;
		int maxChunkZ = (int)(origin.z + radius) % 16;
		
		for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
			for (int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
				provider.provideChunk(chunkX, chunkZ);
			}
		}
	}
	
	private void generateSphere(Sphere sphere, World world) {
		int diameter = sphere.getDiameter();
		XYZTuple<Integer> origin = sphere.getLocation();
		
		float radius = diameter / 2;
		
		SphereParticle[][][] particles = new SphereParticle[diameter+1][diameter+1][diameter+1];
		double desiredWidth = Math.pow(radius, 2);
		XYZTuple<Float> sphereCentre = new XYZTuple<Float>(radius, radius, radius);
		LinkedList<SphereParticle> matchedParticles = new LinkedList<SphereParticle>();
		//Pass 1: Determine Wall
		for (int scanZ = 0; scanZ < diameter; ++scanZ) {
			for (int scanY = 0; scanY < diameter; ++scanY) {
				for (int scanX = 0; scanX < diameter; ++scanX) {
					double dist = Math.pow(scanX - sphereCentre.x, 2) + Math.pow(scanY - sphereCentre.y, 2) + Math.pow(scanZ - sphereCentre.z, 2);
					if (dist < desiredWidth) {
						SphereParticle particle = new SphereParticle(ParticleType.Interior, scanX, scanY, scanZ);
						particles[scanZ][scanY][scanX] = particle;
						matchedParticles.add(particle);
						//
					}
				}
			}
		}
		
		//Pass 2: Determine Interior
		for (SphereParticle particle : matchedParticles) {
			XYZTuple<Integer> location = particle.getLocation();
			int neighboursSet = 0;
			int neighboursNotSet = 0;
			
			for (XYZTuple<Integer> check : xyzScan) {
				int indexZ = location.z + check.z;
				int indexY = location.y + check.y;
				int indexX = location.x + check.x;
				
				if (indexX >= 0 && indexX <= diameter && indexY >= 0 && indexY <= diameter &&indexZ >= 0 && indexZ <= diameter) {
					SphereParticle checkParticle = particles[indexZ][indexY][indexX];
					if (checkParticle == null) {
						neighboursNotSet++;
					} else {
						neighboursSet++;
					}
				}
			}
			if (neighboursSet > 0 && neighboursNotSet > 0) {
				particle.setParticleType(ParticleType.Wall);
			}
		}
		
		//Pass 3: Apply to map
		for (SphereParticle particle : matchedParticles) {
			XYZTuple<Integer> location = particle.getLocation();
			int blockLocationX = (int)(location.x - radius + origin.x);
			int blockLocationY = (int)(location.y - radius + origin.y);
			int blockLocationZ = (int)(location.z - radius + origin.z);
			
			int blockId = 0;
			switch (particle.getParticleType()) {
			case Wall:
				blockId = Block.blockSteel.blockID;
				break;
			default:
				blockId = 0;
				break;
			}		
			
			world.setBlockAndMetadataWithNotify(blockLocationX, blockLocationY, blockLocationZ, blockId, 0, 0);
		}
	}
	
	private class Sphere {
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
	
	private class SphereParticle {
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
	
	private enum ParticleType {
		Wall,
		Interior,
		Floor
	}

	private class XYZTuple<T> {
		public T x;
		public T y;
		public T z;

		public XYZTuple(T x, T y, T z) {
			this.x = x;
			this.y = y;
			this.z = z;
			
		}
	}
	
}

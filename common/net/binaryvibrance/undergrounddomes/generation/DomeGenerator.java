package net.binaryvibrance.undergrounddomes.generation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;

import net.binaryvibrance.undergrounddomes.generation.maths.IntegralVector3;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IWorldGenerator;

//TODO: Split this into Dome Generation and walkway generation.
public class DomeGenerator implements IWorldGenerator {

	private static Logger log = FMLLog.getLogger();

	public final int baseLevel = 96;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId) {
		case 0:
			if (chunkX % 16 == 0 && chunkZ % 16 == 0) {
				generateSurface(world, random, chunkX * 16, chunkZ * 16,
						chunkGenerator, chunkProvider);
			}
			break;
		}
	}

	private void generateSurface(World world, Random random, int x, int z,
			IChunkProvider generator, IChunkProvider provider) {

		int sphereChain = random.nextInt(14) + 2;
		LinkedList<SphereInstance> generatedSpheres = new LinkedList<SphereInstance>();

		int originX = random.nextInt(16) + x;
		int originZ = random.nextInt(16) + z;
		int originY = baseLevel + (random.nextInt(16) - 8);
		
		IntegralVector3 origin = new IntegralVector3(originX, originY, originZ);
		int diameter = random.nextInt(16) + 10;

		log.info(String.format("First Sphere @ (%d,%d,%d) d:%d", originX, originY, originZ, diameter));
		
		SphereInstance sphere = new SphereInstance(origin, diameter);
		sphere.createFloors(random);
		
		provideChunks(sphere, provider);
		generateSphere(sphere, world);
		generatedSpheres.add(sphere);

		SphereInstance previousSphere = sphere;

		for (int sphereIndex = 0; sphereIndex < sphereChain; ++sphereIndex) {
			for (int tries = 0; tries < 10; ++tries) {
				// FIXME: Attempt to generate AWAY from players.
				diameter = random.nextInt(16) + 10;
				diameter = diameter + diameter % 2;
				double radius = diameter / 2.0f;
				double touchingDistance = (previousSphere.getDiameter() / 2.0f + radius);

				int prevX = previousSphere.x;
				int prevZ = previousSphere.z;
				
				int xDirection = random.nextBoolean() ? -1 : 1;
				int zDirection = random.nextBoolean() ? -1 : 1;
				boolean firstDirectionIsXAxis = random.nextBoolean();
				
				int minCoridorSpacing = 6;
								
				double firstOffset = (radius + random.nextInt(12));
				int newSpacing = random.nextInt(8);
				if (firstDirectionIsXAxis) {
					originZ = (int)( prevZ + (firstOffset + minCoridorSpacing) * zDirection);					
					originX = (int)( prevX + (minCoridorSpacing + Math.sqrt(Math.pow(Math.max(touchingDistance, firstOffset + 1), 2) - Math.pow(firstOffset, 2)) + newSpacing) * xDirection);
					log.info(String.format("originX: %d, prevX: %d, touch: %f, offset: %f, spacing: %d", originX, prevX, touchingDistance, firstOffset, newSpacing));
				} else {
					originX = (int)( prevX + (firstOffset + minCoridorSpacing) * xDirection);
					originZ = (int)( prevZ + (minCoridorSpacing + Math.sqrt(Math.pow(Math.max(touchingDistance, firstOffset + 1), 2) - Math.pow(firstOffset, 2)) + newSpacing) * zDirection);
					log.info(String.format("originZ: %d, prevZ: %d, touch: %f, offset: %f, spacing: %d", originZ, prevZ, touchingDistance, firstOffset, newSpacing));
				}				
				
				boolean canGenerate = true;
				for (SphereInstance existingSphere : generatedSpheres) {
					double checkDistance = Math.pow(originX - existingSphere.x, 2)
							+ Math.pow(originY - existingSphere.y, 2)
							+ Math.pow(originZ - existingSphere.z, 2);
					double minimumDistance = Math.pow(
							radius + existingSphere.getRadius(), 2);
					if (checkDistance < minimumDistance) {
						canGenerate = false;
						break;
					}
				}

				if (canGenerate) {
					log.info(String.format("Sphere %d/%d @ (%d,%d,%d) d:%d, o:%d", (sphereIndex + 1), sphereChain, originX, originY, originZ, diameter, firstDirectionIsXAxis ? 1 : 0));
					boolean preferX = random.nextBoolean();
					origin = new IntegralVector3(originX, originY, originZ);
					sphere = new SphereInstance(origin, diameter);
					sphere.createFloors(random);
					provideChunks(sphere, provider);
					generateSphere(sphere, world);
					generatedSpheres.add(sphere);
					generateWalkway(sphere, previousSphere, world, preferX);		
					
					previousSphere = sphere;
					break;
				}
			}
		}
	}
	
	private void generateWalkway(SphereInstance sphere, IntegralVector3 previousSphere, World world, boolean preferX) {
		int x, z, step;
		
		int y = sphere.getTranslatedFloorLevel(0);
		
		if (preferX) {
			step = previousSphere.x < sphere.x ? 1 : -1;
			for (x = previousSphere.x, z = previousSphere.z; x != sphere.x; x += step) {
				world.setBlock(x, y, z, Block.blockGold.blockID, 0, 0);
			}
			
			step = previousSphere.z < sphere.z ? 1 : -1;
			for (z = previousSphere.z, x = sphere.x; z != sphere.z; z += step) {
				world.setBlock(x, y, z, Block.blockDiamond.blockID, 0, 0);
			}
		} else {
			step = previousSphere.z < sphere.z ? 1 : -1;
			for (z = previousSphere.z, x = previousSphere.x; z != sphere.z; z += step) {
				world.setBlock(x, y, z, Block.blockGold.blockID, 0, 0);
			}
			
			step = previousSphere.x < sphere.x ? 1 : -1;
			for (x = previousSphere.x, z = sphere.z; x != sphere.x; x += step) {
				world.setBlock(x, y, z, Block.blockDiamond.blockID, 0, 0);
			}			
		}	
	}

	private void provideChunks(SphereInstance sphere, IChunkProvider provider) {
		float radius = sphere.getDiameter() / 2;
		int minChunkX = (int) (sphere.x - radius) % 16;
		int maxChunkX = (int) (sphere.x + radius) % 16;
		int minChunkZ = (int) (sphere.z - radius) % 16;
		int maxChunkZ = (int) (sphere.z + radius) % 16;

		for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
			for (int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
				provider.provideChunk(chunkX, chunkZ);
			}
		}
	}

	private HashMap<Integer, Sphere> cachedSpheres = new HashMap<Integer, Sphere>();
	
	private void generateSphere(SphereInstance sphereInstance, World world) {
		double radius = sphereInstance.getRadius();
		Sphere sphere;
		int diameter = sphereInstance.getDiameter();
		if (cachedSpheres.containsKey(diameter)) {
			log.info(String.format("Found precalculated atoms for diameter %d", diameter));
			sphere = cachedSpheres.get(diameter);
		} else {
			log.info(String.format("Precalculating atoms for diameter %d", diameter));
			sphere = new Sphere(diameter);
			cachedSpheres.put(diameter, sphere);
			log.info(String.format("Precalculation for diameter %d complete", diameter));
		}
		
		
		SphereAtom[][][] atoms = sphere.getAtoms();
		for (int z = 0; z < diameter; ++z){
			for (int y = 0; y < diameter; ++y) {
				boolean isFloor = sphereInstance.isFloorLevel(y);
				for (int x = 0; x < diameter; ++x) {
					SphereAtom atom = atoms[z][y][x];
					if (atom == null) {
						continue;
					}
					
					int blockLocationX = (int) (x - radius + sphereInstance.x);
					int blockLocationY = (int) (y - radius + sphereInstance.y);
					int blockLocationZ = (int) (z - radius + sphereInstance.z);

					int blockId;
					
					switch (atom.getParticleType()) {
					case Wall:
						blockId = Block.blockIron.blockID;
						break;
					default:
						blockId = isFloor ? Block.glowStone.blockID : 0 ;
						break;
					}
					blockId = isFloor ? Block.glowStone.blockID : blockId ;
					
					world.setBlock(blockLocationX, blockLocationY,
							blockLocationZ, blockId, 0, 0);
				}
			}
		}
	}
}

package net.binaryvibrance.undergrounddomes.generation;

import java.util.Random;

import net.binaryvibrance.helpers.maths.Point3D;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class DomeGenerator implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId) {
		case 0:
			if (chunkX % 16 == 0 && chunkZ % 16 == 0) {
				generateSurface(world, random, chunkX * 16, chunkZ * 16, chunkGenerator, chunkProvider);
			}
			break;
		}
	}

	private void generateSurface(World world, Random random, int x, int z, IChunkProvider generator, IChunkProvider provider) {
		// TODO: allow the SphereChain to be aware of chunks that have yet to be
		// created.
		SphereChain sphereChain2 = new SphereChain(random, x, z);
		sphereChain2.buildChain();

		CorridorGen corridorGen = new CorridorGen(sphereChain2);
		corridorGen.generateCorridor();

		// TODO: Calculate SphereChain bounds
		for (Point3D chunk : sphereChain2.getRequiredChunks()) {
			provider.provideChunk(chunk.xCoord, chunk.zCoord);
		}
		sphereChain2.renderSpheres(world);
		corridorGen.renderCorridor(world);
	}

	/*
	 * private void generateWalkway(SphereInstance sphere, Point3D
	 * previousSphere, World world, boolean preferX) { int x, z, step;
	 * 
	 * int y = sphere.getTranslatedFloorLevel(0);
	 * 
	 * if (preferX) { step = previousSphere.x < sphere.x ? 1 : -1; for (x =
	 * previousSphere.x, z = previousSphere.z; x != sphere.x; x += step) {
	 * world.setBlock(x, y, z, Block.blockGold.blockID, 0, 2); }
	 * 
	 * step = previousSphere.z < sphere.z ? 1 : -1; for (z = previousSphere.z, x
	 * = sphere.x; z != sphere.z; z += step) { world.setBlock(x, y, z,
	 * Block.blockDiamond.blockID, 0, 2); } } else { step = previousSphere.z <
	 * sphere.z ? 1 : -1; for (z = previousSphere.z, x = previousSphere.x; z !=
	 * sphere.z; z += step) { world.setBlock(x, y, z, Block.blockGold.blockID,
	 * 0, 2); }
	 * 
	 * step = previousSphere.x < sphere.x ? 1 : -1; for (x = previousSphere.x, z
	 * = sphere.z; x != sphere.x; x += step) { world.setBlock(x, y, z,
	 * Block.blockDiamond.blockID, 0, 2); } } }
	 */
}

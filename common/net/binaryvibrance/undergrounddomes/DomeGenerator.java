package net.binaryvibrance.undergrounddomes;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class DomeGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId) {
			case 0: 
				generateSurface(world, random, chunkX * 16, chunkZ * 16); 
				break;
		}
		
	}

	private void generateSurface(World world, Random random, int x, int z) {
		// 
		
	}

}

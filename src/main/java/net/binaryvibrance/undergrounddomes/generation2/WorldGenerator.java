package net.binaryvibrance.undergrounddomes.generation2;

import java.util.Random;

import net.binaryvibrance.undergrounddomes.Configuration;
import net.binaryvibrance.undergrounddomes.generation2.model.DomeSet;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenerator implements IWorldGenerator{

	private final Configuration _configuration;

	public WorldGenerator() {
		_configuration = Configuration.getConfiguration();
	}

	private DomeSet currentDomeSet;
	private final Object domeSetLock = new Object();
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

		if (_configuration.getMultiThreaded()) {
			synchronized(domeSetLock) {
				if (currentDomeSet == null) {
					currentDomeSet = new DomeSet(

							new GenADomeGenerator(),
							new GenACorridorGenerator()
					);
					currentDomeSet.create();
				}
			}

			if (currentDomeSet.tryAcquireLock()) {
				//Render dome to current chunk
			}
		} else {
			DomeSet domeSet = new DomeSet(
					_configuration.getDefaultDomeGenerator(),
					_configuration.getDefaultCorridorGenerator()
			);
			domeSet.startGeneration();


		}


		
	}
	
}

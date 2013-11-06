package net.binaryvibrance.undergrounddomes.generation2;

import java.util.Random;

import net.binaryvibrance.undergrounddomes.generation2.model.DomeSet;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenerator implements IWorldGenerator{

	private DomeSet currentDomeSet;
	private Object domeSetLock = new Object();
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		// TODO Auto-generated method stub
		
		synchronized(domeSetLock) {
			if (currentDomeSet == null) {
				currentDomeSet = new DomeSet(
						
						new GenADomeGenerator(),
						new GenACorridorGenerator()
						);
				currentDomeSet.create();
			}
		}
		
		if (currentDomeSet.tryAquireLock()) {
			//Render dome to current chunk
		}
		
	}
	
}

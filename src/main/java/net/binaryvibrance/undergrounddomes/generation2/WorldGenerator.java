package net.binaryvibrance.undergrounddomes.generation2;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameData;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.Configuration;
import net.binaryvibrance.undergrounddomes.generation2.contracts.ICorridorGenerator;
import net.binaryvibrance.undergrounddomes.generation2.contracts.IDomeGenerator;
import net.binaryvibrance.undergrounddomes.generation2.model.Atom;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomElement;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation2.model.DomeSet;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenerator implements IWorldGenerator {

	private final Configuration _configuration;

	public WorldGenerator() {
		_configuration = Configuration.getConfiguration();
	}

	private DomeSet currentDomeSet;
	private final Object domeSetLock = new Object();
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

		//TODO: determine if domeSet is appropriate for this biome
		//      I'm thinking high altitude biomes?
		switch (world.provider.dimensionId) {
			case 0:
				if (chunkX % 16 == 0 && chunkZ % 16 == 0) {
					generateSurface(world, chunkX, chunkZ, chunkGenerator, chunkProvider);
				}
				break;
		}

	}

	private void generateSurface(World world, int x, int z, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		//TODO: batch multiple generations
		//      I'm expecting MystCraft to cause interesting cpu load here.
		//TODO: recover from server shutdown.
		//      Persist base calculations and data to nbt
		//TODO: gracefully handle exceptions in generation code

		Random r = new Random();
		r.setSeed(1 + x ^ 31 + z ^ 17 + world.getWorldInfo().getSeed() ^ 13);

		IDomeGenerator domeGenerator = _configuration.getDefaultDomeGenerator();
		domeGenerator.setRandom(r);
		ICorridorGenerator corridorGenerator = _configuration.getDefaultCorridorGenerator();
		corridorGenerator.setRandom(r);

		DomeSet domeSet;
		domeSet = new DomeSet(x, z, domeGenerator,corridorGenerator);

		if (_configuration.getMultiThreaded()) {
			synchronized(domeSetLock) {
				if (currentDomeSet == null) {
					currentDomeSet = domeSet;
					currentDomeSet.startGenerationAsync();
				}
			}
		} else {
			domeSet.startGeneration();
			AtomField atomField = domeSet.getAtomField();

			List<DomeSet.ChunkData> requiredChunks = domeSet.getRequiredChunks();
			for (DomeSet.ChunkData chunk : requiredChunks) {
				Point3D chunkLocation = chunk.getChunkLocation();
				chunkProvider.provideChunk(chunkLocation.xCoord, chunkLocation.zCoord);
				populateChunk(chunk, world);
			}
		}
	}

	private void populateChunk(DomeSet.ChunkData chunk, World world) {
		Point3D chunkLocation = chunk.getChunkLocation();
		int realX = chunkLocation.xCoord * 16;
		int realZ = chunkLocation.zCoord * 16;
		int realY = _configuration.getDomeHeight();

		Atom[][][] atoms = chunk.getAtoms();

		Block glowStoneBlock = GameData.getBlockRegistry().getObject("glowstone");
		Block ironBlock = GameData.getBlockRegistry().getObject("tnt");
		Block airBlock = GameData.getBlockRegistry().getObject("air");

		for (int z = 0; z < atoms.length; ++z) {
			for (int y = 0; y < atoms[z].length; ++y) {
				for (int x = 0; x < atoms[z][y].length; ++x) {
					Block block;
					switch (atoms[z][y][x].getAtomElement()) {
						case Wall:
							block = ironBlock;
							break;
						case Interior:
							block = airBlock;
							break;
						case Floor:
							block = glowStoneBlock;
							break;
						case Untouched:
							continue;
						default:
							continue;
					}

					world.setBlock(x + realX, y + realY, z + realZ, block, 0, 2);
				}
			}
		}
	}

	@SubscribeEvent
	public void OnTick(TickEvent event) {

		if (_configuration.getMultiThreaded()) {
			if (currentDomeSet.tryAcquireLock()) {
				DomeSet domeSet = currentDomeSet;
				currentDomeSet = null;
				//TODO: for a single completed dome set, render a single chunk worth of atoms.
				AtomField atomField = domeSet.getAtomField();
			}
		}
	}
	
}

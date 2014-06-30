package net.binaryvibrance.undergrounddomes.generation;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.configuration.ConfigurationHandler;
import net.binaryvibrance.undergrounddomes.generation.contracts.INotifyDomeGenerationComplete;
import net.binaryvibrance.undergrounddomes.generation.model.Atom;
import net.binaryvibrance.undergrounddomes.generation.model.AtomElement;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorldGenerator implements IWorldGenerator, INotifyDomeGenerationComplete {
	private Queue<DomeRequestResult> readyResults = new ConcurrentLinkedQueue<DomeRequestResult>();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

		//TODO: determine if domeSet is appropriate for this biome
		//      I'm thinking high altitude biomes?
		switch (world.provider.dimensionId) {
			case 0:
				if (chunkX % 16 == 0 && chunkZ % 16 == 0) {
					generateSurface(world, chunkX, chunkZ, chunkProvider);
				}
				break;
		}

	}

	private void generateSurface(World world, int x, int z, IChunkProvider chunkProvider) {
		//TODO: recover from server shutdown.
		//      Persist base calculations and data to nbt
		//TODO: gracefully handle exceptions in generation code
		ConfigurationHandler configuration = ConfigurationHandler.instance();
		DomeRequest domeRequest;
		domeRequest = new DomeRequest(x, z, world, chunkProvider, this);

		if (configuration.getMultiThreadedWorldGen()) {
			domeRequest.startGenerationAsync();
		} else {
			DomeRequestResult generationResult = domeRequest.startGeneration();
			while (generationResult.hasMoreChunkData()) {
				processGenerationResult(generationResult);
			}
		}
	}


	@Override
	public void OnComplete(DomeRequestResult requestResult) {
		readyResults.offer(requestResult);
	}

	long currentTick;

	@SubscribeEvent
	public synchronized void OnTick(TickEvent.ServerTickEvent event) {
		if (!readyResults.isEmpty()) {
			//Yield, only update 4 times a second.
			currentTick++;
			if (currentTick % 5 != 0) {
				return;
			}

			DomeRequestResult requestResult = readyResults.peek();
			processGenerationResult(requestResult);
			if (!requestResult.hasMoreChunkData()) {
				readyResults.poll();
			}
		}
	}

	private void processGenerationResult(DomeRequestResult requestResult) {
		DomeRequestResult.ChunkData chunkData = requestResult.getNextChunkData();
		if (chunkData != null) {
			Point3D chunkLocation = chunkData.getChunkLocation();
			LogHelper.info("Processing chunk at %s", chunkLocation);
			requestResult.getChunkProvider().provideChunk(chunkLocation.xCoord, chunkLocation.zCoord);
			populateChunk(chunkData, requestResult.getWorld());
		}
	}

	private void populateChunk(DomeRequestResult.ChunkData chunk, World world) {
		ConfigurationHandler configuration = ConfigurationHandler.instance();
		Point3D chunkLocation = chunk.getChunkLocation();
		int realX = chunkLocation.xCoord * 16;
		int realZ = chunkLocation.zCoord * 16;
		int realY = configuration.getDomeHeight();

		Atom[][][] atoms = chunk.getAtoms();

		FMLControlledNamespacedRegistry<Block> blockRegistry = GameData.getBlockRegistry();
		//TODO: Abstract this out, it's getting a bit clunky
		Dictionary<AtomElement, Block> blockMapping = new Hashtable<AtomElement, Block>();
		blockMapping.put(AtomElement.Floor, blockRegistry.getObject("glowstone"));
		blockMapping.put(AtomElement.Wall, blockRegistry.getObject("mossy_cobblestone"));
		blockMapping.put(AtomElement.Interior, blockRegistry.getObject("air"));
		blockMapping.put(AtomElement.CorridorFloor, blockRegistry.getObject("iron_block"));
		blockMapping.put(AtomElement.CorridorMidpoint, blockRegistry.getObject("gold_block"));
		blockMapping.put(AtomElement.CorridorEntrance, blockRegistry.getObject("diamond_block"));
		blockMapping.put(AtomElement.Debug, blockRegistry.getObject("glass"));

		for (int z = 0; z < atoms.length; ++z) {
			for (int y = 0; y < atoms[z].length; ++y) {
				for (int x = 0; x < atoms[z][y].length; ++x) {
					Block block;
					Atom atom = atoms[z][y][x];
					AtomElement element = AtomElement.Untouched;
					if (atom != null) {
						element = atom.getAtomElement();
					}

					block = blockMapping.get(element);
					if (block == null) {
						continue;
					}

					world.setBlock(x + realX, y + realY, z + realZ, block, 0, 2);
				}
			}
		}
	}
}
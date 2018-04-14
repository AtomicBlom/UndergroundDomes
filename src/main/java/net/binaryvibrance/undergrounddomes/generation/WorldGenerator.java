package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.configuration.ConfigurationHandler;
import net.binaryvibrance.undergrounddomes.generation.contracts.INotifyDomeGenerationComplete;
import net.binaryvibrance.undergrounddomes.generation.model.Atom;
import net.binaryvibrance.undergrounddomes.generation.model.AtomElement;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IBlockStatePalette;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.registries.GameData;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorldGenerator implements IWorldGenerator, INotifyDomeGenerationComplete {
	private Queue<DomeRequestResult> readyResults = new ConcurrentLinkedQueue<DomeRequestResult>();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
	                     IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

		//TODO: determine if domeSet is appropriate for this biome
		//      I'm thinking high altitude biomes?
		switch (world.provider.getDimension()) {
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
			LogHelper.info("Processing chunk at {}", chunkLocation);
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

		//TODO: Abstract this out, it's getting a bit clunky
		Dictionary<AtomElement, IBlockState> blockMapping = new Hashtable<>();
		blockMapping.put(AtomElement.Floor, Blocks.GLOWSTONE.getDefaultState());
		blockMapping.put(AtomElement.Wall, Blocks.MOSSY_COBBLESTONE.getDefaultState());
		blockMapping.put(AtomElement.Interior, Blocks.AIR.getDefaultState());
		blockMapping.put(AtomElement.CorridorFloor, Blocks.IRON_BLOCK.getDefaultState());
		blockMapping.put(AtomElement.CorridorMidpoint, Blocks.GOLD_BLOCK.getDefaultState());
		blockMapping.put(AtomElement.CorridorEntrance, Blocks.DIAMOND_BLOCK.getDefaultState());
		blockMapping.put(AtomElement.Debug, Blocks.GLASS.getDefaultState());

		for (int z = 0; z < atoms.length; ++z) {
			for (int y = 0; y < atoms[z].length; ++y) {
				for (int x = 0; x < atoms[z][y].length; ++x) {
					IBlockState block;
					Atom atom = atoms[z][y][x];
					AtomElement element = AtomElement.Untouched;
					if (atom != null) {
						element = atom.getAtomElement();
					}

					block = blockMapping.get(element);
					if (block == null) {
						continue;
					}

					world.setBlockState(new BlockPos(x + realX, y + realY, z + realZ), block, 2);
				}
			}
		}
	}
}

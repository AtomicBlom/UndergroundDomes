package net.binaryvibrance.undergrounddomes.generation2;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.Configuration;
import net.binaryvibrance.undergrounddomes.generation2.contracts.INotifyDomeGenerationComplete;
import net.binaryvibrance.undergrounddomes.generation2.model.Atom;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomElement;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorldGenerator implements IWorldGenerator, INotifyDomeGenerationComplete {
	private final Configuration _configuration;

	public WorldGenerator() {
		_configuration = Configuration.getConfiguration();
	}

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

		DomeRequest domeRequest;
		domeRequest = new DomeRequest(x, z, world, chunkProvider, this);

		if (_configuration.getMultiThreaded()) {
			domeRequest.startGenerationAsync();
		} else {
			domeRequest.startGeneration();
		}
	}


	@Override
	public void OnComplete(DomeRequestResult requestResult) {
		readyResults.offer(requestResult);
	}

	long currentTick;

	@SubscribeEvent
	public synchronized void OnTick(TickEvent event) {
		if (event.side == Side.SERVER && !readyResults.isEmpty()) {
			/*currentTick++;
			if (currentTick % 10 != 0) {
				return;
			}*/
			DomeRequestResult requestResult = readyResults.peek();
			World world = requestResult.getWorld();

			DomeRequest.ChunkData chunkData = requestResult.getNextChunkData();
			if (chunkData != null) {
				Point3D chunkLocation = chunkData.getChunkLocation();
				LogHelper.info("Processing chunk at " + chunkLocation);
				requestResult.getChunkProvider().provideChunk(chunkLocation.xCoord, chunkLocation.zCoord);
				populateChunk(chunkData, requestResult.getWorld());
			}
			if (!requestResult.hasMoreChunkData()) {
				readyResults.poll();
			}
		}
	}

	private void populateChunk(DomeRequest.ChunkData chunk, World world) {
		Point3D chunkLocation = chunk.getChunkLocation();
		int realX = chunkLocation.xCoord * 16;
		int realZ = chunkLocation.zCoord * 16;
		int realY = _configuration.getDomeHeight();

		Atom[][][] atoms = chunk.getAtoms();

		FMLControlledNamespacedRegistry<Block> blockRegistry = GameData.getBlockRegistry();
		Block floorBlock = blockRegistry.getObject("glowstone");
		Block wallBlock = blockRegistry.getObject("tnt");
		Block interiorBlock = blockRegistry.getObject("air");
		Block debugBlock = blockRegistry.getObject("gold_block");
		Block corridorFloorBlock = blockRegistry.getObject("iron_block");

		for (int z = 0; z < atoms.length; ++z) {
			for (int y = 0; y < atoms[z].length; ++y) {
				for (int x = 0; x < atoms[z][y].length; ++x) {
					Block block;
					Atom atom = atoms[z][y][x];
					AtomElement element = AtomElement.Untouched;
					if (atom != null) {
						element = atom.getAtomElement();
					}
					switch (element) {
						case Wall:
							block = wallBlock;
							break;
						case Interior:
							block = interiorBlock;
							break;
						case Floor:
							block = floorBlock;
							break;
						case CorridorFloor:
							block = corridorFloorBlock;
							break;
						case Debug:
							block = debugBlock;
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
}

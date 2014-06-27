package net.binaryvibrance.undergrounddomes.generation;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
* Created by CodeWarrior on 26/06/2014.
*/
public class DomeRequestResult {

	private final World world;
	private final IChunkProvider chunkProvider;
	private final int chunkX;
	private final int chunkZ;
	private Queue<DomeRequest.ChunkData> chunkData;

	public DomeRequestResult(World world, IChunkProvider chunkProvider, int chunkX, int chunkZ) {

		this.world = world;
		this.chunkProvider = chunkProvider;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.chunkData = new LinkedList<DomeRequest.ChunkData>();
	}

	public World getWorld() {
		return world;
	}

	public IChunkProvider getChunkProvider() {
		return chunkProvider;
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkZ() {
		return chunkZ;
	}

	public void setChunkData(List<DomeRequest.ChunkData> chunkData) {
		this.chunkData.addAll(chunkData);
	}

	public boolean hasMoreChunkData() {
		return !chunkData.isEmpty();
	}

	public DomeRequest.ChunkData getNextChunkData() {
		if (chunkData.isEmpty()) {
			return null;
		}
		return chunkData.remove();
	}
}

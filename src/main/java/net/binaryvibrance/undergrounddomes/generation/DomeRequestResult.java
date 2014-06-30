package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.model.Atom;
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
	private Queue<ChunkData> chunkData;

	public DomeRequestResult(World world, IChunkProvider chunkProvider) {

		this.world = world;
		this.chunkProvider = chunkProvider;
		this.chunkData = new LinkedList<ChunkData>();
	}

	public World getWorld() {
		return world;
	}

	public IChunkProvider getChunkProvider() {
		return chunkProvider;
	}

	public void setChunkData(List<ChunkData> chunkData) {
		this.chunkData.addAll(chunkData);
	}

	public boolean hasMoreChunkData() {
		return !chunkData.isEmpty();
	}

	public ChunkData getNextChunkData() {
		if (chunkData.isEmpty()) {
			return null;
		}
		return chunkData.remove();
	}

	public static class ChunkData {
		private final Point3D chunkLocation;
		private final Atom[][][] atoms;

		public ChunkData(Point3D chunkLocation, Atom[][][] atoms) {

			this.chunkLocation = chunkLocation;
			this.atoms = atoms;
		}

		public Point3D getChunkLocation() {
			return chunkLocation;
		}

		public Atom[][][] getAtoms() {
			return atoms;
		}
	}
}

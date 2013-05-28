package net.binaryvibrance.undergrounddomes.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.maths.Vector3;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class SphereInstance extends Point3D {
	private static final Logger LOG = LogHelper.getLogger();
	private static final int MIN_FLOOR_SIZE = 5;
	private final int diameter;
	private final float radius;
	private List<Integer> floors;
	private final Sphere sphereAtoms;

	public SphereInstance(Point3D location, int diameter) {
		super(location.x,location.y,location.z);
		this.diameter = diameter;
		this.radius = diameter / 2.0f;
		sphereAtoms = Sphere.construct(diameter);
	}
	
	public void createFloors(Random random) {
		ArrayList<Integer> definedFloors = new ArrayList<Integer>();
		//
		int available = (int)((diameter - 2) * 0.75); //Don't include walls
		int maxFloors = (int)Math.floor(available / (float)MIN_FLOOR_SIZE);
		LOG.info("MaxFloors: " + maxFloors);
		int actualFloors = maxFloors;// == 1 ? 1 : random.nextInt(maxFloors - 1) + 1;
		int interval = (int)Math.ceil(available / actualFloors);
		int variance = interval - MIN_FLOOR_SIZE;
		
		int baseHeight = diameter - available -2;
		definedFloors.add(baseHeight);
		LOG.info(String.format("Floor 0 at level %d", baseHeight));
		for (int floor = 1; floor < actualFloors; ++floor) {
			int floorVariance = random.nextBoolean() ? 1 : -1;
			int floorStart = baseHeight + floor * interval + (variance * floorVariance);
			LOG.info(String.format("Floor %d at level %d", floor, floorStart));
			definedFloors.add(floorStart);
		}
		
		floors = definedFloors;		
	}

	public int getDiameter() {
		return diameter;
	}
	
	public double getRadius() {
		return radius;
	}

	public boolean isFloorLevel(int y) {
		for (Integer floorLevel : floors) {
			if (floorLevel == y) return true;
		}
		return false;
	}
	
	public int getFloorLevel(int index) {
		return floors.get(index);
	}
	
	public int getTranslatedFloorLevel(int index) {
		return (int)(y - radius + floors.get(index));
	}

	public void render(World world, Vector3 offset) {
		int xOffset = offset.x;
		int yOffset = offset.y;
		int zOffset = offset.z;
		
		SphereAtom[][][] atoms = sphereAtoms.getAtoms();
		for (int z = 0; z < diameter; ++z) {
			for (int y = 0; y < diameter; ++y) {
				boolean isFloor = isFloorLevel(y);
				for (int x = 0; x < diameter; ++x) {
					SphereAtom atom = atoms[z][y][x];
					if (atom == null) {
						continue;
					}

					final int blockLocationX = (int) (x - radius + this.x + xOffset);
					final int blockLocationY = (int) (y - radius + this.y + yOffset);
					final int blockLocationZ = (int) (z - radius + this.z + zOffset);

					int blockId;

					switch (atom.getParticleType()) {
					case Wall:
						blockId = Block.blockIron.blockID;
						break;
					default:
						blockId = isFloor ? Block.glowStone.blockID : 0;//Block.oreEmerald.blockID;
						break;
					}
					blockId = isFloor ? Block.glowStone.blockID : blockId;

					world.setBlock(blockLocationX, blockLocationY, blockLocationZ, blockId, 0, 0);
				}
			}
		}
	}
}
package net.binaryvibrance.undergrounddomes.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class SphereInstance extends Point3D {
	@Override
	public String toString() {
		return "SphereInstance [diameter=" + diameter + ", x=" + xCoord + ", y=" + yCoord + ", z=" + zCoord + "]";
	}

	private static final Logger LOG = LogHelper.getLogger();
	private static final int MIN_FLOOR_SIZE = 5;
	private final int diameter;
	private final float radius;
	private List<SphereFloor> floors;
	private final Sphere sphereAtoms;

	public SphereInstance(Point3D location, int diameter) {
		super(location.x, location.y, location.z);
		this.diameter = diameter;
		radius = diameter / 2.0f;
		sphereAtoms = Sphere.construct(diameter);
	}

	public void createFloors(Random random) {
		ArrayList<SphereFloor> definedFloors = new ArrayList<SphereFloor>();

		int available = (int) ((diameter - 2) * 0.75); // Don't include walls
		int maxFloors = (int) Math.floor(available / (float) MIN_FLOOR_SIZE);
		LOG.info("MaxFloors: " + maxFloors);
		int actualFloors = maxFloors;// == 1 ? 1 : random.nextInt(maxFloors - 1)
										// + 1;
		int interval = (int) Math.ceil(available / actualFloors);
		int variance = interval - MIN_FLOOR_SIZE;

		int baseHeight = diameter - available - 2;
		definedFloors.add(new SphereFloor(this, baseHeight));
		LOG.info(String.format("Floor 0 at level %d", baseHeight));
		for (int floor = 1; floor < actualFloors; ++floor) {
			int floorVariance = random.nextBoolean() ? 1 : -1;
			int floorStart = baseHeight + floor * interval + variance * floorVariance;
			LOG.info(String.format("Floor %d at level %d", floor, floorStart));
			definedFloors.add(new SphereFloor(this, floorStart));
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
		for (SphereFloor floorLevel : floors) {
			if (floorLevel.level == y)
				return true;
		}
		return false;
	}

	public SphereFloor getFloor(int index) {
		return floors.get(index);
	}

	public int getTranslatedFloorLevel(int index) {
		return (int) (y - radius + floors.get(index).level);
	}

	public void render(World world, Vec3 offset) {
		int xOffset = (int) offset.xCoord;
		int yOffset = (int) offset.yCoord;
		int zOffset = (int) offset.zCoord;

		int baseFloor = getFloor(0).level;

		SphereAtom[][][] atoms = sphereAtoms.getAtoms();
		for (int z = 0; z < diameter; ++z) {
			for (int y = baseFloor; y < diameter; ++y) {
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
						blockId = isFloor ? Block.glowStone.blockID : 0;// Block.oreEmerald.blockID;
						break;
					}
					blockId = isFloor ? Block.glowStone.blockID : blockId;

					world.setBlock(blockLocationX, blockLocationY, blockLocationZ, blockId, 0, 2);
				}
			}
		}
	}

	public boolean intersectsLine(Line line) {
		Vec3 d = line.end.subtract(line.start);
		Vec3 f = line.start.subtract(this);
		double r = this.radius;

		double a = d.dotProduct(d);
		double b = 2 * f.dotProduct(d);
		double c = f.dotProduct(f) - r * r;

		double discriminant = b * b - 4 * a * c;
		if (discriminant < 0) {
			// no intersection
			return false;
		}

		// ray didn't totally miss sphere,
		// so there is a solution to
		// the equation.

		discriminant = Math.sqrt(discriminant);

		// either solution may be on or off the ray so need to test both
		// t1 is always the smaller value, because BOTH discriminant and
		// a are nonnegative.
		double t1 = (-b - discriminant) / (2 * a);
		double t2 = (-b + discriminant) / (2 * a);

		// 3x HIT cases:
		// -o-> --|--> | | --|->
		// Impale(t1 hit,t2 hit), Poke(t1 hit,t2>1), ExitWound(t1<0, t2 hit),

		// 3x MISS cases:
		// -> o o -> | -> |
		// FallShort (t1>1,t2>1), Past (t1<0,t2<0), CompletelyInside(t1<0, t2>1)

		if (t1 >= 0 && t1 <= 1) {
			// t1 is an intersection, and if it hits,
			// it's closer than t2 would be
			// Impale, Poke
			return true;
		}

		// here t1 didn't intersect so we are either started
		// inside the sphere or completely past it
		if (t2 >= 0 && t2 <= 1) {
			// ExitWound
			return true;
		}

		// no intn: FallShort, Past, CompletelyInside
		return false;

	}
}
package net.binaryvibrance.undergrounddomes.generation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.DeveloperOptions;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class SphereChain {
	private static final Logger LOG = LogHelper.getLogger();

	int sphereChainLength;
	private final List<SphereInstance> chain;
	private final Random random;

	final int startX;
	final int startZ;

	int heightOffset;

	public SphereChain(Random random, int startX, int startZ) {
		this.random = random;
		this.startX = startX;
		this.startZ = startZ;

		chain = new LinkedList<SphereInstance>();

		// Create up to 16 spheres.
		sphereChainLength = random.nextInt(12) + 4;
	}

	public void buildChain() {
		int buildLength = 0;

		SphereInstance previousSphere = null;
		while (buildLength < sphereChainLength) {
			SphereInstance sphere = null;
			int sphereScore = Integer.MIN_VALUE;
			for (int i = 0; i < 10; i++) {
				SphereInstance potentialSphere = getPotentialSphere(previousSphere);
				if (!isValid(potentialSphere)) {
					continue;
				}
				int potentialSphereScore = getSphereScore(potentialSphere);
				if (potentialSphereScore > sphereScore) {
					sphere = potentialSphere;
					sphereScore = potentialSphereScore;
				}
			}
			if (sphere == null) {
				break;
			}
			sphere.createFloors(random);
			LOG.info(String.format("Sphere %d/%d @ (%d,%d,%d) d:%d", buildLength + 1, sphereChainLength, sphere.xCoord, sphere.yCoord,
					sphere.zCoord, sphere.getDiameter()));
			chain.add(sphere);

			heightOffset = Math.max(heightOffset, (int) sphere.getRadius() + 1);

			previousSphere = sphere;
			buildLength++;
		}

		sphereChainLength = chain.size();

		if (DeveloperOptions.RENDER_ABOVE_GROUND) {
			heightOffset += 96;
		}

	}

	private int getSphereScore(SphereInstance potentialSphere) {
		double x = potentialSphere.x;
		double y = potentialSphere.y;
		double z = potentialSphere.z;
		int count = 1;

		for (SphereInstance sphere : chain) {
			x += sphere.x;
			y += sphere.y;
			z += sphere.z;
			count++;
		}

		double offsetX = x / count;
		double offsetY = y / count;
		double offsetZ = z / count;

		double distance = -Math.sqrt(Math.pow(offsetX, 2) + Math.pow(offsetY, 2) + Math.pow(offsetZ, 2));

		return (int) distance;
	}

	private boolean isValid(SphereInstance sphere) {
		for (SphereInstance existingSphere : chain) {
			double checkDistance = sphere.distance(existingSphere);
			/*
			 * double checkDistance = Math.pow(sphere.x - existingSphere.x, 2) +
			 * Math.pow(sphere.y - existingSphere.y, 2) + Math.pow(sphere.z -
			 * existingSphere.z, 2);
			 */
			double minimumDistance = Math.pow(sphere.getRadius() + existingSphere.getRadius(), 2);
			if (checkDistance < minimumDistance)
				return false;
		}

		return true;
	}

	private SphereInstance getPotentialSphere(SphereInstance previousSphere) {
		final int minCoridorSpacing = 6;
		int originX;
		int originZ;

		int diameter = random.nextInt(16) + 10;
		diameter = diameter + diameter % 2;
		final double radius = diameter / 2.0f;

		final int xDirection = random.nextBoolean() ? -1 : 1;
		final int zDirection = random.nextBoolean() ? -1 : 1;
		final boolean firstDirectionIsXAxis = random.nextBoolean();
		final double firstDimensionOffset = radius + random.nextInt(12);
		final int newSpacing = minCoridorSpacing + random.nextInt(8);

		final int previousSphereDiameter = previousSphere != null ? previousSphere.getDiameter() : 0;
		final int previousSphereX = previousSphere != null ? previousSphere.xCoord : 0;
		final int previousSphereZ = previousSphere != null ? previousSphere.zCoord : 0;

		final double touchingDistance = previousSphereDiameter / 2.0f + radius;
		// FIXME: Do I even need to do this? Corridors aren't created here
		// anymore.
		if (firstDirectionIsXAxis) {
			originZ = (int) (previousSphereZ + (firstDimensionOffset + minCoridorSpacing) * zDirection);
			originX = (int) (previousSphereX + (minCoridorSpacing
					+ Math.sqrt(Math.pow(Math.max(touchingDistance, firstDimensionOffset + 1), 2) - Math.pow(firstDimensionOffset, 2)) + newSpacing)
					* xDirection);
			LOG.finer(String.format("originX: %d, prevX: %d, touch: %f, offset: %f, spacing: %d", originX, previousSphereX,
					touchingDistance, firstDimensionOffset, newSpacing));
		} else {
			originX = (int) (previousSphereX + (firstDimensionOffset + minCoridorSpacing) * xDirection);
			originZ = (int) (previousSphereZ + (minCoridorSpacing
					+ Math.sqrt(Math.pow(Math.max(touchingDistance, firstDimensionOffset + 1), 2) - Math.pow(firstDimensionOffset, 2)) + newSpacing)
					* zDirection);
			LOG.finer(String.format("originZ: %d, prevZ: %d, touch: %f, offset: %f, spacing: %d", originZ, previousSphereZ,
					touchingDistance, firstDimensionOffset, newSpacing));
		}

		LOG.info(String.format("Sphere @ (%d,%d,%d) d:%d", originX, 0, originZ, diameter));
		SphereInstance sphere = new SphereInstance(new Point3D(originX, 0, originZ), diameter);
		return sphere;
	}

	public List<SphereInstance> getChain() {
		return chain;
	}

	public Set<Point3D> getRequiredChunks() {
		HashSet<Point3D> requiredChunks = new HashSet<Point3D>();

		for (SphereInstance sphere : chain) {
			float radius = sphere.getDiameter() / 2;
			int minChunkX = (int) (sphere.x + startX - radius) % 16;
			int maxChunkX = (int) (sphere.x + startX + radius) % 16;
			int minChunkZ = (int) (sphere.z + startZ - radius) % 16;
			int maxChunkZ = (int) (sphere.z + startZ + radius) % 16;

			for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
				for (int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
					requiredChunks.add(new Point3D(chunkX, 0, chunkZ));
				}
			}
		}
		return requiredChunks;
	}

	public void renderSpheres(World world) {
		int chainLength = chain.size();
		int current = 1;
		for (SphereInstance sphere : chain) {
			LOG.info(String.format("Rendering Sphere %d/%d at (%d,%d,%d) diameter %d", current++, chainLength, sphere.xCoord + startX,
					heightOffset, sphere.zCoord + startZ, sphere.getDiameter()));
			sphere.render(world, Vec3.createVectorHelper(startX, heightOffset, startZ));
		}
	}
}

package net.binaryvibrance.undergrounddomes.generation2;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation2.model.Dome;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;


public class GenADomeGenerator implements IDomeGenerator {
	
	private static final Logger LOG = LogHelper.getLogger();
	private Random random;

	@Override
	public void Generate() {
		List<Dome> domes;
		domes = createDomeChain();
	}

	@Override
	public void SetRandom(Random random) {
		this.random = random;
	}
	
	private List<Dome> createDomeChain() {

		//Create up to 16 domes.
		int domeChainLength = random.nextInt(12) + 4;
		LinkedList<Dome> chain = new LinkedList<Dome>();
		int buildLength = 0;

		Dome previousDome = null;
		while (buildLength < domeChainLength) {
			Dome dome = null;
			int domeScore = Integer.MIN_VALUE;
			for (int i = 0; i < 10; i++) {
				Dome potentialDome = getPotentialDome(previousDome);
				if (!isValid(potentialDome, chain)) {
					continue;
				}
				int potentialDomeScore = getDomeScore(potentialDome, chain);
				if (potentialDomeScore > domeScore) {
					dome = potentialDome;
					domeScore = potentialDomeScore;
				}
			}
			if (dome == null) {
				break;
			}
			dome.createFloors(random);
			LOG.info(String.format("Dome %d/%d @ (%d,%d,%d) d:%d", buildLength + 1, domeChainLength, dome.xCoord, dome.yCoord,
					dome.zCoord, dome.getDiameter()));
			chain.add(dome);

			//heightOffset = Math.max(heightOffset, (int) dome.getRadius() + 1);

			previousDome = dome;
			buildLength++;
		}

		domeChainLength = chain.size();	
	}
	
	private int getDomeScore(Dome potentialDome, List<Dome> chain) {
		double x = potentialDome.x;
		double y = potentialDome.y;
		double z = potentialDome.z;
		int count = 1;

		for (Dome dome : chain) {
			x += dome.x;
			y += dome.y;
			z += dome.z;
			count++;
		}

		double offsetX = x / count;
		double offsetY = y / count;
		double offsetZ = z / count;

		double distance = -Math.sqrt(Math.pow(offsetX, 2) + Math.pow(offsetY, 2) + Math.pow(offsetZ, 2));

		return (int) distance;
	}

	private boolean isValid(Dome dome, List<Dome> chain) {
		for (Dome existingDome : chain) {
			double checkDistance = dome.distance(existingDome);
			/*
			 * double checkDistance = Math.pow(dome.x - existingDome.x, 2) +
			 * Math.pow(dome.y - existingDome.y, 2) + Math.pow(dome.z -
			 * existingDome.z, 2);
			 */
			double minimumDistance = Math.pow(dome.getRadius() + existingDome.getRadius(), 2);
			if (checkDistance < minimumDistance)
				return false;
		}

		return true;
	}

	private Dome getPotentialDome(Dome previousDome) {
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

		final int previousDomeDiameter = previousDome != null ? previousDome.getDiameter() : 0;
		final int previousDomeX = previousDome != null ? previousDome.xCoord : 0;
		final int previousDomeZ = previousDome != null ? previousDome.zCoord : 0;

		final double touchingDistance = previousDomeDiameter / 2.0f + radius;
		// FIXME: Do I even need to do this? Corridors aren't created here
		// anymore.
		if (firstDirectionIsXAxis) {
			originZ = (int) (previousDomeZ + (firstDimensionOffset + minCoridorSpacing) * zDirection);
			originX = (int) (previousDomeX + (minCoridorSpacing
					+ Math.sqrt(Math.pow(Math.max(touchingDistance, firstDimensionOffset + 1), 2) - Math.pow(firstDimensionOffset, 2)) + newSpacing)
					* xDirection);
			LOG.finer(String.format("originX: %d, prevX: %d, touch: %f, offset: %f, spacing: %d", originX, previousDomeX,
					touchingDistance, firstDimensionOffset, newSpacing));
		} else {
			originX = (int) (previousDomeX + (firstDimensionOffset + minCoridorSpacing) * xDirection);
			originZ = (int) (previousDomeZ + (minCoridorSpacing
					+ Math.sqrt(Math.pow(Math.max(touchingDistance, firstDimensionOffset + 1), 2) - Math.pow(firstDimensionOffset, 2)) + newSpacing)
					* zDirection);
			LOG.finer(String.format("originZ: %d, prevZ: %d, touch: %f, offset: %f, spacing: %d", originZ, previousDomeZ,
					touchingDistance, firstDimensionOffset, newSpacing));
		}

		LOG.info(String.format("Dome @ (%d,%d,%d) d:%d", originX, 0, originZ, diameter));
		Dome dome = new Dome(new Point3D(originX, 0, originZ), diameter);
		return dome;
	}


}

package net.binaryvibrance.undergrounddomes.generation;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import net.binaryvibrance.undergrounddomes.generation.maths.Line;
import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.maths.Vector3;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CorridorGen {
	private static final Logger LOG = LogHelper.getLogger();
	private final SphereChain sphereChain;

	List<Line> corridorPaths = new LinkedList<Line>();

	public CorridorGen(SphereChain sphereChain) {
		this.sphereChain = sphereChain;
	}

	public void generateCorridor() {
		int currentSphere = 1;
		int sphereCount = sphereChain.sphereChainLength;
		for (SphereInstance sphere : sphereChain.getChain()) {
			LOG.info(String.format("Creating corridors for sphere %d/%d", currentSphere++, sphereCount));
			// Calculate Nearest Neighbours
			SphereNearestNeighbour snn = new SphereNearestNeighbour(sphere);
			for (SphereInstance neighbourSphere : sphereChain.getChain()) {
				snn.addNeighbour(neighbourSphere);
			}

			// Build corridor between nearest triad

			SphereInstance primary = null;
			for (SphereInstance secondary : snn) {
				if (primary == null) {
					primary = secondary;
					continue;
				}

				boolean valid = true;
				List<Line> allPaths = new LinkedList<Line>();
				Point3D averagePoint = Point3D.average(sphere, primary, secondary);
				Thingo currentPaths = generatePaths(sphere, averagePoint);
				allPaths.add(currentPaths.a);
				allPaths.add(currentPaths.b);
				Thingo primaryPaths = generatePaths(primary, averagePoint);
				allPaths.add(primaryPaths.a);
				allPaths.add(primaryPaths.b);
				Thingo secondaryPaths = generatePaths(secondary, averagePoint);
				allPaths.add(secondaryPaths.a);
				allPaths.add(secondaryPaths.b);

				// Check path validity
				for (Line l : allPaths) {
					if (sphere.intersectsLine(l)) {
						LOG.info(String.format("Current %s intersects %s", l, sphere));
						valid = false;
						break;
					}
					if (primary.intersectsLine(l)) {
						LOG.info(String.format("Primary %s intersects %s", l, sphere));
						valid = false;
						break;
					}
					if (secondary.intersectsLine(l)) {
						LOG.info(String.format("Secondary %s intersects %s", l, sphere));
						valid = false;
						break;
					}
				}

				if (valid) {
					corridorPaths.addAll(allPaths);
					break;
				}
			}
		}
	}

	private Thingo generatePaths(SphereInstance sphere, Point3D averagePoint) {
		SphereFloor baseFloor = sphere.getFloor(0);
		Point3D northPoint = baseFloor.getEntrance(EnumFacing.NORTH).location;
		Point3D southPoint = baseFloor.getEntrance(EnumFacing.SOUTH).location;
		Point3D eastPoint = baseFloor.getEntrance(EnumFacing.EAST).location;
		Point3D westPoint = baseFloor.getEntrance(EnumFacing.WEST).location;
		double northDistance = averagePoint.distance(northPoint);
		double southDistance = averagePoint.distance(southPoint);
		double eastDistance = averagePoint.distance(eastPoint);
		double westDistance = averagePoint.distance(westPoint);

		Line a = null;
		Line b = null;
		Point3D join;
		Vec3 adjustmentVector = null;
		if (northDistance <= southDistance && northDistance <= eastDistance && northDistance <= westDistance) {
			join = new Point3D(northPoint.x, 0, averagePoint.z);
			a = new Line(northPoint, join);
			adjustmentVector = Vector3.SOUTH;
		} else if (southDistance <= northDistance && southDistance <= eastDistance && southDistance <= westDistance) {
			join = new Point3D(southPoint.x, 0, averagePoint.z);
			a = new Line(southPoint, join);
			adjustmentVector = Vector3.NORTH;
		} else if (eastDistance <= northDistance && eastDistance <= southDistance && eastDistance <= westDistance) {
			join = new Point3D(averagePoint.x, 0, eastPoint.z);
			a = new Line(eastPoint, join);
			adjustmentVector = Vector3.WEST;
		} else {
			join = new Point3D(averagePoint.x, 0, westPoint.z);
			a = new Line(westPoint, join);
			adjustmentVector = Vector3.EAST;
		}
		b = new Line(join, averagePoint);

		/*
		 * List<Line> corridorLines = new ArrayList<Line>();
		 * corridorLines.add(a); corridorLines.add(b);
		 */
		return new Thingo(a, b, adjustmentVector);
	}

	public void renderCorridor(World world) {
		int offsetX = sphereChain.startX;
		int offsetZ = sphereChain.startZ;
		int offsetY = sphereChain.heightOffset;

		int currentLine = 1;
		int maxLines = corridorPaths.size();
		for (Line path : corridorPaths) {
			LOG.info(String.format("Creating Line %d/%d %s", currentLine++, maxLines, path.toString()));
			Vec3 vector = path.getRenderVector();
			Point3D currentPoint = path.start;
			do {
				world.setBlock(currentPoint.xCoord + offsetX, currentPoint.yCoord + offsetY, currentPoint.zCoord + offsetZ,
						Block.blockGold.blockID, 0, 0);
				currentPoint = currentPoint.add(vector);
			} while (!currentPoint.equals(path.end));
			world.setBlock(currentPoint.xCoord + offsetX, currentPoint.yCoord + offsetY, currentPoint.zCoord + offsetZ,
					Block.blockGold.blockID, 0, 0);
		}
	}

	private class Thingo {
		public final Line a;
		public final Line b;
		public final Object entranceLocation;
		public final Point3D destinationLocation;

		public Thingo(Line a, Line b, Vec3 entranceAdjustment) {
			this.a = a;
			this.b = b;
			this.entranceLocation = a.start.add(entranceAdjustment);
			this.destinationLocation = b.end;

		}
	}

}

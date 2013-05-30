package net.binaryvibrance.undergrounddomes.generation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import net.binaryvibrance.undergrounddomes.generation.maths.Line;
import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
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

				Point3D averagePoint = Point3D.average(sphere, primary, secondary);
				List<Line> paths = generatePaths(sphere, averagePoint);
				corridorPaths.addAll(paths);

				paths = generatePaths(primary, averagePoint);
				corridorPaths.addAll(paths);

				paths = generatePaths(secondary, averagePoint);
				corridorPaths.addAll(paths);

				// Check path validity

				if (valid) {
					break;
				}
			}
		}
	}

	private List<Line> generatePaths(SphereInstance sphere, Point3D averagePoint) {
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
		if (northDistance <= southDistance && northDistance <= eastDistance && northDistance <= westDistance) {
			join = new Point3D(northPoint.x, 0, averagePoint.z);
			a = new Line(northPoint, join);
		} else if (southDistance <= northDistance && southDistance <= eastDistance && southDistance <= westDistance) {
			join = new Point3D(southPoint.x, 0, averagePoint.z);
			a = new Line(southPoint, join);
		} else if (eastDistance <= northDistance && eastDistance <= southDistance && eastDistance <= westDistance) {
			join = new Point3D(averagePoint.x, 0, eastPoint.z);
			a = new Line(eastPoint, join);
		} else {
			join = new Point3D(averagePoint.x, 0, westPoint.z);
			a = new Line(westPoint, join);
		}
		b = new Line(join, averagePoint);

		List<Line> corridorLines = new ArrayList<Line>();
		corridorLines.add(a);
		corridorLines.add(b);
		return corridorLines;
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

}

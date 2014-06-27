package net.binaryvibrance.undergrounddomes.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import cpw.mods.fml.common.registry.GameData;
import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.helpers.maths.Vector3;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CorridorGen {
	private final SphereChain sphereChain;

	List<Line> corridorPaths = new LinkedList<Line>();

	public CorridorGen(SphereChain sphereChain) {
		this.sphereChain = sphereChain;
	}

	public void generateCorridor() {
		int currentSphere = 1;
		int sphereCount = sphereChain.sphereChainLength;

		List<SphereInstance> sphereChain = this.sphereChain.getChain();

		for (SphereInstance sphere : sphereChain) {
			LogHelper.info(String.format("Creating corridors for sphere %d/%d", currentSphere++, sphereCount));
			// Calculate Nearest Neighbours
			SphereNearestNeighbour snn = new SphereNearestNeighbour(sphere);
			for (SphereInstance neighbourSphere : sphereChain) {
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

				EntranceToCorridor sphereCorridorEntrance = generateCorridorEntrance(sphere, averagePoint);
				EntranceToCorridor primaryCorridorEntrance = generateCorridorEntrance(primary, averagePoint);
				EntranceToCorridor secondaryCorridorEntrance = generateCorridorEntrance(secondary, averagePoint);
				List<EntranceToCorridor> entrances = new ArrayList<EntranceToCorridor>(Arrays.asList(new EntranceToCorridor[] {
						sphereCorridorEntrance, primaryCorridorEntrance, secondaryCorridorEntrance }));

				int appliedEntrances = 0;
				Point3D replacementJoin = null;
				for (EntranceToCorridor entrance : entrances) {
					if (entrance.isApplied()) {
						++appliedEntrances;
						if (replacementJoin == null) {
							replacementJoin = entrance.lineToOrigin.end;
						}
					}
				}

				for (EntranceToCorridor entrance : entrances) {
					if (entrance.isApplied())
						continue;
					if (appliedEntrances == 2 && !entrance.isApplied()) {
						entrance.setNewEndpoint(replacementJoin);
					}
					for (SphereInstance compareSphere : sphereChain) {
						if (compareSphere.intersectsLine(entrance.lineToCorridor)) {
							LogHelper.info(String.format("Corridor %s intersects with sphere %s", entrance.lineToCorridor, compareSphere));
							valid = false;
							break;
						}
						if (compareSphere.intersectsLine(entrance.lineToOrigin)) {
							LogHelper.info(String.format("Corridor %s intersects with sphere %s", entrance.lineToOrigin, compareSphere));
							valid = false;
							break;
						}
					}
					if (!valid) {
						break;
					}

					allPaths.add(entrance.lineToCorridor);
					allPaths.add(entrance.lineToOrigin);
					entrance.markApplied();
				}

				if (valid) {
					corridorPaths.addAll(allPaths);
					break;
				}
			}
		}
	}

	private EntranceToCorridor generateCorridorEntrance(SphereInstance sphere, Point3D averagePoint) {
		SphereFloor baseFloor = sphere.getFloor(0);
		SphereEntrance northPoint = baseFloor.getEntrance(EnumFacing.NORTH);
		SphereEntrance southPoint = baseFloor.getEntrance(EnumFacing.SOUTH);
		SphereEntrance eastPoint = baseFloor.getEntrance(EnumFacing.EAST);
		SphereEntrance westPoint = baseFloor.getEntrance(EnumFacing.WEST);

		double northDistance = averagePoint.distance(northPoint.location);
		double southDistance = averagePoint.distance(southPoint.location);
		double eastDistance = averagePoint.distance(eastPoint.location);
		double westDistance = averagePoint.distance(westPoint.location);

		EntranceToCorridor etc = new EntranceToCorridor();
		etc.lineToOrigin.end.set(averagePoint);

		if (northDistance <= southDistance && northDistance <= eastDistance && northDistance <= westDistance) {
			if (northPoint.corridorPath != null) {
				return northPoint.corridorPath;
			}
			etc.setEntrance(northPoint);
			etc.lineToCorridor.end.set(northPoint.location.x, 0, averagePoint.z);
			etc.lineToCorridor.start.set(northPoint.location);
			etc.setAdjustmentVector(Vector3.SOUTH);
		} else if (southDistance <= northDistance && southDistance <= eastDistance && southDistance <= westDistance) {
			if (southPoint.corridorPath != null) {
				return southPoint.corridorPath;
			}
			etc.setEntrance(southPoint);
			etc.lineToCorridor.end.set(southPoint.location.x, 0, averagePoint.z);
			etc.lineToCorridor.start.set(southPoint.location);
			etc.setAdjustmentVector(Vector3.NORTH);
		} else if (eastDistance <= northDistance && eastDistance <= southDistance && eastDistance <= westDistance) {
			if (eastPoint.corridorPath != null) {
				return eastPoint.corridorPath;
			}
			etc.setEntrance(eastPoint);
			etc.lineToCorridor.end.set(averagePoint.x, 0, eastPoint.location.z);
			etc.lineToCorridor.start.set(eastPoint.location);
			etc.setAdjustmentVector(Vector3.WEST);
		} else {
			if (westPoint.corridorPath != null) {
				return westPoint.corridorPath;
			}
			etc.setEntrance(westPoint);
			etc.lineToCorridor.end.set(averagePoint.x, 0, westPoint.location.z);
			etc.lineToCorridor.start.set(westPoint.location);
			etc.setAdjustmentVector(Vector3.EAST);
		}

		return etc;
	}

	public void renderCorridor(World world) {
		int offsetX = sphereChain.startX;
		int offsetZ = sphereChain.startZ;
		int offsetY = sphereChain.heightOffset;

		int currentLine = 1;
		int maxLines = corridorPaths.size();

        Block goldBlock = GameData.getBlockRegistry().getObject("gold_block");

		for (Line path : corridorPaths) {
			LogHelper.info(String.format("Creating Line %d/%d %s", currentLine++, maxLines, path.toString()));
			Vec3 vector = path.getRenderVector();
			Point3D currentPoint = path.start;
			do {
				world.setBlock(currentPoint.xCoord + offsetX, currentPoint.yCoord + offsetY, currentPoint.zCoord + offsetZ,
                        goldBlock, 0, 0);
				currentPoint = currentPoint.add(vector);
			} while (!currentPoint.equals(path.end));
			world.setBlock(currentPoint.xCoord + offsetX, currentPoint.yCoord + offsetY, currentPoint.zCoord + offsetZ,
                    goldBlock, 0, 0);
		}
	}
}

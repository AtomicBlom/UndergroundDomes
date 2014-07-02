package net.binaryvibrance.undergrounddomes.generation.v1;

import java.util.*;

import net.binaryvibrance.helpers.KeyValuePair;
import net.binaryvibrance.helpers.maths.GeometryHelper;
import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.contracts.ICorridorGenerator;
import net.binaryvibrance.undergrounddomes.generation.contracts.ILineIntersectable;
import net.binaryvibrance.undergrounddomes.generation.model.CompassDirection;
import net.binaryvibrance.undergrounddomes.generation.model.Corridor;
import net.binaryvibrance.undergrounddomes.generation.model.CorridorTerminus;
import net.binaryvibrance.undergrounddomes.generation.model.Dome;
import net.binaryvibrance.undergrounddomes.generation.model.DomeEntrance;
import net.binaryvibrance.undergrounddomes.generation.model.DomeFloor;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;

public class CorridorGenerator implements ICorridorGenerator {

	@Override
	public List<Corridor> generate(List<Dome> domes) {
		int currentDome = 1;
		// List<Dome> domeChain = this.domeChain.getChain();
		int domeCount = domes.size();

		List<Corridor> validCorridors = new LinkedList<Corridor>();

		//This particular implementation of the corridor generator loops over each dome
		//for each dome, it attempts to build 3 corridors between it's 2 closest neighbours.
		//The corridors are not allowed to pass through another dome.

		//TODO: If a corridor passes through another corridor, join them together

		for (Dome dome : domes) {
			LogHelper.info("Creating corridors for dome %d/%d %s", currentDome++, domeCount, dome);
			// Calculate Nearest Neighbours
			DomeNearestNeighbour snn = new DomeNearestNeighbour(dome);
			for (Dome neighbourDome : domes) {
				snn.addNeighbour(neighbourDome);
			}

			// Build corridor between nearest triad
			Dome primary = null;
			for (Dome secondary : snn) {
				LogHelper.info("Secondary Dome " + secondary);
				if (primary == null) {
					LogHelper.info("... is actually the primary.");
					primary = secondary;
					continue;
				}

				Point3D averagePoint = Point3D.average(dome.getLocation(), primary.getLocation(), secondary.getLocation());

				DomeEntrance domeCorridorEntrance = getClosestCorridorEntrance(dome, averagePoint);
				DomeEntrance primaryCorridorEntrance = getClosestCorridorEntrance(primary, averagePoint);
				DomeEntrance secondaryCorridorEntrance = getClosestCorridorEntrance(secondary, averagePoint);

				List<DomeEntrance> entrances = new ArrayList<DomeEntrance>(Arrays.asList(new DomeEntrance[] { domeCorridorEntrance, primaryCorridorEntrance,
						secondaryCorridorEntrance }));

				CorridorTerminus centrePointTerminus = new CorridorTerminus();
				centrePointTerminus.setLocation(averagePoint);

				//Step 1, would corridors intersect other spheres?
				List<KeyValuePair<DomeEntrance, CorridorTerminus>> entriesToCreate = new ArrayList<KeyValuePair<DomeEntrance, CorridorTerminus>>();
				boolean valid = true;

				List<Dome> obstacles = new LinkedList<Dome>();
				for (Dome d : domes) {
					//if (d != dome && d != primary && d != secondary) {
						obstacles.add(d);
					//}
				}

				List<Corridor> potentialValidCorridors = new LinkedList<Corridor>();
				for (DomeEntrance entrance : entrances) {

					CorridorTerminus entranceTerminus = new CorridorTerminus();
					entranceTerminus.setLocation(entrance.getLocation());
					Corridor corridor = new Corridor(entranceTerminus, centrePointTerminus, entrance.getCompassDirection());
					ILineIntersectable obstacle = corridor.getFirstIntersectingObstacle(obstacles);
					if (obstacle != null) {
						valid = false;
						LogHelper.info("%s intersects with obstacle %s", corridor, obstacle);
						break;
					} else {
						potentialValidCorridors.add(corridor);
					}

					/*var validCorridor = Corridor.tryCreateBetween(entranceTerminus, centrePointTerminus, entrance.getCompassDirection(), domes);

					if (validCorridor != null) {
						entriesToCreate.add(new KeyValuePair<DomeEntrance, CorridorTerminus>(entrance, entranceTerminus));
					} else {
						valid = false;
						break;
					}*/
				}

				if (valid) {
					//Step 2: If we can, then Check each corridor to see if it should be attached to an existing corridor.
					LogHelper.info("Found a non-colliding corridor");
					for (KeyValuePair<DomeEntrance, CorridorTerminus> kvp : entriesToCreate) {
						kvp.key.setTerminus(kvp.value);
					}

					List<CorridorLinePair> allValidLines = new LinkedList<CorridorLinePair>();
					for (Corridor corridor : validCorridors) {
						for (Line line : corridor.getAllLines()) {
							allValidLines.add(new CorridorLinePair(line, corridor));
						}
					}

					for (Corridor corridor : potentialValidCorridors) {
						for (Line line : corridor.getAllLines()) {
							Point3D usedIntersection = null;
							CorridorLinePair usedCorridorLinePair = null;
							double distanceCheck = Double.MAX_VALUE;
							boolean intersected = false;

							for (CorridorLinePair validLine : allValidLines) {
								Point3D intersection = GeometryHelper.getLineIntersectionXZ(line, validLine.getLine());
								if (intersection != null) {
									double distance = corridor.getStart().getLocation().distance(intersection);
									if (distance < distanceCheck) {
										usedCorridorLinePair = validLine;
										usedIntersection = intersection;
										distanceCheck = distance;
										intersected = true;
									}
								}
							}

							if (intersected) {
								//If we've found an intersection, update the first line matched and break the rest.
								corridor.IntersectWith(usedCorridorLinePair.getCorridor(), usedIntersection);
								break;
							}
						}
					}

					validCorridors.addAll(potentialValidCorridors);
					break;
				}
			}
		}

		return validCorridors;
	}

	@Override
	public void setRandom(Random random) {

	}

	/*private void on3AppliedEntrances(List<DomeEntrance> entrances, List<Dome> domes, Point3D averagePoint) {
		// TODO Auto-generated method stub

	}

	private void on2AppliedEntrances(List<DomeEntrance> entrances, List<Dome> domes, Point3D averagePoint) {
		// TODO Auto-generated method stub

	}

	private void on1AppliedEntrance(List<DomeEntrance> entrances, List<Dome> domes, Point3D averagePoint) {
		DomeEntrance existingEntrance;

        DomeEntrance firstEntrance = null;
        DomeEntrance secondEntrance = null;

		for (DomeEntrance entrance : entrances) {
			if (entrance.isInUse()) {
				existingEntrance = entrance;
				continue;
			}
			if (!entrance.isInUse() && firstEntrance == null) {
				firstEntrance = entrance;
				continue;
			}
            if (!entrance.isInUse() && firstEntrance != null) {
                secondEntrance = entrance;
            }
		}
	}

	// Create a brand new corridor
	private void on0AppliedEntrances(List<DomeEntrance> entrances, List<Dome> domes, Point3D averagePoint) {
		CorridorTerminus centrePointTerminus = new CorridorTerminus();
		centrePointTerminus.setLocation(averagePoint);

		List<KeyValuePair<DomeEntrance, CorridorTerminus>> entriesToCreate = new ArrayList<KeyValuePair<DomeEntrance, CorridorTerminus>>();
		boolean valid = true;
		for (DomeEntrance entrance : entrances) {
			CorridorTerminus entranceTerminus = new CorridorTerminus();
			if (!Corridor.tryCreateBetween(entranceTerminus, centrePointTerminus, entrance.getCompassDirection(), domes)) {
				entriesToCreate.add(new KeyValuePair<DomeEntrance, CorridorTerminus>(entrance, entranceTerminus));
			} else {
				valid = false;
				break;
			}
		}

		if (valid) {
			for (KeyValuePair<DomeEntrance, CorridorTerminus> kvp : entriesToCreate) {
				kvp.key.setTerminus(kvp.value);
			}
		}
	}*/

	private DomeEntrance getClosestCorridorEntrance(Dome sphere, Point3D averagePoint) {
		DomeFloor baseFloor = sphere.getFloor(0);
		DomeEntrance northPointEntrance = baseFloor.getEntrance(CompassDirection.NORTH);
		DomeEntrance southPointEntrance = baseFloor.getEntrance(CompassDirection.SOUTH);
		DomeEntrance eastPointEntrance = baseFloor.getEntrance(CompassDirection.EAST);
		DomeEntrance westPointEntrance = baseFloor.getEntrance(CompassDirection.WEST);

		Point3D northPointLocation = northPointEntrance.getLocation();
		Point3D southPointLocation = southPointEntrance.getLocation();
		Point3D eastPointLocation = eastPointEntrance.getLocation();
		Point3D westPointLocation = westPointEntrance.getLocation();

		double northDistance = averagePoint.distance(northPointLocation);
		double southDistance = averagePoint.distance(southPointLocation);
		double eastDistance = averagePoint.distance(eastPointLocation);
		double westDistance = averagePoint.distance(westPointLocation);

		if (northDistance <= southDistance && northDistance <= eastDistance && northDistance <= westDistance) {
			return northPointEntrance;
		} else if (southDistance <= northDistance && southDistance <= eastDistance && southDistance <= westDistance) {
			return southPointEntrance;
		} else if (eastDistance <= northDistance && eastDistance <= southDistance && eastDistance <= westDistance) {
			return eastPointEntrance;
		} else {
			return westPointEntrance;
		}
	}
	
	class DomeNearestNeighbour implements Iterable<Dome> {
		private final Dome dome;
		private final PriorityQueue<KeyValuePair<Dome, Double>> distances;

		private final Comparator<KeyValuePair<Dome, Double>> comparer = new Comparator<KeyValuePair<Dome, Double>>() {
			@Override
			public int compare(KeyValuePair<Dome, Double> o1, KeyValuePair<Dome, Double> o2) {
				return Double.compare(o1.value, o2.value);
			}
		};

		public DomeNearestNeighbour(Dome dome) {
			this.dome = dome;
			distances = new PriorityQueue<KeyValuePair<Dome, Double>>(16, comparer);
		}

		public void addNeighbour(Dome otherDome) {
			if (otherDome == dome)
				return;

			Point3D domeLocation = dome.getLocation();
			Point3D otherDomeLocation = otherDome.getLocation();

			distances.add(new KeyValuePair<Dome, Double>(otherDome, domeLocation.distance(otherDomeLocation)));
		}

		@Override
		public Iterator<Dome> iterator() {
			Iterator<Dome> iterator = new Iterator<Dome>() {
				private final Iterator<KeyValuePair<Dome, Double>> internalIterator = distances.iterator();

				@Override
				public boolean hasNext() {
					return internalIterator.hasNext();
				}

				@Override
				public Dome next() {
					KeyValuePair<Dome, Double> next = internalIterator.next();
					return next.key;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException("Cannot remove elements");
				}

			};

			return iterator;
		}

	}

	private class CorridorLinePair {
		private final Line line;
		private final Corridor corridor;

		public CorridorLinePair(Line line, Corridor corridor) {

			this.line = line;
			this.corridor = corridor;
		}

		public Line getLine() {
			return line;
		}

		public Corridor getCorridor() {
			return corridor;
		}
	}
}

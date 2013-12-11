package net.binaryvibrance.undergrounddomes.generation2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Logger;

import net.binaryvibrance.helpers.KeyValuePair;
import net.binaryvibrance.helpers.maths.GeometryHelper;
import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.helpers.maths.Vector3;
import net.binaryvibrance.undergrounddomes.generation2.contracts.ICorridorGenerator;
import net.binaryvibrance.undergrounddomes.generation2.model.CompassDirection;
import net.binaryvibrance.undergrounddomes.generation2.model.Corridor;
import net.binaryvibrance.undergrounddomes.generation2.model.CorridorTerminus;
import net.binaryvibrance.undergrounddomes.generation2.model.Dome;
import net.binaryvibrance.undergrounddomes.generation2.model.DomeEntrance;
import net.binaryvibrance.undergrounddomes.generation2.model.DomeFloor;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;

public class GenACorridorGenerator implements ICorridorGenerator {

	private static final Logger LOG = LogHelper.getLogger();
	
	@Override
	public List<Corridor> generate(List<Dome> domes) {
		int currentDome = 1;
		//List<Dome> domeChain = this.domeChain.getChain();
		int domeCount = domes.size();

		

		for (Dome dome : domes) {
			LOG.info(String.format("Creating corridors for dome %d/%d",
					currentDome++, domeCount));
			// Calculate Nearest Neighbours
			DomeNearestNeighbour snn = new DomeNearestNeighbour(dome);
			for (Dome neighbourDome : domes) {
				snn.addNeighbour(neighbourDome);
			}

			// Build corridor between nearest triad

			Dome primary = null;
			for (Dome secondary : snn) {
				if (primary == null) {
					primary = secondary;
					continue;
				}

				boolean valid = true;
				List<Line> allPaths = new LinkedList<Line>();
				Point3D averagePoint = Point3D.average(
						dome.getLocation(), 
						primary.getLocation(),
						secondary.getLocation());

				DomeEntrance domeCorridorEntrance = getClosestCorridorEntrance(dome, averagePoint);
				DomeEntrance primaryCorridorEntrance = getClosestCorridorEntrance(primary, averagePoint);
				DomeEntrance secondaryCorridorEntrance = getClosestCorridorEntrance(secondary, averagePoint);
				
				List<DomeEntrance> entrances = new ArrayList<DomeEntrance>(
						Arrays.asList(new DomeEntrance[] {
								domeCorridorEntrance,
								primaryCorridorEntrance,
								secondaryCorridorEntrance }));
/*
				int appliedEntrances = 0;
				CorridorTerminus replacementJoin = null;
				for (DomeEntrance entrance : entrances) {
					if (entrance.isInUse()) {
						++appliedEntrances;
						if (replacementJoin == null) {
							//replacementJoin = entrance.getTerminus().getCorridor().;
							//FIXME: set the replacement join to a terminus in the corridor...
						}
					}
				}

				for (DomeEntrance entrance : entrances) {
					if (entrance.isInUse())
						continue;
					if (appliedEntrances == 2 && !entrance.isInUse()) {
						entrance.setNewEndpoint(replacementJoin);
					}
					for (Dome compareDome : domes) {
						if (GeometryHelper.lineIntersectsSphere(entrance.lineToCorridor, compareDome.getLocation(), compareDome.getRadius())) {
							LOG.info(String.format(
									"Corridor %s intersects with dome %s",
									entrance.lineToCorridor, compareDome));
							valid = false;
							break;
						}
						if (GeometryHelper.lineIntersectsSphere(entrance.lineToOrigin, compareDome.getLocation(), compareDome.getRadius())) {
							LOG.info(String.format(
									"Corridor %s intersects with dome %s",
									entrance.lineToOrigin, compareDome));
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
				}*/
			}
		}

		return new ArrayList<Corridor>();
	}

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

		
		
		//DomeEntrance etc = new DomeEntrance();
		/*etc.lineToOrigin.end.set(averagePoint);

		if (northDistance <= southDistance && northDistance <= eastDistance && northDistance <= westDistance) {
			if (northPointEntrance.isInUse()) {
				return northPointEntrance;
			}
			etc.setEntrance(northPointEntrance);
			etc.lineToCorridor.end.set(northPointLocation.x, 0, averagePoint.z);
			etc.lineToCorridor.start.set(northPointLocation);
			etc.setAdjustmentVector(Vector3.SOUTH);
		} else if (southDistance <= northDistance && southDistance <= eastDistance && southDistance <= westDistance) {
			if (southPointEntrance.isInUse()) {
				return southPointEntrance;
			}
			etc.setEntrance(southPointEntrance);
			etc.lineToCorridor.end.set(southPointLocation.x, 0, averagePoint.z);
			etc.lineToCorridor.start.set(southPointLocation);
			etc.setAdjustmentVector(Vector3.NORTH);
		} else if (eastDistance <= northDistance && eastDistance <= southDistance && eastDistance <= westDistance) {
			if (eastPointEntrance.isInUse()) {
				return eastPointEntrance;
			}
			etc.setEntrance(eastPointEntrance);
			etc.lineToCorridor.end.set(averagePoint.x, 0, eastPointLocation.z);
			etc.lineToCorridor.start.set(eastPointLocation);
			etc.setAdjustmentVector(Vector3.WEST);
		} else {
			if (westPointEntrance.isInUse()) {
				return westPointEntrance;
			}
			etc.setEntrance(westPointEntrance);
			etc.lineToCorridor.end.set(averagePoint.x, 0, westPointLocation.z);
			etc.lineToCorridor.start.set(westPointLocation);
			etc.setAdjustmentVector(Vector3.EAST);
		}

		return etc;*/
		return null;
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
}

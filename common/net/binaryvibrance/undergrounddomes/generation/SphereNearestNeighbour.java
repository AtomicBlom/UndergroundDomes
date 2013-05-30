package net.binaryvibrance.undergrounddomes.generation;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import net.binaryvibrance.helpers.KeyValuePair;

class SphereNearestNeighbour implements Iterable<SphereInstance> {
	private final SphereInstance sphere;
	private final PriorityQueue<KeyValuePair<SphereInstance, Double>> distances;

	private final Comparator<KeyValuePair<SphereInstance, Double>> comparer = new Comparator<KeyValuePair<SphereInstance, Double>>() {
		@Override
		public int compare(KeyValuePair<SphereInstance, Double> o1, KeyValuePair<SphereInstance, Double> o2) {
			return Double.compare(o1.value, o2.value);
		}
	};

	public SphereNearestNeighbour(SphereInstance sphere) {
		this.sphere = sphere;
		distances = new PriorityQueue<KeyValuePair<SphereInstance, Double>>(16, comparer);
	}

	public void addNeighbour(SphereInstance otherSphere) {
		if (otherSphere == sphere)
			return;

		double distance = Math.pow(otherSphere.x - sphere.x, 2) + Math.pow(otherSphere.y - sphere.y, 2)
				+ Math.pow(otherSphere.z - sphere.z, 2);
		distances.add(new KeyValuePair<SphereInstance, Double>(otherSphere, distance));
	}

	@Override
	public Iterator<SphereInstance> iterator() {
		Iterator<SphereInstance> iterator = new Iterator<SphereInstance>() {
			private final Iterator<KeyValuePair<SphereInstance, Double>> internalIterator = distances.iterator();

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return internalIterator.hasNext();
			}

			@Override
			public SphereInstance next() {
				// TODO Auto-generated method stub
				KeyValuePair<SphereInstance, Double> next = internalIterator.next();
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
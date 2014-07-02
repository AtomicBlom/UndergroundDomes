package net.binaryvibrance.undergrounddomes.generation.model;

import net.binaryvibrance.helpers.maths.Point3D;

import java.util.ArrayList;
import java.util.List;

public class CorridorTerminus {
    private Point3D location;
    private List<ITerminusSpoke> spokes = new ArrayList<ITerminusSpoke>();

	public CorridorTerminus(Point3D location) {
		this.location = location;
	}

    public Point3D getLocation() {
        return location;
    }

    public void addSpoke(ITerminusSpoke spoke) {
	    spokes.add(spoke);
    }

	public int getSpokeCount() {
		return spokes.size();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CorridorTerminus that = (CorridorTerminus) o;

		if (!location.equals(that.location)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return location.hashCode();
	}

	public List<ITerminusSpoke> getSpokes() {
		return spokes;
	}
}

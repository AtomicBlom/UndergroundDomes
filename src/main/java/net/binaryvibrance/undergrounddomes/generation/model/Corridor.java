package net.binaryvibrance.undergrounddomes.generation.model;

import net.binaryvibrance.helpers.maths.GeometryHelper;
import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.contracts.ILineIntersectable;

import java.util.LinkedList;
import java.util.List;

public class Corridor implements ITerminusSpoke {
    private CorridorTerminus start;
    private CorridorTerminus end;

	public Corridor(CorridorTerminus startTerminus,
                    CorridorTerminus endTerminus) {

	    start = startTerminus;
		startTerminus.addSpoke(this);
	    end = endTerminus;
		endTerminus.addSpoke(this);
    }

	@Override
	public String toString() {
		return String.format("Corridor (%s -> %s)", getStart().getLocation(), getEnd().getLocation() );
	}

	public CorridorTerminus getStart() {
		return start;
	}

	public CorridorTerminus getEnd() {
		return end;
	}

	public void setEnd(CorridorTerminus end) {
		this.end = end;
	}
}

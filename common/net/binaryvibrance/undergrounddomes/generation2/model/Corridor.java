package net.binaryvibrance.undergrounddomes.generation2.model;

import java.util.List;

import net.binaryvibrance.helpers.maths.Point3D;

public class Corridor {
	public Corridor(CorridorTerminus startTerminus, CorridorTerminus endTerminus) {
		start = startTerminus;
		end = endTerminus;
	}
	CorridorTerminus start;
	CorridorTerminus end;
	List<Point3D> intermediatePoints;
}

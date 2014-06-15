package net.binaryvibrance.undergrounddomes.generation2.model;

import java.util.List;
import java.util.logging.Logger;

import net.binaryvibrance.helpers.maths.GeometryHelper;
import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation2.contracts.ILineIntersectable;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;

public class Corridor {
	private static final Logger LOG = LogHelper.getLogger();
	
	protected Corridor() {
	}

	CorridorTerminus start;
	CorridorTerminus end;
	List<Point3D> intermediatePoints;

	public static boolean tryCreateBetween(CorridorTerminus startTerminus,
			CorridorTerminus endTerminus, CompassDirection initialDirection,
			List<? extends ILineIntersectable> obstacles) {
		Corridor newCorridor = new Corridor();
		newCorridor.start = startTerminus;
		newCorridor.end = endTerminus;

		boolean successful = true;

		Point3D midPoint = GeometryHelper.getMidPoint(
				startTerminus.getLocation(), endTerminus.getLocation(),
				initialDirection);
		newCorridor.intermediatePoints.add(midPoint);

		for (ILineIntersectable obstacle : obstacles) {
			Line startLine = new Line(startTerminus.getLocation(), midPoint);
			Line endLine = new Line(endTerminus.getLocation(), midPoint);
			
			if (obstacle.intersects(startLine) || obstacle.intersects(endLine)) {
				LOG.info(String.format("Corridor %s intersects with obstacle %s",
						newCorridor, obstacle));
				successful = false;
				break;
			}
		}
		
		return successful;
	}
}

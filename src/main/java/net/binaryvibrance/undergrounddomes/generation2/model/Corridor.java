package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.helpers.maths.GeometryHelper;
import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation2.contracts.ILineIntersectable;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Corridor {
    private static final Logger LOG = LogHelper.getLogger();
    CorridorTerminus start;
    CorridorTerminus end;
    List<Point3D> intermediatePoints;

	public Corridor(CorridorTerminus startTerminus,
                    CorridorTerminus endTerminus,
                    CompassDirection initialDirection) {

	    start = startTerminus;
	    end = endTerminus;

	    Point3D midPoint = GeometryHelper.getMidPoint(
			    startTerminus.getLocation(), endTerminus.getLocation(),
			    initialDirection);

	    intermediatePoints = new LinkedList<Point3D>();
	    intermediatePoints.add(midPoint);

    }

	public ILineIntersectable getFirstIntersectingObstacle(List<? extends ILineIntersectable> obstacles) {

		List<Line> allPoints = getAllLines();


		for (ILineIntersectable obstacle : obstacles) {
			for (Line line : allPoints) {
				if (obstacle.intersects(line)) {
					LOG.info(String.format("Corridor %s intersects with obstacle %s", this, obstacle));
					return obstacle;
				}
			}
		}

		return null;
	}

	private List<Line> getAllLines() {
		List<Point3D> linePoints = new LinkedList<Point3D>();
		List<Line> lines = new LinkedList<Line>();
		linePoints.addAll(intermediatePoints);
		linePoints.add(end.getLocation());

		Point3D lineStart = start.getLocation();
		for (Point3D lineEnd : linePoints) {
			lines.add(new Line(lineStart, lineEnd));
			lineStart = lineEnd;
		}

		return lines;
	}
}

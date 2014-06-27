package net.binaryvibrance.undergrounddomes.generation.model;

import net.binaryvibrance.helpers.maths.GeometryHelper;
import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.contracts.ILineIntersectable;

import java.util.LinkedList;
import java.util.List;

public class Corridor {
    private CorridorTerminus start;
    private CorridorTerminus end;
    List<Point3D> intermediatePoints;

	public Corridor(CorridorTerminus startTerminus,
                    CorridorTerminus endTerminus,
                    CompassDirection initialDirection) {

	    start = startTerminus;
	    end = endTerminus;

	    Point3D midPoint = GeometryHelper.getMidPoint(
			    startTerminus.getLocation(), endTerminus.getLocation(),
			    initialDirection);

		if (midPoint.xCoord < 0 || midPoint.yCoord < 0 || midPoint.zCoord < 0) {
			midPoint = GeometryHelper.getMidPoint(
					startTerminus.getLocation(), endTerminus.getLocation(),
					initialDirection);
		}

	    intermediatePoints = new LinkedList<Point3D>();
	    intermediatePoints.add(midPoint);

    }

	public ILineIntersectable getFirstIntersectingObstacle(List<? extends ILineIntersectable> obstacles) {

		List<Line> allPoints = getAllLines();


		for (ILineIntersectable obstacle : obstacles) {
			for (Line line : allPoints) {
				if (obstacle.intersects(line)) {
					return obstacle;
				}
			}
		}

		return null;
	}

	public List<Line> getAllLines() {
		List<Point3D> linePoints = new LinkedList<Point3D>();
		List<Line> lines = new LinkedList<Line>();
		linePoints.addAll(intermediatePoints);
		linePoints.add(getEnd().getLocation());

		Point3D lineStart = getStart().getLocation();
		for (Point3D lineEnd : linePoints) {
			lines.add(new Line(lineStart, lineEnd));
			lineStart = lineEnd;
		}

		return lines;
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

	public void IntersectWith(Corridor otherCorridor, Point3D usedIntersection) {
		CorridorTerminus newTerminus = new CorridorTerminus();
		newTerminus.setLocation(usedIntersection);
		newTerminus.addToCorridor(this);
		newTerminus.addToCorridor(otherCorridor);

		//Figure out which points to remove

		Point3D lineStart = getStart().getLocation();
		int removeFrom = 0;
		for (Point3D lineEnd : intermediatePoints) {
			if (!GeometryHelper.IsPointOnLine(lineStart, lineEnd, usedIntersection)) {
				removeFrom++;
			}
			lineStart = lineEnd;
		}
		for (int i = removeFrom; i < intermediatePoints.size(); i++) {
			intermediatePoints.remove(removeFrom);
		}

		end = newTerminus;

	}
}

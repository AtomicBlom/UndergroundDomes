package net.binaryvibrance.undergrounddomes.generation.v1;

import net.binaryvibrance.helpers.maths.GeometryHelper;
import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.contracts.ILineIntersectable;
import net.binaryvibrance.undergrounddomes.generation.model.Corridor;
import net.binaryvibrance.undergrounddomes.generation.model.CorridorTerminus;
import net.binaryvibrance.undergrounddomes.generation.model.Dome;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;

import java.util.List;

/**
 * Created by CodeWarrior on 2/07/2014.
 */
public abstract class CorridorHelper {
	public static ILineIntersectable getFirstIntersectingObstacle(Corridor corridor, List<? extends ILineIntersectable> obstacles) {
		Line line = getLineFromCorridor(corridor);

		for (ILineIntersectable obstacle : obstacles) {
			if (obstacle.intersects(line)) {
				return obstacle;
			}
		}

		return null;
	}

	public static Line getLineFromCorridor(Corridor corridor) {
		return new Line(corridor.getStart().getLocation(), corridor.getEnd().getLocation());
	}

	public static void IntersectWith(Corridor corridorA, Corridor corridorB) {

		Line lineA = getLineFromCorridor(corridorA);
		Line lineB = getLineFromCorridor(corridorB);

		Point3D collisionPoint = GeometryHelper.getLineIntersectionXZ(lineA, lineB);
		CorridorTerminus newTerminus = new CorridorTerminus(collisionPoint);

		Corridor corridorC = new Corridor(corridorA.getEnd(), newTerminus);
		Corridor corridorD = new Corridor(corridorC.getEnd(), newTerminus);
		corridorA.setEnd(newTerminus);
		corridorB.setEnd(newTerminus);

		if (corridorA.getStart().getSpokeCount() >= 1) {
			newTerminus.addSpoke(corridorA);
		}
		if (corridorB.getStart().getSpokeCount() >= 1) {
			newTerminus.addSpoke(corridorB);
		}
		if (corridorC.getStart().getSpokeCount() >= 1) {
			newTerminus.addSpoke(corridorC);
		}
		if (corridorD.getStart().getSpokeCount() >= 1) {
			newTerminus.addSpoke(corridorD);
		}
	}

	public static boolean CollidesWith(Corridor corridor, List<Dome> obstacles) {
		ILineIntersectable obstacle = CorridorHelper.getFirstIntersectingObstacle(corridor, obstacles);
		if (obstacle != null) {
			LogHelper.info("%s intersects with obstacle %s", corridor, obstacle);
			return true;
		}
		return false;

	}

	public static Point3D checkCollision(Corridor corridor, Corridor otherCorridor) {
		Line corridorLine = getLineFromCorridor(corridor);
		Line otherCorridorLine = getLineFromCorridor(otherCorridor);

		return GeometryHelper.getLineIntersectionXZ(corridorLine, otherCorridorLine);
	}
}

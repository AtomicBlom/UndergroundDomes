package net.binaryvibrance.undergrounddomes.generation.v1;

import java.util.List;

import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.contracts.IAtomFieldRenderer;
import net.binaryvibrance.undergrounddomes.generation.model.AtomElement;
import net.binaryvibrance.undergrounddomes.generation.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation.model.Corridor;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.util.Vec3;

public class CorridorRenderer implements IAtomFieldRenderer {

	private List<Corridor> corridors;

	public CorridorRenderer(List<Corridor> corridors) {
		this.corridors = corridors;
	}
	
	@Override
	public void RenderToAtomField(AtomField field) {
		for (Corridor corridor : corridors) {
			LogHelper.info("    Rendering corridor %s", corridor);

			Line line = CorridorHelper.getLineFromCorridor(corridor);
			LogHelper.info("        Rendering line %s", line);
			Vec3 vector = line.getRenderVector();
			Point3D currentPoint = line.start;
			do {
				field.SetAtomAt(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord, AtomElement.CorridorFloor);
				currentPoint = currentPoint.add(vector);
			} while (!currentPoint.equals(line.end));

			field.SetAtomAt(line.start.xCoord, line.start.yCoord, line.start.zCoord, AtomElement.CorridorMidpoint);
			field.SetAtomAt(line.end.xCoord, line.end.yCoord, line.end.zCoord, AtomElement.CorridorMidpoint);

			Point3D startLocation = corridor.getStart().getLocation();
			field.SetAtomAt(startLocation.xCoord, startLocation.yCoord, startLocation.zCoord, AtomElement.CorridorEntrance);
			Point3D endLocation = corridor.getEnd().getLocation();
			field.SetAtomAt(endLocation.xCoord, endLocation.yCoord, endLocation.zCoord, AtomElement.CorridorEntrance);
		}
	}

}

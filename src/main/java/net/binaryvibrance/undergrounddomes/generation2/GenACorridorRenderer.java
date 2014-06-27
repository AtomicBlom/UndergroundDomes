package net.binaryvibrance.undergrounddomes.generation2;

import java.util.List;

import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation2.contracts.IAtomFieldRenderer;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomElement;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation2.model.Corridor;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.util.Vec3;

public class GenACorridorRenderer implements IAtomFieldRenderer {

	@SuppressWarnings("unused")
	private List<Corridor> corridors;

	public GenACorridorRenderer(List<Corridor> corridors) {
		this.corridors = corridors;
	}
	
	@Override
	public void RenderToAtomField(AtomField field) {
		for (Corridor corridor : corridors) {
			LogHelper.info(String.format("    Rendering corridor %s", corridor));
			for (Line line : corridor.getAllLines()) {
				LogHelper.info(String.format("        Rendering line %s", line));
				Vec3 vector = line.getRenderVector();
				Point3D currentPoint = line.start;
				do {
					field.SetAtomAt(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord, AtomElement.CorridorFloor);
					currentPoint = currentPoint.add(vector);
				} while (!currentPoint.equals(line.end));
				field.SetAtomAt(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord, AtomElement.Debug);
			}

			Point3D startLocation = corridor.getStart().getLocation();
			field.SetAtomAt(startLocation.xCoord, startLocation.yCoord, startLocation.zCoord, AtomElement.Debug);
			Point3D endLocation = corridor.getEnd().getLocation();
			field.SetAtomAt(endLocation.xCoord, endLocation.yCoord, endLocation.zCoord, AtomElement.Debug);
		}
	}

}

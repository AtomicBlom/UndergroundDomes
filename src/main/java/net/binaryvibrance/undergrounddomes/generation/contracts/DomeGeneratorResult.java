package net.binaryvibrance.undergrounddomes.generation.contracts;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.model.Dome;

import java.util.List;

/**
 * Created by CodeWarrior on 26/06/2014.
 */
public class DomeGeneratorResult {
	public DomeGeneratorResult(List<Dome> domes, Point3D size) {

		this.domes = domes;
		this.size = size;
	}
	private final List<Dome> domes;
	private final Point3D size;

	public List<Dome> getDomes() {
		return domes;
	}

	public Point3D getSize() {
		return size;
	}
}

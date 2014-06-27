package net.binaryvibrance.undergrounddomes.generation.contracts;

import net.binaryvibrance.helpers.maths.Line;

public interface ILineIntersectable {
	public boolean intersects(Line line);
}

package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.undergrounddomes.generation.maths.Line;
import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.minecraft.util.Vec3;

public class EntranceToCorridor {
	public final Line a;
	public final Line b;
	public final Point3D entranceLocation;
	public final Point3D destinationLocation;
	private boolean applied = false;

	public EntranceToCorridor(Line a, Line b, Vec3 entranceAdjustment) {
		this.a = a;
		this.b = b;
		this.entranceLocation = a.start.add(entranceAdjustment);
		this.destinationLocation = b.end;
	}

	public void markApplied() {
		applied = true;			
	}

	public boolean isApplied() {
		return applied ;
	}
}
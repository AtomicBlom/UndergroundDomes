package net.binaryvibrance.undergrounddomes.generation.model;

import net.binaryvibrance.helpers.maths.Point3D;
import net.minecraft.util.Vec3;

public class DomeEntrance implements ITerminusSpoke {
    private DomeFloor floor;
    private CorridorTerminus exitTerminus;
    private boolean inUse = false;
    private Vec3 locationOffset;
    private CompassDirection compassDirection;

    public DomeEntrance(DomeFloor floor, CompassDirection compassDirection, Vec3 locationOffset) {
        this.floor = floor;
        this.compassDirection = compassDirection;
        this.locationOffset = locationOffset;
    }

    public CorridorTerminus getTerminus() {
        return this.exitTerminus;
    }

    public void setTerminus(CorridorTerminus terminus) {
        this.exitTerminus = terminus;
    }

    public DomeFloor getFloor() {
        return this.floor;
    }

    public Point3D getLocation() {
        return this.getFloor().getDome().getLocation().add(locationOffset);
    }

    public boolean isInUse() {
        return exitTerminus != null;
    }

    public CompassDirection getCompassDirection() {
        return compassDirection;
    }

	@Override
	public String toString() {
		return "DomeEntrance{" +
				"floor=" + floor.getLevel() +
				", location=" + getLocation() +
				", dome=" + floor.getDome().getLocation() +
				", compassDirection=" + compassDirection +
				", inUse=" + inUse +
				'}';
	}
}

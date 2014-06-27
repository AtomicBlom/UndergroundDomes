package net.binaryvibrance.undergrounddomes.generation.model;

import net.binaryvibrance.helpers.maths.Point3D;
import net.minecraft.util.Vec3;

public class DomeEntrance {
    private DomeFloor floor;
    private CorridorTerminus corridorExit;
    private boolean inUse = false;
    private Vec3 locationOffset;
    private CompassDirection compassDirection;

    public DomeEntrance(DomeFloor floor, CompassDirection compassDirection, Vec3 locationOffset) {
        this.floor = floor;
        this.compassDirection = compassDirection;
        this.locationOffset = locationOffset;
    }

    public CorridorTerminus getTerminus() {
        return this.corridorExit;
    }

    public void setTerminus(CorridorTerminus terminus) {
        this.corridorExit = terminus;
        inUse = terminus != null;
    }

    public DomeFloor getFloor() {
        return this.floor;
    }

    public Point3D getLocation() {
        return this.getFloor().getDome().getLocation().add(locationOffset);
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse() {
        inUse = true;
    }

    public CompassDirection getCompassDirection() {
        return compassDirection;
    }

}

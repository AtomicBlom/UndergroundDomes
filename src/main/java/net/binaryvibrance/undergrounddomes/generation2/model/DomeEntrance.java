package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.helpers.maths.Point3D;

public class DomeEntrance {
    private DomeFloor floor;
    private CorridorTerminus corridorExit;
    private boolean inUse = false;
    private Point3D location;
    private CompassDirection compassDirection;

    public DomeEntrance(DomeFloor floor, CompassDirection compassDirection, Point3D location) {
        this.floor = floor;
        this.compassDirection = compassDirection;
        this.location = location;
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
        return location;
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

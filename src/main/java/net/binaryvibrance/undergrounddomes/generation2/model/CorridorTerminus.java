package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.helpers.maths.Point3D;

import java.util.List;

public class CorridorTerminus {
    private Point3D location;
    private List<Corridor> corridors;

    public Point3D getLocation() {
        return location;
    }

    public void setLocation(Point3D location) {
        this.location = location;
    }

    public void addToCorridor(Corridor corridor) {
        // TODO Auto-generated method stub
        corridors.add(corridor);

    }
}

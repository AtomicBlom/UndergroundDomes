package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.helpers.maths.GeometryHelper;
import net.binaryvibrance.helpers.maths.Line;
import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation2.contracts.ILineIntersectable;

import java.util.LinkedList;
import java.util.List;

public class Dome implements ILineIntersectable {
    public DomePurpose purpose;
    private List<DomeFloor> domeFloors = new LinkedList<DomeFloor>();
    private Point3D location;
    private int diameter;

    public Dome(Point3D location, int diameter) {
        this.diameter = diameter;
        this.location = location;
    }

    public Point3D getLocation() {
        return location;
    }

    public double getRadius() {
        return diameter / 2;
    }

    public int getDiameter() {
        return diameter;
    }

    public void addFloor(DomeFloor floor) {
        domeFloors.add(floor);
    }

    public DomeFloor getFloor(int floor) {
        return domeFloors.get(floor);
    }

    @Override
    public boolean intersects(Line line) {
        return GeometryHelper.lineIntersectsSphere(line, location, getRadius());
    }
}

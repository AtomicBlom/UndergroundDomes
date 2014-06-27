package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.helpers.maths.Vector3;

import java.util.HashMap;
import java.util.Map;

public class DomeFloor {
	private final Dome dome;
	private final int level;
    private Map<CompassDirection, DomeEntrance> entrances;

    public DomeFloor(Dome dome, int level) {
	    this.dome = dome;
	    this.level = level;

        entrances = new HashMap<CompassDirection, DomeEntrance>();
        //From http://mathforum.org/dr.math/faq/formulas/faq.sphere.html
        //r = (h^2+r'^2)/(2h)`
        double levelRadius =Math.floor((Math.pow(level, 2) + Math.pow(dome.getRadius() - 0.5, 2)) / (2 * level)) + 1;

        DomeEntrance northEntrance = new DomeEntrance(
                this,
                CompassDirection.NORTH,
		        Vector3.multiply(Vector3.NORTH, levelRadius));
        DomeEntrance southEntrance = new DomeEntrance(
                this,
                CompassDirection.SOUTH,
		        Vector3.multiply(Vector3.SOUTH, levelRadius));

        DomeEntrance eastEntrance = new DomeEntrance(
                this,
                CompassDirection.EAST,
		        Vector3.multiply(Vector3.EAST, levelRadius));
        DomeEntrance westEntrance = new DomeEntrance(
                this,
                CompassDirection.WEST,
		        Vector3.multiply(Vector3.WEST, levelRadius));

        entrances.put(CompassDirection.NORTH, northEntrance);
        entrances.put(CompassDirection.SOUTH, southEntrance);
        entrances.put(CompassDirection.EAST, eastEntrance);
        entrances.put(CompassDirection.WEST, westEntrance);
    }

    public DomeEntrance getEntrance(CompassDirection direction) {
        return entrances.get(direction);
    }

    public int getLevel() {
        return level;
    }

	public Dome getDome() {
		return dome;
	}
}

package net.binaryvibrance.undergrounddomes.generation2.model;

import java.util.HashMap;
import java.util.Map;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.helpers.maths.Vector3;

public class DomeFloor {
    private final int level;
    private Map<CompassDirection, DomeEntrance> entrances;

    public DomeFloor(Dome dome, int level) {
        this.level = level;
        
        entrances = new HashMap<CompassDirection, DomeEntrance>();
        //TODO: Calculate Dome Entrance location
        
        //From http://mathforum.org/dr.math/faq/formulas/faq.sphere.html
        //r = (h^2+r'^2)/(2h)
        double levelRadius = (Math.pow(dome.getRadius(), 2) + Math.pow(level, 2))/(level * 2);
        
        DomeEntrance northEntrance = new DomeEntrance(
        		this, 
        		CompassDirection.NORTH,
        		new Point3D(0,0,0).add(Vector3.multiply(Vector3.NORTH, levelRadius)));
        DomeEntrance southEntrance = new DomeEntrance(
        		this, 
        		CompassDirection.SOUTH,
        		new Point3D(0,0,0).add(Vector3.multiply(Vector3.SOUTH, levelRadius)));
        
        DomeEntrance eastEntrance = new DomeEntrance(
        		this, 
        		CompassDirection.EAST,
        		new Point3D(0,0,0).add(Vector3.multiply(Vector3.EAST, levelRadius)));
        DomeEntrance westEntrance = new DomeEntrance(
        		this, 
        		CompassDirection.WEST,
        		new Point3D(0,0,0).add(Vector3.multiply(Vector3.WEST, levelRadius)));
        
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
}

package net.binaryvibrance.undergrounddomes.generation.model;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DomeFloor {
	private final Dome dome;
	private final int level;
    private Map<EnumFacing, DomeEntrance> entrances;

    public DomeFloor(Dome dome, int level) {
	    this.dome = dome;
	    this.level = level;

        entrances = new HashMap<EnumFacing, DomeEntrance>();
        //From http://mathforum.org/dr.math/faq/formulas/faq.sphere.html
        //r = (h^2+r'^2)/(2h)`
        double levelRadius =Math.floor((Math.pow(level, 2) + Math.pow(dome.getRadius() - 0.5, 2)) / (2 * level)) + 1;

        DomeEntrance northEntrance = new DomeEntrance(
                this,
		        EnumFacing.NORTH,
		        BlockPos.ORIGIN.offset(EnumFacing.NORTH, (int)levelRadius));
        DomeEntrance southEntrance = new DomeEntrance(
                this,
		        EnumFacing.SOUTH,
		        BlockPos.ORIGIN.offset(EnumFacing.SOUTH, (int)levelRadius));

        DomeEntrance eastEntrance = new DomeEntrance(
                this,
		        EnumFacing.EAST,
		        BlockPos.ORIGIN.offset(EnumFacing.EAST, (int)levelRadius));
        DomeEntrance westEntrance = new DomeEntrance(
                this,
		        EnumFacing.WEST,
		        BlockPos.ORIGIN.offset(EnumFacing.WEST, (int)levelRadius));

        entrances.put(EnumFacing.NORTH, northEntrance);
        entrances.put(EnumFacing.SOUTH, southEntrance);
        entrances.put(EnumFacing.EAST, eastEntrance);
        entrances.put(EnumFacing.WEST, westEntrance);
    }

    public DomeEntrance getEntrance(EnumFacing direction) {
        return entrances.get(direction);
    }

    public int getLevel() {
        return level;
    }

	public Dome getDome() {
		return dome;
	}

	public Collection<DomeEntrance> getEntrances() {
		return entrances.values();
	}
}

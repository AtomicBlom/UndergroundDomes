package net.binaryvibrance.undergrounddomes.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import net.binaryvibrance.undergrounddomes.generation.maths.IntegralVector3;
import cpw.mods.fml.common.FMLLog;

public class SphereInstance extends IntegralVector3 {
	private static final Logger log = FMLLog.getLogger();
	private static final int floorSize = 5;
	private int diameter;
	private float radius;
	private List<Integer> floors;

	public SphereInstance(IntegralVector3 location, int diameter) {
		super(location.x,location.y,location.z);
		this.diameter = diameter;
		this.radius = diameter / 2.0f;
	}
	
	public void createFloors(Random random) {
		ArrayList<Integer> definedFloors = new ArrayList<Integer>();
		//
		int available = (int)((diameter - 2) * 0.75); //Don't include walls
		int maxFloors = (int)Math.floor(available / (float)floorSize);
		log.info("MaxFloors: " + maxFloors);
		int actualFloors = maxFloors;// == 1 ? 1 : random.nextInt(maxFloors - 1) + 1;
		int interval = (int)Math.ceil(available / actualFloors);
		int variance = interval - floorSize;
		
		int baseHeight = diameter - available -2;
		definedFloors.add(baseHeight);
		log.info(String.format("Floor 0 at level %d", baseHeight));
		for (int floor = 1; floor < actualFloors; ++floor) {
			int floorVariance = random.nextBoolean() ? 1 : -1;
			int floorStart = baseHeight + floor * interval + (variance * floorVariance);
			log.info(String.format("Floor %d at level %d", floor, floorStart));
			definedFloors.add(floorStart);
		}
		
		floors = definedFloors;		
	}

	public int getDiameter() {
		return diameter;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public boolean isFloorLevel(int y) {
		for (Integer floorLevel : floors) {
			if (floorLevel == y) return true;
		}
		return false;
	}
	
	public int getFloorLevel(int index) {
		return floors.get(index);
	}
	
	public int getTranslatedFloorLevel(int index) {
		return (int)(y - radius + floors.get(index));
	}
}
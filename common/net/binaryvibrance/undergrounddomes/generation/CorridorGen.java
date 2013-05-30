package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.maths.Vector3;
import net.minecraft.world.World;

public class CorridorGen {

	private SphereChain sphereChain;

	public CorridorGen(SphereChain sphereChain) {
		this.sphereChain = sphereChain;
	}
	
	public void generateCorridor() {
		for (SphereInstance sphere : sphereChain.getChain()) {
			//Calculate Nearest Neighbours
			SphereNearestNeighbour snn = new SphereNearestNeighbour(sphere);
			for (SphereInstance neighbourSphere : sphereChain.getChain()) {
				snn.addNeighbour(neighbourSphere);
			}
			
			//Build corridor between nearest triad
			
			SphereInstance primary = null;
			for (SphereInstance secondary : snn) {
				if (primary == null) {
					primary = secondary;
					continue;
				}
				
				boolean valid = true;
				
				Point3D averagePoint = Point3D.average(sphere, primary, secondary);
				generateRects(sphere, averagePoint);			
				
				if (valid) break;
			}			
		}
	}

	private void generateRects(SphereInstance sphere, Point3D averagePoint) {
		int radius = (int)sphere.getRadius();
		//North
		Point3D northPoint = sphere.add(new Vector3(0, 0, radius));
		Point3D southPoint = sphere.add(new Vector3(0, 0, -radius));
		Point3D eastPoint = sphere.add(new Vector3(radius, 0, 0));
		Point3D westPoint = sphere.add(new Vector3(-radius, 0, 0));
		double northDistance = averagePoint.distance(northPoint);
		double southDistance = averagePoint.distance(southPoint);
		double eastDistance = averagePoint.distance(eastPoint);
		double westDistance = averagePoint.distance(westPoint);
		
		//Box a = null;
		//Box b = null;
		
		if (northDistance <= southDistance && northDistance <= eastDistance && northDistance <= westDistance) {
			//a = new Box(northPoint.add(new Vector3(-2, 0, 0)), new Point3D(northPoint.x + 2, 6, averagePoint.z + 2));
			//b = new Box(new Point3D(northPoint.x - 2, 0, averagePoint.z - 2), averagePoint.add(new Vector3(0, 6, 2)));
		} else if (southDistance <= northDistance && southDistance <= eastDistance && southDistance <= westDistance) {
			
		} else if (eastDistance <= northDistance && eastDistance <= southDistance && eastDistance <= westDistance) {
			
		} else {
			
		}
		
	}

	public void renderCorridor(World world) {
		
		// TODO Auto-generated method stub
		
	}	
	
	
}

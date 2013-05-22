package net.binaryvibrance.undergrounddomes.generation;

import java.util.LinkedList;

import net.binaryvibrance.undergrounddomes.generation.maths.IntegralVector3;
import net.binaryvibrance.undergrounddomes.generation.maths.Vector3;

public class Sphere {
	
	private int diameter;

	public Sphere(int diameter) {
		this.diameter = diameter;
		calculateAtoms(diameter);
	}
	
	public int getDiameter() {
		return diameter;
	}
	
	public LinkedList<SphereAtom> getAtoms() {
		return matchedAtoms;
	}
	
	protected SphereAtom[][][] atoms;
	protected LinkedList<SphereAtom> matchedAtoms;
	
	private void calculateAtoms(int diameter){
		atoms = new SphereAtom[diameter + 1][diameter + 1][diameter + 1];
		float radius = diameter / 2;
		double desiredWidth = Math.pow(radius, 2);
		Vector3 sphereCentre = new Vector3(radius, radius, radius);
		matchedAtoms = new LinkedList<SphereAtom>();
		// Pass 1: Determine Wall
		for (int scanZ = 0; scanZ < diameter; ++scanZ) {
			for (int scanY = 0; scanY < diameter; ++scanY) {
				for (int scanX = 0; scanX < diameter; ++scanX) {
					double dist = Math.pow(scanX - sphereCentre.x, 2)
							+ Math.pow(scanY - sphereCentre.y, 2)
							+ Math.pow(scanZ - sphereCentre.z, 2);
					if (dist < desiredWidth) {
						SphereAtom atom = new SphereAtom(
								ParticleType.Interior, scanX, scanY, scanZ);
						atoms[scanZ][scanY][scanX] = atom;
						matchedAtoms.add(atom);
					}
				}
			}
		}
		// Pass 2: Determine Interior
		for (SphereAtom atom : matchedAtoms) {
			int neighboursSet = 0;
			int neighboursNotSet = 0;

			for (IntegralVector3 check : IntegralVector3.getNeighbours()) {
				int indexZ = atom.z + check.z;
				int indexY = atom.y + check.y;
				int indexX = atom.x + check.x;

				if (indexX >= 0 && indexX <= diameter && indexY >= 0
						&& indexY <= diameter && indexZ >= 0
						&& indexZ <= diameter) {
					SphereAtom checkParticle = atoms[indexZ][indexY][indexX];
					if (checkParticle == null) {
						neighboursNotSet++;
					} else {
						neighboursSet++;
					}
				}
			}
			if (neighboursSet > 0 && neighboursNotSet > 0) {
				atom.setParticleType(ParticleType.Wall);
			}
		}
	}
}

package net.binaryvibrance.undergrounddomes.generation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import net.binaryvibrance.undergrounddomes.generation.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.maths.Point3DF;
import net.binaryvibrance.undergrounddomes.generation.maths.Vector3;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;

public class Sphere {
	private int diameter;

	private Sphere(int diameter) {
		this.diameter = diameter;
		calculateAtoms(diameter);
	}

	private static final Logger LOG = LogHelper.getLogger();
	private static final HashMap<Integer, Sphere> cachedSpheres = new HashMap<Integer, Sphere>();

	public static Sphere construct(int diameter) {
		Sphere sphere = null;
		if (cachedSpheres.containsKey(diameter)) {
			LOG.info(String.format("Found precalculated atoms for diameter %d", diameter));
			sphere = cachedSpheres.get(diameter);
		} else {
			LOG.info(String.format("Precalculating atoms for diameter %d", diameter));
			sphere = new Sphere(diameter);
			cachedSpheres.put(diameter, sphere);
			LOG.info(String.format("Precalculation for diameter %d complete", diameter));
		}
		return sphere;
	}

	public int getDiameter() {
		return diameter;
	}

	public SphereAtom[][][] getAtoms() {
		return atoms;
	}

	protected SphereAtom[][][] atoms;
	protected LinkedList<SphereAtom> matchedAtoms;

	private void calculateAtoms(int diameter) {
		atoms = new SphereAtom[diameter + 1][diameter + 1][diameter + 1];
		float radius = diameter / 2;
		double desiredWidth = Math.pow(radius, 2);
		Point3DF sphereCentre = new Point3DF(radius, radius, radius);
		matchedAtoms = new LinkedList<SphereAtom>();

		// Pass 1: Determine Sphere Atoms
		for (int scanZ = 0; scanZ < diameter; ++scanZ) {
			for (int scanY = 0; scanY < diameter; ++scanY) {
				for (int scanX = 0; scanX < diameter; ++scanX) {
					double dist = Math.pow(scanX - sphereCentre.x, 2) + Math.pow(scanY - sphereCentre.y, 2)
							+ Math.pow(scanZ - sphereCentre.z, 2);
					if (dist < desiredWidth) {
						SphereAtom atom = new SphereAtom(ParticleType.Interior, scanX, scanY, scanZ);
						atoms[scanZ][scanY][scanX] = atom;
						matchedAtoms.add(atom);
					}
				}
			}
		}

		// Pass 2: Determine Exterior Wall
		for (SphereAtom atom : matchedAtoms) {
			int neighboursSet = 0;
			int neighboursNotSet = 0;

			for (Vector3 vector : Vector3.NEIGHBOURS) {
				Point3D check = atom.add(vector);

				if (check.x >= 0 && check.x <= diameter && check.y >= 0 && check.y <= diameter && check.z >= 0 && check.z <= diameter) {
					SphereAtom checkParticle = atoms[check.z][check.y][check.x];
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

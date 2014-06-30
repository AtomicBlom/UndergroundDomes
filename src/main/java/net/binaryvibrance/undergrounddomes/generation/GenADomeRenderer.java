package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.helpers.maths.Vector3;
import net.binaryvibrance.undergrounddomes.generation.contracts.IAtomFieldRenderer;
import net.binaryvibrance.undergrounddomes.generation.model.Atom;
import net.binaryvibrance.undergrounddomes.generation.model.AtomElement;
import net.binaryvibrance.undergrounddomes.generation.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation.model.Dome;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.util.Vec3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Steven Blom on 23/06/2014.
 *
 * Renders domes to the atomfield.
 */
public class GenADomeRenderer implements IAtomFieldRenderer {

	private List<Dome> domes;

	public GenADomeRenderer(List<Dome> domes) {

		this.domes = domes;
	}

	@Override
	public void RenderToAtomField(AtomField field) {
		for (Dome dome : domes) {
			int firstFloorLevel = dome.getFloor(0).getLevel();
			//FIXME: Render domes to the atomfield
			PreRenderedDome renderedDome = construct(dome.getDiameter());
			int minX = (int)(dome.getLocation().x - dome.getRadius() + 0.5);
			int minY = (int)(dome.getLocation().y - dome.getRadius() + 0.5);
			int minZ = (int)(dome.getLocation().z - dome.getRadius() + 0.5);
			for (Atom[][] zAtoms : renderedDome.getAtoms()) {
				for (int y = firstFloorLevel; y < zAtoms.length; y++) {
					Atom[] yAtoms = zAtoms[y];
					boolean isFloor = dome.isFloorLevel(y);

					for (Atom atom : yAtoms) {
						if (atom != null) {
							AtomElement atomElement = atom.getAtomElement();
							if (isFloor && atomElement == AtomElement.Interior) {
								atomElement = AtomElement.Floor;
							}

							if (atomElement != AtomElement.Untouched) {
								field.SetAtomAt(atom.xCoord + minX, atom.yCoord + minY, atom.zCoord + minZ, atomElement);
							}
						}
					}
				}
			}
		}

	}

	private static final HashMap<Integer, PreRenderedDome> cachedSpheres = new HashMap<Integer, PreRenderedDome>();

	public static PreRenderedDome construct(int diameter) {
		PreRenderedDome sphere;
		if (cachedSpheres.containsKey(diameter)) {
			LogHelper.info("Found pre-calculated atoms for diameter %d", diameter);
			sphere = cachedSpheres.get(diameter);
		} else {
			LogHelper.info("Pre-calculating atoms for diameter %d", diameter);
			sphere = new PreRenderedDome(diameter);
			cachedSpheres.put(diameter, sphere);
			LogHelper.info("Pre-calculation for diameter %d complete", diameter);
		}
		return sphere;
	}

	static class PreRenderedDome {

		private PreRenderedDome(int diameter) {
			this.diameter = diameter;
		}

		private int diameter;

		public Atom[][][] getAtoms() {
			if (atoms == null) {
				atoms = calculateAtoms(diameter);
			}
			return atoms;
		}

		protected Atom[][][] atoms;

		private static Atom[][][] calculateAtoms(int diameter) {
			Atom[][][] atoms = new Atom[diameter+1][diameter+1][diameter+1];
			LinkedList<Atom> matchedAtoms;
			double radius = diameter / 2.0d;
			double rSquared = Math.pow(radius, 2);
			Point3D sphereCentre = new Point3D(radius, radius, radius);
			matchedAtoms = new LinkedList<Atom>();

			// Pass 1: Determine Sphere Atoms
			for (int scanZ = 0; scanZ < diameter + 1; ++scanZ) {
				for (int scanY = 0; scanY < diameter + 1; ++scanY) {
				//int scanY = (diameter - 1) / 2;
					for (int scanX = 0; scanX < diameter + 1; ++scanX) {
						double dist = Math.pow((sphereCentre.x - 0.5) - scanX, 2)
								+ Math.pow((sphereCentre.y - 0.5) - scanY, 2)
								+ Math.pow((sphereCentre.z - 0.5) - scanZ, 2);
						if (dist < rSquared) {
							Atom atom = new Atom(AtomElement.Interior, scanX, scanY, scanZ);
							atoms[scanZ][scanY][scanX] = atom;
							matchedAtoms.add(atom);
						}
					}
				}
			}

			// Pass 2: Determine Exterior Wall
			for (Atom atom : matchedAtoms) {
				int neighboursSet = 0;
				int neighboursNotSet = 0;

				for (Vec3 vector : Vector3.NEIGHBOURS) {
					Point3D check = atom.add(vector);

					if (check.x >= 0 && check.x < diameter && check.y >= 0 && check.y < diameter && check.z >= 0 && check.z < diameter) {
						Atom checkParticle = atoms[check.zCoord][check.yCoord][check.xCoord];
						if (checkParticle == null) {
							neighboursNotSet++;
						} else {
							neighboursSet++;
						}
					} else {
						neighboursNotSet++;
					}
				}
				if (neighboursSet > 0 && neighboursNotSet > 0) {
					atom.setAtomElement(AtomElement.Wall);
				}
			}
			return atoms;
		}

	}

}

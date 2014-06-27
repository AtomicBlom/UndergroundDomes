package net.binaryvibrance.undergrounddomes.generation2;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.helpers.maths.Vector3;
import net.binaryvibrance.undergrounddomes.generation2.contracts.IAtomFieldRenderer;
import net.binaryvibrance.undergrounddomes.generation2.model.Atom;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomElement;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation2.model.Dome;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
import net.minecraft.util.Vec3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

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
			//FIXME: Render domes to the atomfield
			PreRenderedDome renderedDome = construct(dome.getDiameter());
			int minX = (int)(dome.getLocation().x - dome.getRadius());
			int minY = (int)(dome.getLocation().y - dome.getRadius());
			int minZ = (int)(dome.getLocation().z - dome.getRadius());
			for (Atom[][] zAtoms : renderedDome.getAtoms()) {
				for (Atom[] yAtoms : zAtoms) {
					for (Atom atom : yAtoms) {
						if (atom != null && atom.getAtomElement() != AtomElement.Untouched) {
							field.SetAtomAt(atom.xCoord + minX, atom.yCoord + minY, atom.zCoord + minZ, atom.getAtomElement());
						}
					}
				}
			}
		}

	}

	private static final HashMap<Integer, PreRenderedDome> cachedSpheres = new HashMap<Integer, PreRenderedDome>();

	public static PreRenderedDome construct(int diameter) {
		PreRenderedDome sphere = null;
		if (cachedSpheres.containsKey(diameter)) {
			LogHelper.info(String.format("Found pre-calculated atoms for diameter %d", diameter));
			sphere = cachedSpheres.get(diameter);
		} else {
			LogHelper.info(String.format("Pre-calculating atoms for diameter %d", diameter));
			sphere = new PreRenderedDome(diameter);
			cachedSpheres.put(diameter, sphere);
			LogHelper.info(String.format("Pre-calculation for diameter %d complete", diameter));
		}
		return sphere;
	}

	static class PreRenderedDome {

		private PreRenderedDome(int diameter) {
			this.diameter = diameter;
		}

		private int diameter;

		public int getDiameter() {
			return diameter;
		}

		public Atom[][][] getAtoms() {
			if (atoms == null) {
				atoms = calculateAtoms(diameter);
			}
			return atoms;
		}

		protected Atom[][][] atoms;

		private static Atom[][][] calculateAtoms(int diameter) {
			Atom[][][] atoms = new Atom[diameter + 1][diameter + 1][diameter + 1];
			LinkedList<Atom> matchedAtoms;
			float radius = diameter / 2;
			double desiredWidth = Math.pow(radius, 2);
			Point3D sphereCentre = new Point3D(radius, radius, radius);
			matchedAtoms = new LinkedList<Atom>();

			// Pass 1: Determine Sphere Atoms
			for (int scanZ = 0; scanZ < diameter; ++scanZ) {
				for (int scanY = 0; scanY < diameter; ++scanY) {
					for (int scanX = 0; scanX < diameter; ++scanX) {
						double dist = Math.pow(scanX - sphereCentre.x, 2) + Math.pow(scanY - sphereCentre.y, 2)
								+ Math.pow(scanZ - sphereCentre.z, 2);
						if (dist < desiredWidth) {
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

					if (check.x >= 0 && check.x <= diameter && check.y >= 0 && check.y <= diameter && check.z >= 0 && check.z <= diameter) {
						Atom checkParticle = atoms[check.zCoord][check.yCoord][check.xCoord];
						if (checkParticle == null) {
							neighboursNotSet++;
						} else {
							neighboursSet++;
						}
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

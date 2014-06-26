package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.helpers.maths.Point3D;

import java.util.Arrays;

public class AtomField {

	private final Atom[][][] atomField;
	private Point3D size;

	public AtomField(Point3D size) {
		this.size = size;
		this.atomField = new Atom[(int)size.z][(int)size.y][(int)size.x];
		for (int z = 0; z < size.z; ++z) {
			for (int y = 0; y < size.y; ++y) {
				for (int x = 0; x < size.x; ++x){
					atomField[z][y][x] = new Atom(AtomElement.Untouched, x, y, z);
				}
			}
		}
	}

	public Point3D getSize() {
		return size;
	}

	public Atom getAtomAt(int x, int y, int z) {
		if (x < 0 || x >= size.x || y < 0 || y >= size.y || z < 0 || z >= size.z) {
			throw new IndexOutOfBoundsException("attempt to get an atom that does it outside of the bounds of the AtomField.");
		}
		return atomField[z][y][x];
	}

	public void SetAtomAt(int x, int y, int z, AtomElement element) {
		Atom atom = getAtomAt(x, y, z);
		atom.setAtomElement(element);
	}

	public Atom[][][] getSlice(int minX, int minZ, int maxX, int maxZ) {
		int xLength = maxX - minX;
		Atom[][][] slice = new Atom[maxZ - minZ][(int)size.y][xLength];

		for (int z = minZ, zPos = 0; z < maxZ; ++z, ++zPos) {
			for (int y = 0; y < size.y; ++y) {
				for (int x = minX, xPos = 0; x < maxX; ++x, ++xPos) {
					//System.arraycopy(atomField[z][y], minX, slice[z][y], 0, xLength);
					slice[zPos][y][xPos] = atomField[z][y][x];
				}
			}
		}
		return slice;
	}
}

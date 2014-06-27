package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.helpers.maths.Point3D;

import java.util.Arrays;

public class AtomField {

	private final Atom[][][] atomField;
	private Point3D size;
	private int buffer = 10;
	public AtomField(Point3D size) {
		this.size = size;
		this.atomField = new Atom[(int)size.z + buffer][(int)size.y + buffer][(int)size.x + buffer];
		/*for (int z = 0; z < size.z + buffer; ++z) {
			for (int y = 0; y < size.y + buffer; ++y) {
				for (int x = 0; x < size.x + buffer; ++x){
					atomField[z][y][x] = new Atom(AtomElement.Untouched, x, y, z);
				}
			}
		}*/
	}

	public Point3D getSize() {
		return size;
	}

	public Atom getAtomAt(int x, int y, int z) {
		if (x < 0 || x >= size.x + buffer || y < 0 || y >= size.y + buffer || z < 0 || z >= size.z + buffer) {
			String message = String.format("Requested atom @ (%d, %d, %d) was out of range of (0,0,0)->(%d,%d,%d)", x, y, z, size.xCoord, size.yCoord, size.zCoord);
			throw new IndexOutOfBoundsException(message);
		}

		Atom atom = atomField[z][y][x];
		if (atom == null) {
			atom = new Atom(AtomElement.Untouched, x, y, z);
		}

		return atom;
	}

	public void SetAtomAt(int x, int y, int z, AtomElement element) {
		if (x < 0 || x >= size.x + buffer || y < 0 || y >= size.y + buffer || z < 0 || z >= size.z + buffer) {
			String message = String.format("Requested atom @ (%d, %d, %d) was out of range of (0,0,0)->(%d,%d,%d)", x, y, z, size.xCoord, size.yCoord, size.zCoord);
			throw new IndexOutOfBoundsException(message);
		}

		Atom atom = atomField[z][y][x];
		if (atom == null) {
			atom = new Atom(AtomElement.Untouched, x, y, z);
			atomField[z][y][x] = atom;
		}
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

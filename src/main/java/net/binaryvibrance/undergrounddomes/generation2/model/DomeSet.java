package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.generation.SphereInstance;
import net.binaryvibrance.undergrounddomes.generation2.GenACorridorRenderer;
import net.binaryvibrance.undergrounddomes.generation2.GenADomeRenderer;
import net.binaryvibrance.undergrounddomes.generation2.contracts.DomeGeneratorResult;
import net.binaryvibrance.undergrounddomes.generation2.contracts.IAtomFieldRenderer;
import net.binaryvibrance.undergrounddomes.generation2.contracts.ICorridorGenerator;
import net.binaryvibrance.undergrounddomes.generation2.contracts.IDomeGenerator;
import scala.Array;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class DomeSet {
    private AtomField atomField;

    private final AtomicBoolean ready = new AtomicBoolean(false);
    private boolean inUse = false;

	private final int chunkX;
	private final int chunkZ;
	private final IDomeGenerator domeGenerator;
    private final ICorridorGenerator corridorGenerator;
	private DomeGeneratorResult domes;

	public DomeSet(int chunkX, int chunkZ,
		    IDomeGenerator domeGenerator,
		    ICorridorGenerator corridorGenerator) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;

		this.domeGenerator = domeGenerator;
        this.corridorGenerator = corridorGenerator;
    }

	public class ChunkData {
		private final Point3D chunkLocation;
		private final Atom[][][] atoms;

		public ChunkData(Point3D chunkLocation, Atom[][][] atoms) {

			this.chunkLocation = chunkLocation;
			this.atoms = atoms;
		}

		public Point3D getChunkLocation() {
			return chunkLocation;
		}

		public Atom[][][] getAtoms() {
			return atoms;
		}
	}

	public List<ChunkData> getRequiredChunks() {
		//HashSet<Point3D> requiredChunks = new HashSet<Point3D>();
		List<ChunkData> results = new LinkedList<ChunkData>();

		for (int z = 0; z < atomField.getSize().z; z += 16) {
			for (int x = 0; x < atomField.getSize().x; x += 16) {
				Point3D chunkLocation = new Point3D(
						x / 16 + chunkX,
						0,
						z / 16 + chunkZ
				);
				int maxX = Math.min(x + 16, atomField.getSize().xCoord);
				int maxZ = Math.min(z + 16, atomField.getSize().zCoord);
				if (maxX == 0 || maxZ == 0) {
					continue;
				}
				Atom[][][] atoms = atomField.getSlice(x, z, maxX, maxZ);
				ChunkData data = new ChunkData(chunkLocation, atoms);
				//requiredChunks.add(chunkLocation);
				results.add(data);
			}
		}

		/*for (Dome dome : domes.getDomes()) {
			Point3D domeLocation = dome.getLocation();

			int minChunkX = (int) (domeLocation.xCoord + chunkX - dome.getRadius()) % 16;
			int maxChunkX = (int) (domeLocation.xCoord + chunkX + dome.getRadius()) % 16;
			int minChunkZ = (int) (domeLocation.zCoord + chunkZ - dome.getRadius()) % 16;
			int maxChunkZ = (int) (domeLocation.zCoord + chunkZ + dome.getRadius()) % 16;

			for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
				for (int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
					Point3D chunkLocation = new Point3D(chunkX, 0, chunkZ);
					if (!requiredChunks.contains(chunkLocation)) {
						Atom[][][] atoms = atomField.getSlice(minChunkX * 16, minChunkZ * 16, minChunkX * 16 + 16, minChunkZ * 16 + 16);
						ChunkData data = new ChunkData(chunkLocation, atoms);
						requiredChunks.add(chunkLocation);
						results.add(data);
					}


				}
			}
		}*/
		return results;
	}


	public void startGenerationAsync() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                startGeneration();
            }
        });
        t.setName("Dome Generation");
        t.run();
    }

	public AtomField getAtomField() {
		return atomField;
	}

    public void startGeneration() {
	    domes = domeGenerator.generate();
	    List<Corridor> corridors = corridorGenerator.generate(domes.getDomes());

	    atomField = new AtomField(domes.getSize());

	    IAtomFieldRenderer renderer;
	    renderer = new GenADomeRenderer(domes.getDomes());
		renderer.RenderToAtomField(atomField);
        renderer = new GenACorridorRenderer(corridors);
        renderer.RenderToAtomField(atomField);

        ready.set(true);
    }

    public synchronized boolean tryAcquireLock() {
        if (ready.get()) {
            if (!inUse) {
                inUse = true;
                return true;
            }
        }
        return false;
    }
}

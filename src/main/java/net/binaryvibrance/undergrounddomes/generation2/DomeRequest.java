package net.binaryvibrance.undergrounddomes.generation2;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.Configuration;
import net.binaryvibrance.undergrounddomes.generation2.contracts.*;
import net.binaryvibrance.undergrounddomes.generation2.model.Atom;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation2.model.Corridor;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DomeRequest {
	private final DomeRequestResult result;
	private AtomField atomField;

	private final int chunkX;
	private final int chunkZ;
	private INotifyDomeGenerationComplete notify;
	private final IDomeGenerator domeGenerator;
    private final ICorridorGenerator corridorGenerator;
	private DomeGeneratorResult domes;

	public DomeRequest(int chunkX, int chunkZ, World world, IChunkProvider chunkProvider, INotifyDomeGenerationComplete notify) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.notify = notify;
		Configuration _configuration = Configuration.getConfiguration();

		Random r = new Random();
		r.setSeed(1 +chunkX ^ 31 + chunkZ ^ 17 + world.getWorldInfo().getSeed() ^ 13);

		IDomeGenerator domeGenerator = _configuration.getDefaultDomeGenerator();
		domeGenerator.setRandom(r);
		ICorridorGenerator corridorGenerator = _configuration.getDefaultCorridorGenerator();
		corridorGenerator.setRandom(r);

		this.domeGenerator = domeGenerator;
        this.corridorGenerator = corridorGenerator;
		this.result = new DomeRequestResult(world, chunkProvider, chunkX, chunkZ);
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

    public void startGeneration() {
	    domes = domeGenerator.generate();
	    List<Corridor> corridors = corridorGenerator.generate(domes.getDomes());

	    atomField = new AtomField(domes.getSize());

	    IAtomFieldRenderer renderer;
	    renderer = new GenADomeRenderer(domes.getDomes());
		renderer.RenderToAtomField(atomField);
        renderer = new GenACorridorRenderer(corridors);
        renderer.RenderToAtomField(atomField);

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
			    results.add(data);
		    }
	    }

	    result.setChunkData(results);

	    notify.OnComplete(result);
    }
}

package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.Configuration;
import net.binaryvibrance.undergrounddomes.generation.contracts.*;
import net.binaryvibrance.undergrounddomes.generation.model.Atom;
import net.binaryvibrance.undergrounddomes.generation.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation.model.Corridor;
import net.binaryvibrance.undergrounddomes.helpers.LogHelper;
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
	    LogHelper.info("Generating Domes");
	    domes = domeGenerator.generate();
	    LogHelper.info("Generating Corridors");

	    List<Corridor> corridors = corridorGenerator.generate(domes.getDomes());

	    atomField = new AtomField(domes.getSize());

	    IAtomFieldRenderer renderer;
	    LogHelper.info("Rendering Domes");

	    renderer = new GenADomeRenderer(domes.getDomes());
		renderer.RenderToAtomField(atomField);
	    LogHelper.info("Rendering Corridors");

	    renderer = new GenACorridorRenderer(corridors);

        renderer.RenderToAtomField(atomField);

	    List<ChunkData> results = new LinkedList<ChunkData>();

	    LogHelper.info("Splitting AtomField into Chunks");

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
	    LogHelper.info("Generation Complete");
	    notify.OnComplete(result);
    }
}

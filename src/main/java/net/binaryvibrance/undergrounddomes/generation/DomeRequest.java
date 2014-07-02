package net.binaryvibrance.undergrounddomes.generation;

import net.binaryvibrance.helpers.maths.Point3D;
import net.binaryvibrance.undergrounddomes.configuration.ConfigurationHandler;
import net.binaryvibrance.undergrounddomes.generation.contracts.*;
import net.binaryvibrance.undergrounddomes.generation.model.Atom;
import net.binaryvibrance.undergrounddomes.generation.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation.model.Corridor;
import net.binaryvibrance.undergrounddomes.generation.v1.CorridorRenderer;
import net.binaryvibrance.undergrounddomes.generation.v1.DomeRenderer;
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
		ConfigurationHandler _configuration = ConfigurationHandler.instance();

		Random r = new Random();
		r.setSeed(1 +chunkX ^ 31 + chunkZ ^ 17 + world.getWorldInfo().getSeed() ^ 13);

		IDomeGenerator domeGenerator = _configuration.getDefaultDomeGenerator();
		domeGenerator.setRandom(r);
		ICorridorGenerator corridorGenerator = _configuration.getDefaultCorridorGenerator();
		corridorGenerator.setRandom(r);

		this.domeGenerator = domeGenerator;
        this.corridorGenerator = corridorGenerator;
		this.result = new DomeRequestResult(world, chunkProvider);
    }

	public void startGenerationAsync() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
	            startGeneration();
	            notify.OnComplete(result);
            }
        });
        t.setName("Dome Generation");
        t.start();
    }

    public DomeRequestResult startGeneration() {
	    LogHelper.info("Generating Domes");
	    domes = domeGenerator.generate();
	    LogHelper.info("Generating Corridors");

	    List<Corridor> corridors = corridorGenerator.generate(domes.getDomes());

	    atomField = new AtomField(domes.getSize());

	    IAtomFieldRenderer renderer;
	    LogHelper.info("Rendering Domes");

	    renderer = new DomeRenderer(domes.getDomes());
		renderer.RenderToAtomField(atomField);
	    LogHelper.info("Rendering Corridors");

	    renderer = new CorridorRenderer(corridors);

        renderer.RenderToAtomField(atomField);

	    List<DomeRequestResult.ChunkData> results = new LinkedList<DomeRequestResult.ChunkData>();

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
			    DomeRequestResult.ChunkData data = new DomeRequestResult.ChunkData(chunkLocation, atoms);
			    results.add(data);
		    }
	    }

	    result.setChunkData(results);
	    LogHelper.info("Generation Complete");
	    return result;
    }
}

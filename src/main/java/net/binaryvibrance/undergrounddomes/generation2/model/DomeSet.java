package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.undergrounddomes.generation2.GenACorridorRenderer;
import net.binaryvibrance.undergrounddomes.generation2.GenADomeRenderer;
import net.binaryvibrance.undergrounddomes.generation2.contracts.IAtomFieldRenderer;
import net.binaryvibrance.undergrounddomes.generation2.contracts.ICorridorGenerator;
import net.binaryvibrance.undergrounddomes.generation2.contracts.IDomeGenerator;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DomeSet {
    private AtomField atomField;
    private List<Dome> domes;
    private List<Corridor> corridors;

    private AtomicBoolean ready = new AtomicBoolean(false);
    private boolean inUse = false;

    private IDomeGenerator domeGenerator;
    private ICorridorGenerator corridorGenerator;

    public DomeSet(
		    IDomeGenerator domeGenerator,
		    ICorridorGenerator corridorGenerator) {

        this.domeGenerator = domeGenerator;
        this.corridorGenerator = corridorGenerator;
    }

    public void create() {
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
	    atomField = new AtomField();
        domes = domeGenerator.Generate();
        corridors = corridorGenerator.generate(domes);

	    IAtomFieldRenderer renderer;
	    renderer = new GenADomeRenderer(domes);
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

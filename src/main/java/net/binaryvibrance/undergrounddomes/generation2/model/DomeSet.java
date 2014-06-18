package net.binaryvibrance.undergrounddomes.generation2.model;

import net.binaryvibrance.undergrounddomes.generation2.GenACorridorRenderer;
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

    public DomeSet(IDomeGenerator domeGenerator, ICorridorGenerator corridorGenerator) {

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

    private void startGeneration() {
        domes = domeGenerator.Generate();
        corridors = corridorGenerator.generate(domes);

        IAtomFieldRenderer renderer = new GenACorridorRenderer(corridors);
        renderer.RenderToAtomField(atomField);

        ready.set(true);
    }

    public synchronized boolean tryAquireLock() {
        if (ready.get()) {
            if (!inUse) {
                inUse = true;
                return true;
            }
        }
        return false;
    }
}

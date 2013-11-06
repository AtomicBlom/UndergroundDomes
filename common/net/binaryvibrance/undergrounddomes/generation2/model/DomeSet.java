package net.binaryvibrance.undergrounddomes.generation2.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.binaryvibrance.undergrounddomes.generation2.ICorridorGenerator;
import net.binaryvibrance.undergrounddomes.generation2.IDomeGenerator;

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
		domeGenerator.Generate();
		
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

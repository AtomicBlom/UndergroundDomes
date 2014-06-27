package net.binaryvibrance.undergrounddomes.generation.contracts;

import java.util.List;
import java.util.Random;

import net.binaryvibrance.undergrounddomes.generation.model.Corridor;
import net.binaryvibrance.undergrounddomes.generation.model.Dome;

public interface ICorridorGenerator {

	List<Corridor> generate(List<Dome> domes);
	void setRandom(Random random);
}


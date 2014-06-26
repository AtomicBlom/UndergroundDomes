package net.binaryvibrance.undergrounddomes.generation2.contracts;

import java.util.List;
import java.util.Random;

import net.binaryvibrance.undergrounddomes.generation2.model.Corridor;
import net.binaryvibrance.undergrounddomes.generation2.model.Dome;

public interface ICorridorGenerator {

	List<Corridor> generate(List<Dome> domes);
	void setRandom(Random random);
}

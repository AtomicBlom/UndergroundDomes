package net.binaryvibrance.undergrounddomes.generation2.contracts;

import java.util.List;
import java.util.Random;

import net.binaryvibrance.undergrounddomes.generation2.model.Dome;

public interface IDomeGenerator {

	DomeGeneratorResult generate();
	void setRandom(Random random);
}

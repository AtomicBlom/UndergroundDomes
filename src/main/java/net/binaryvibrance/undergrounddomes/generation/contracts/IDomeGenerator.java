package net.binaryvibrance.undergrounddomes.generation.contracts;

import java.util.Random;

public interface IDomeGenerator {

	DomeGeneratorResult generate();
	void setRandom(Random random);
}

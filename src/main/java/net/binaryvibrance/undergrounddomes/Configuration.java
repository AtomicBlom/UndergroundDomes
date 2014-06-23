package net.binaryvibrance.undergrounddomes;

import net.binaryvibrance.undergrounddomes.generation2.GenACorridorGenerator;
import net.binaryvibrance.undergrounddomes.generation2.GenADomeGenerator;
import net.binaryvibrance.undergrounddomes.generation2.contracts.ICorridorGenerator;
import net.binaryvibrance.undergrounddomes.generation2.contracts.IDomeGenerator;

/**
 * Tracks the configuration and configurable runtime instances.
 */
public class Configuration {

    private static Configuration _configuration;
    private int _domeHeight;

	//TODO: Persist Changes
	//TODO: Read Changes

    private Configuration() {
        _domeHeight = 96;
    }

    public static Configuration getConfiguration() {
        if (_configuration == null) {
            _configuration = new Configuration();
        }
        return _configuration;
    }

    public int getDomeHeight() {
        return _domeHeight;
    }

    public void setDomeHeight(int domeHeight) {
        _domeHeight = domeHeight;
    }

	public boolean getMultiThreaded() {
		//FIXME: Allow this to be set when I've figured out how to support this properly.
		return false;
	}

	public IDomeGenerator getDefaultDomeGenerator() {
		return new GenADomeGenerator();
	}

	public ICorridorGenerator getDefaultCorridorGenerator() {
		return new GenACorridorGenerator();
	}
}

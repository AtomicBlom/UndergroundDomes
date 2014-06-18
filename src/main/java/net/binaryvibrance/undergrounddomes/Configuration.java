package net.binaryvibrance.undergrounddomes;

/**
 * Created by CodeWarrior on 15/06/2014.
 */
public class Configuration {

    private static Configuration _configuration;
    private int _domeHeight;

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
}

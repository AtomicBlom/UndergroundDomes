package net.binaryvibrance.undergrounddomes.generation.contracts;

import net.binaryvibrance.undergrounddomes.generation.DomeRequestResult;

public interface INotifyDomeGenerationComplete {
	void OnComplete(DomeRequestResult requestResult);
}

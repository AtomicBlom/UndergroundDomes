package net.binaryvibrance.undergrounddomes.generation2.contracts;

import net.binaryvibrance.undergrounddomes.generation2.DomeRequestResult;

public interface INotifyDomeGenerationComplete {
	void OnComplete(DomeRequestResult requestResult);
}

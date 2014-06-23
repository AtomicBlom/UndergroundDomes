package net.binaryvibrance.undergrounddomes.generation2;

import net.binaryvibrance.undergrounddomes.generation2.contracts.IAtomFieldRenderer;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation2.model.Dome;

import java.util.List;

/**
 * Created by Steven Blom on 23/06/2014.
 *
 * Renders domes to the atomfield.
 */
public class GenADomeRenderer implements IAtomFieldRenderer {

	private List<Dome> domes;

	public GenADomeRenderer(List<Dome> domes) {

		this.domes = domes;
	}

	@Override
	public void RenderToAtomField(AtomField field) {
		for (Dome dome : domes) {
			//FIXME: Render domes to the atomfield
		}
	}
}

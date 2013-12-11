package net.binaryvibrance.undergrounddomes.generation2;

import java.util.List;

import net.binaryvibrance.undergrounddomes.generation2.contracts.IAtomFieldRenderer;
import net.binaryvibrance.undergrounddomes.generation2.model.AtomField;
import net.binaryvibrance.undergrounddomes.generation2.model.Corridor;

public class GenACorridorRenderer implements IAtomFieldRenderer {

	private List<Corridor> corridors;

	public GenACorridorRenderer(List<Corridor> corridors) {
		this.corridors = corridors;
	}
	
	@Override
	public void RenderToAtomField(AtomField field) {
				
	}

}

package net.binaryvibrance.undergrounddomes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

/**
 * Created by CodeWarrior on 30/06/2014.
 */
public class GuiFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public boolean hasConfigGui()
	{
		return false;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new ConfigGui(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}
}

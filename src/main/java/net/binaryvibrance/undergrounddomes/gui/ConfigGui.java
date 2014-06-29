package net.binaryvibrance.undergrounddomes.gui;

import cpw.mods.fml.client.config.GuiConfig;
import net.binaryvibrance.undergrounddomes.Constants;
import net.binaryvibrance.undergrounddomes.configuration.ConfigurationHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by CodeWarrior on 30/06/2014.
 */
public class ConfigGui extends GuiConfig {
	public ConfigGui(GuiScreen parent) {
		super(parent,
				new ConfigElement(ConfigurationHandler.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
				Constants.Mod.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.configuration.toString()));
	}
}

package net.binaryvibrance.undergrounddomes;

import net.binaryvibrance.undergrounddomes.block.BlockLightReceptor;
import net.minecraft.util.ResourceLocation;

public final class Constants {
	public final static class Mod {
		public static final String MOD_ID = "bvdomes";
		public static final String MOD_NAME = "Underground Domes";
		public static final String MOD_VERSION = "0.0.1";
		public static final String CLIENT_SIDE_PROXY = "net.binaryvibrance.undergrounddomes.proxy.ClientProxy";
		public static final String SERVER_SIDE_PROXY = "net.binaryvibrance.undergrounddomes.proxy.ServerProxy";
	}

	public final static class Blocks {
		public static final BlockLightReceptor LIGHT_RECEPTOR = new BlockLightReceptor();
	}

	public final static class Textures {
		public static final String MODEL_SHEET_LOCATION = "textures/models/";
		public static final ResourceLocation MODEL_LIGHT_RECEPTOR = new ResourceLocation(Mod.MOD_ID.toLowerCase(), MODEL_SHEET_LOCATION + "lightreceptor.png");
	}
}

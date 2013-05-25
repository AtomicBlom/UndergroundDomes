package net.binaryvibrance.undergrounddomes;

import net.binaryvibrance.undergrounddomes.block.BVBlock;
import net.binaryvibrance.undergrounddomes.block.BlockLightReceptor;

public final class Constants {
	public final static class Mod {
		public static final String MOD_ID = "BV.Domes";
		public static final String MOD_NAME = "Underground Domes";
		public static final String MOD_VERSION = "0.0.1";
		public static final String CLIENT_SIDE_PROXY = "net.binaryvibrance.undergrounddomes.proxy.ClientProxy";
		public static final String SERVER_SIDE_PROXY = "net.binaryvibrance.undergrounddomes.proxy.ServerProxy";
	}
	public final static class Blocks {
		public static final BVBlock LightReceptor = new BlockLightReceptor();
	}
}

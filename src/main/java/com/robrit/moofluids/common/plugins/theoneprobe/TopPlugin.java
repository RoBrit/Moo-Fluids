package com.robrit.moofluids.common.plugins.theoneprobe;

import com.google.common.base.Function;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TopPlugin {

	public static void init() {
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.robrit.moofluids.common.plugins.theoneprobe.TopPlugin$GetTheOneProbe");
	}

	public static class GetTheOneProbe implements Function<ITheOneProbe, Void>  {

		@Override
		public Void apply(ITheOneProbe probe) {
			probe.registerEntityProvider(new CowInfoProvider());
			return null;
		}
	}
	
}

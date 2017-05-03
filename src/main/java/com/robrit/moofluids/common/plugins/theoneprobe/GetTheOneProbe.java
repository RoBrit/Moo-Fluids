package com.robrit.moofluids.common.plugins.theoneprobe;

import java.util.function.Function;

import javax.annotation.Nullable;

import mcjty.theoneprobe.api.ITheOneProbe;

public class GetTheOneProbe implements Function<ITheOneProbe, Void>  {

	@Nullable
	@Override
	public Void apply(ITheOneProbe probe) {
		probe.registerEntityProvider(new CowInfoProvider());
		return null;
	}
}
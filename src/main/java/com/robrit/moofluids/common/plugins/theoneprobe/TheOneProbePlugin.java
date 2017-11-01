/*
 * TheOneProbePlugin.java
 *
 * Copyright (c) 2014-2017 TheRoBrit
 *
 * Moo-Fluids is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Moo-Fluids is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.robrit.moofluids.common.plugins.theoneprobe;

import com.google.common.base.Function;

import com.robrit.moofluids.common.ref.ModInformation;
import com.robrit.moofluids.common.util.LogHelper;

import net.minecraftforge.fml.common.event.FMLInterModComms;

import javax.annotation.Nullable;

import mcjty.theoneprobe.api.ITheOneProbe;

public class TheOneProbePlugin {
  private static final String REGISTRAR_CLASSPATH =
      ModInformation.MOD_PACKAGE + ".common.plugins.theoneprobe.TheOneProbePlugin$Register";

  public static void init() {
    LogHelper.info("TheOneProbe detected. Registering entities with TheOneProbe registry.");
    FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", REGISTRAR_CLASSPATH);
  }

  public static class Register implements Function<ITheOneProbe, Void> {
    @Nullable
    @Override
    public Void apply(ITheOneProbe probe) {
      probe.registerEntityProvider(new FluidCowEntityProvider());
      return null;
    }
  }
}
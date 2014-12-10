/*
 * CommonProxy.java
 *
 * Copyright (c) 2014 TheRoBrit
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

package com.robrit.moofluids.common.proxy;

import com.robrit.moofluids.common.event.ConfigurationHandler;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;
import com.robrit.moofluids.common.util.ModInformation;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

import cpw.mods.fml.common.FMLCommonHandler;

public abstract class CommonProxy implements IProxy {

  @Override
  public void initContainableFluids() {
    for (final FluidContainerRegistry.FluidContainerData fluidContainerData : FluidContainerRegistry
        .getRegisteredFluidContainerData()) {
      if (fluidContainerData.filledContainer.getItem() != null) {
        final String fluidName = fluidContainerData.fluid.getFluid().getName();
        if (!EntityHelper.hasContainableFluid(fluidName)) {
          final Fluid containableFluid = fluidContainerData.fluid.getFluid();
          EntityHelper.setContainableFluid(fluidName, containableFluid);

          if (ModInformation.DEBUG_MODE) {
            LogHelper.info(fluidName + " has been added as an containable (i.e. bucketable) fluid");
          }
        }
      }
    }
  }

  @Override
  public void registerEventHandlers() {
    FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
  }
}

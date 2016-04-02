/*
 * CommonProxy.java
 *
 * Copyright (c) 2014-2016 TheRoBrit
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

import com.robrit.moofluids.common.MooFluids;
import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.entity.event.EntityChristmasCow;
import com.robrit.moofluids.common.entity.event.EntityNewYearsCow;
import com.robrit.moofluids.common.event.ConfigurationHandler;
import com.robrit.moofluids.common.event.EntitySpawnHandler;
import com.robrit.moofluids.common.ref.ConfigurationData;
import com.robrit.moofluids.common.ref.ModInformation;
import com.robrit.moofluids.common.util.DateHelper;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;

import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

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

    if (FluidRegistry.isUniversalBucketEnabled()) {
      for (final Fluid fluid : FluidRegistry.getBucketFluids()) {
        final String fluidName = fluid.getName();
        if (!EntityHelper.hasContainableFluid(fluidName)) {
          EntityHelper.setContainableFluid(fluidName, fluid);

          if (ModInformation.DEBUG_MODE) {
            LogHelper.info(fluidName + " has been added as an containable (i.e. bucketable) fluid");
          }
        }
      }
    }
  }

  @Override
  public void registerEventHandlers() {
    MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
    MinecraftForge.EVENT_BUS.register(new EntitySpawnHandler());
  }

  @Override
  public void registerEntities() {
    EntityRegistry.registerModEntity(EntityFluidCow.class, "EntityFluidCow",
                                     EntityHelper.getRegisteredEntityId(),
                                     MooFluids.getInstance(), 64, 1,
                                     true, 0xFFFFFF, 0xFFFFFF);

    for (final BiomeDictionary.Type biomeType : BiomeDictionary.Type.values()) {
      EntityRegistry.addSpawn(EntityFluidCow.class,
                              ConfigurationData.GLOBAL_FLUID_COW_SPAWN_RATE_VALUE, 1, 1,
                              EnumCreatureType.CREATURE,
                              BiomeDictionary.getBiomesForType(biomeType));
    }

    if (ConfigurationData.EVENT_ENTITIES_ENABLED_VALUE) {
      /* Checks if the current date is between the dates (12/16/2014) and (12/28/2014) */
      if (DateHelper.isDateBetweenEpochBoundaries(1418688000, 1419724800)) {
        EntityRegistry.registerModEntity(EntityChristmasCow.class, "EntityChristmasCow",
                                         EntityHelper.getRegisteredEntityId(),
                                         MooFluids.getInstance(), 64, 1,
                                         true, 0x228B22, 0xAE0505);

        EntityRegistry.addSpawn(EntityChristmasCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                BiomeDictionary.getBiomesForType(BiomeDictionary.Type.COLD));

        EntityRegistry.addSpawn(EntityChristmasCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST));

        EntityRegistry.addSpawn(EntityChristmasCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS));
      }
      /* Checks if the current date is between the dates (12/29/2014) and (01/02/2015) */
      if (DateHelper.isDateBetweenEpochBoundaries(1419811200, 1420156800)) {
        EntityRegistry.registerModEntity(EntityNewYearsCow.class, "EntityNewYearsCow",
                                         EntityHelper.getRegisteredEntityId(),
                                         MooFluids.getInstance(), 64, 1,
                                         true, 0xC0C0C0, 0xFFD700);

        EntityRegistry.addSpawn(EntityNewYearsCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                BiomeDictionary.getBiomesForType(BiomeDictionary.Type.LUSH));

        EntityRegistry.addSpawn(EntityNewYearsCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                BiomeDictionary.getBiomesForType(BiomeDictionary.Type.MAGICAL));

        EntityRegistry.addSpawn(EntityNewYearsCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST));

        EntityRegistry.addSpawn(EntityNewYearsCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS));
      }
    }
  }
}

/*
 * CommonProxy.java
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

package com.robrit.moofluids.common.proxy;

import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.entity.holiday.EntityChristmasCow;
import com.robrit.moofluids.common.entity.holiday.EntityEasterCow;
import com.robrit.moofluids.common.entity.holiday.EntityHalloweenCow;
import com.robrit.moofluids.common.entity.holiday.EntityNewYearsCow;
import com.robrit.moofluids.common.entity.holiday.EntityValentinesCow;
import com.robrit.moofluids.common.event.ConfigurationHandler;
import com.robrit.moofluids.common.event.EntitySpawnHandler;
import com.robrit.moofluids.common.plugins.theoneprobe.TheOneProbePlugin;
import com.robrit.moofluids.common.plugins.waila.WailaPlugin;
import com.robrit.moofluids.common.ref.ConfigurationData;
import com.robrit.moofluids.common.ref.ModInformation;
import com.robrit.moofluids.common.util.DateHelper;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;

import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;

import java.time.Month;

public abstract class CommonProxy implements IProxy {

  @Override
  public void initContainableFluids() {
    if (FluidRegistry.isUniversalBucketEnabled()) {
      if (FluidRegistry.getBucketFluids().size() > 0) {
        for (final Fluid fluid : FluidRegistry.getBucketFluids()) {
          final String fluidName = fluid.getName();
          if (!EntityHelper.hasContainableFluid(fluidName)) {
            EntityHelper.setContainableFluid(fluidName, fluid);

            if (ModInformation.DEBUG_MODE) {
              LogHelper.info(String.format("%s has been added as an containable (i.e. bucketable) fluid", fluidName));
            }
          }
        }
      } else {
        LogHelper.error("No registered fluids found");
      }
    } else {
      throw new UnsupportedOperationException("Forge UniversalBucket must be enabled");
    }
  }

  @Override
  public void registerEventHandlers() {
    MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
    MinecraftForge.EVENT_BUS.register(new EntitySpawnHandler());
  }

  @Override
  public void registerEntities() {
    if (EntityHelper.getContainableFluidsArray().length > 0) {
      EntityHelper.registerEntity(EntityFluidCow.class, EntityFluidCow.ENTITY_NAME,
                                  64, 1, true, 0xFFFFFF, 0xFFFFFF);
    }

    if (ConfigurationData.EVENT_ENTITIES_ENABLED_VALUE) {
      EntityHelper.registerEntityLootTable(EntityChristmasCow.ENTITY_NAME);
      EntityHelper.registerEntity(EntityChristmasCow.class, EntityChristmasCow.ENTITY_NAME,
                                  64, 1, true, 0x228B22, 0xAE0505);

      EntityHelper.registerEntityLootTable(EntityEasterCow.ENTITY_NAME);
      EntityHelper.registerEntity(EntityEasterCow.class, EntityEasterCow.ENTITY_NAME,
                                  64, 1, true, 0x8BEAAF, 0x83DDD6);

      EntityHelper.registerEntityLootTable(EntityHalloweenCow.ENTITY_NAME);
      EntityHelper.registerEntity(EntityHalloweenCow.class, EntityHalloweenCow.ENTITY_NAME,
                                  64, 1, true, 0x101010, 0xFF0000);

      EntityHelper.registerEntityLootTable(EntityNewYearsCow.ENTITY_NAME);
      EntityHelper.registerEntity(EntityNewYearsCow.class, EntityNewYearsCow.ENTITY_NAME,
                                  64, 1, true, 0xC0C0C0, 0xFFD700);

      EntityHelper.registerEntityLootTable(EntityValentinesCow.ENTITY_NAME);
      EntityHelper.registerEntity(EntityValentinesCow.class, EntityValentinesCow.ENTITY_NAME,
                                  64, 1, true, 0xDDDDDD, 0xFF3BC5);
    }
  }

  @Override
  public void registerEntitySpawns() {
    EntityHelper.addSpawnAllBiomes(EntityFluidCow.class, ConfigurationData.GLOBAL_FLUID_COW_SPAWN_RATE_VALUE,
                              4, 4, EnumCreatureType.CREATURE);

    if (ConfigurationData.EVENT_ENTITIES_ENABLED_VALUE) {
      /* Checks if the current date is between the dates (16/12) and (28/12) every year */
      if (DateHelper.isDateBetweenBoundaries(16, Month.DECEMBER.getValue(),
                                             28, Month.DECEMBER.getValue())) {
        EntityHelper.addSpawnFromType(EntityChristmasCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                      BiomeDictionary.Type.SNOWY,
                                      BiomeDictionary.Type.COLD,
                                      BiomeDictionary.Type.FOREST,
                                      BiomeDictionary.Type.PLAINS);
      }

      /* Checks if the current date is between the dates (10/04) and (24/04) every year */
      if (DateHelper.isDateBetweenBoundaries(10, Month.APRIL.getValue(),
                                             24, Month.APRIL.getValue())) {
        EntityHelper.addSpawnFromType(EntityEasterCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                      BiomeDictionary.Type.HILLS,
                                      BiomeDictionary.Type.FOREST,
                                      BiomeDictionary.Type.PLAINS);
      }

      /* Checks if the current date is between the dates (26/10) and (1/11) every year */
      if (DateHelper.isDateBetweenBoundaries(26, Month.OCTOBER.getValue(),
                                             1, Month.NOVEMBER.getValue())) {
        EntityHelper.addSpawnFromType(EntityHalloweenCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                      BiomeDictionary.Type.SPOOKY,
                                      BiomeDictionary.Type.DEAD,
                                      BiomeDictionary.Type.FOREST,
                                      BiomeDictionary.Type.PLAINS);
      }

      /* Checks if the current date is between the dates (29/12) and (04/01) every year */
      if (DateHelper.isDateBetweenBoundaries(29, Month.DECEMBER.getValue(),
                                             4, Month.JANUARY.getValue())) {
        EntityHelper.addSpawnFromType(EntityNewYearsCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                      BiomeDictionary.Type.LUSH,
                                      BiomeDictionary.Type.MAGICAL,
                                      BiomeDictionary.Type.FOREST,
                                      BiomeDictionary.Type.PLAINS);
      }

      /* Checks if the current date is between the dates (10/02) and (16/02) every year */
      if (DateHelper.isDateBetweenBoundaries(10, Month.FEBRUARY.getValue(),
                                             16, Month.FEBRUARY.getValue())) {
        EntityHelper.addSpawnFromType(EntityValentinesCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                      BiomeDictionary.Type.HOT,
                                      BiomeDictionary.Type.FOREST,
                                      BiomeDictionary.Type.PLAINS);
      }
    }
  }

  @Override
  public void registerPlugins() {
    if (Loader.isModLoaded("waila")) {
      WailaPlugin.init();
    }

    if (Loader.isModLoaded("theoneprobe")) {
      TheOneProbePlugin.init();
    }
  }
}

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

import com.robrit.moofluids.common.MooFluids;
import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.entity.holiday.EntityChristmasCow;
import com.robrit.moofluids.common.entity.holiday.EntityEasterCow;
import com.robrit.moofluids.common.entity.holiday.EntityHalloweenCow;
import com.robrit.moofluids.common.entity.holiday.EntityNewYearsCow;
import com.robrit.moofluids.common.entity.holiday.EntityValentinesCow;
import com.robrit.moofluids.common.event.ConfigurationHandler;
import com.robrit.moofluids.common.event.EntitySpawnHandler;
import com.robrit.moofluids.common.ref.ConfigurationData;
import com.robrit.moofluids.common.ref.ModInformation;
import com.robrit.moofluids.common.util.DateHelper;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class CommonProxy implements IProxy {

  @Override
  public void initContainableFluids() {
    for (final FluidContainerRegistry.FluidContainerData fluidContainerData :
        FluidContainerRegistry.getRegisteredFluidContainerData()) {
      if (fluidContainerData.filledContainer.getItem() != null) {
        final String fluidName = fluidContainerData.fluid.getFluid().getName();
        if (!EntityHelper.hasContainableFluid(fluidName)) {
          final Fluid fluid = fluidContainerData.fluid.getFluid();
          EntityHelper.setContainableFluid(fluidName, fluid);

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
                                     MooFluids.getInstance(), 64, 1, true, 0xFFFFFF, 0xFFFFFF);

    if (ConfigurationData.EVENT_ENTITIES_ENABLED_VALUE) {
      EntityHelper.registerEntityLootTable("EntityChristmasCow");
      EntityRegistry.registerModEntity(EntityChristmasCow.class, "EntityChristmasCow",
                                       EntityHelper.getRegisteredEntityId(),
                                       MooFluids.getInstance(), 64, 1, true, 0x228B22, 0xAE0505);

      EntityHelper.registerEntityLootTable("EntityEasterCow");
      EntityRegistry.registerModEntity(EntityEasterCow.class, "EntityEasterCow",
                                       EntityHelper.getRegisteredEntityId(),
                                       MooFluids.getInstance(), 64, 1, true, 0x8BEAAF, 0x83DDD6);

      EntityHelper.registerEntityLootTable("EntityHalloweenCow");
      EntityRegistry.registerModEntity(EntityHalloweenCow.class, "EntityHalloweenCow",
                                       EntityHelper.getRegisteredEntityId(),
                                       MooFluids.getInstance(), 64, 1, true, 0x101010, 0xFF0000);


      EntityHelper.registerEntityLootTable("EntityNewYearsCow");
      EntityRegistry.registerModEntity(EntityNewYearsCow.class, "EntityNewYearsCow",
                                       EntityHelper.getRegisteredEntityId(),
                                       MooFluids.getInstance(), 64, 1, true, 0xC0C0C0, 0xFFD700);

      EntityHelper.registerEntityLootTable("EntityValentinesCow");
      EntityRegistry.registerModEntity(EntityValentinesCow.class, "EntityValentinesCow",
                                       EntityHelper.getRegisteredEntityId(),
                                       MooFluids.getInstance(), 64, 1, true, 0xDDDDDD, 0xFF3BC5);
    }
  }

  @Override
  public void registerEntitySpawns() {
    final ArrayList<Biome> biomes = new ArrayList<Biome>();
    for (final BiomeDictionary.Type biomeType : BiomeDictionary.Type.values()) {
      biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(biomeType))); // Add biomes
    }

    EntityRegistry.addSpawn(EntityFluidCow.class,
                            ConfigurationData.GLOBAL_FLUID_COW_SPAWN_RATE_VALUE, 1, 1,
                            EnumCreatureType.CREATURE,
                            biomes.toArray(new Biome[biomes.size()]));
    biomes.clear(); // Reset biome list

    if (ConfigurationData.EVENT_ENTITIES_ENABLED_VALUE) {
      /* Checks if the current date is between the dates (16/12) and (28/12) every year */
      if (DateHelper.isDateBetweenBoundaries(16, Month.DECEMBER.getValue(),
                                             28, Month.DECEMBER.getValue())) {
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.COLD)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS)));

        EntityRegistry.addSpawn(EntityChristmasCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                biomes.toArray(new Biome[biomes.size()]));
        biomes.clear(); // Reset biome list
      }

      /* Checks if the current date is between the dates (10/04) and (24/04) every year */
      if (DateHelper.isDateBetweenBoundaries(10, Month.APRIL.getValue(),
                                             24, Month.APRIL.getValue())) {
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.HILLS)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS)));

        EntityRegistry.addSpawn(EntityEasterCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                biomes.toArray(new Biome[biomes.size()]));
        biomes.clear(); // Reset biome list
      }

      /* Checks if the current date is between the dates (26/10) and (1/11) every year */
      if (DateHelper.isDateBetweenBoundaries(26, Month.OCTOBER.getValue(),
                                             1, Month.NOVEMBER.getValue())) {
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.SPOOKY)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.DEAD)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS)));

        EntityRegistry.addSpawn(EntityHalloweenCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                biomes.toArray(new Biome[biomes.size()]));
        biomes.clear(); // Reset biome list
      }

      /* Checks if the current date is between the dates (29/12) and (04/01) every year */
      if (DateHelper.isDateBetweenBoundaries(29, Month.DECEMBER.getValue(),
                                             4, Month.JANUARY.getValue())) {
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.LUSH)));
        biomes
            .addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.MAGICAL)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS)));

        EntityRegistry.addSpawn(EntityNewYearsCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                biomes.toArray(new Biome[biomes.size()]));
        biomes.clear(); // Reset biome list
      }

      /* Checks if the current date is between the dates (10/02) and (16/02) every year */
      if (DateHelper.isDateBetweenBoundaries(10, Month.FEBRUARY.getValue(),
                                             16, Month.FEBRUARY.getValue())) {
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.HOT)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST)));
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS)));

        EntityRegistry.addSpawn(EntityValentinesCow.class, 8, 4, 4, EnumCreatureType.CREATURE,
                                biomes.toArray(new Biome[biomes.size()]));
        biomes.clear(); // Reset biome list
      }
    }
  }
}

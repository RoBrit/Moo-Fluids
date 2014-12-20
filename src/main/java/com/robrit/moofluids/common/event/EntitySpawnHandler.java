/*
 * EntitySpawnHandler.java
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

package com.robrit.moofluids.common.event;


import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.entity.EntityTypeData;
import com.robrit.moofluids.common.util.EntityHelper;

import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntitySpawnHandler {

  private static final int SPAWN_LOWER_BOUNDARY = 3;
  private static final int SPAWN_UPPER_BOUNDARY = 5;
  private static Fluid[] fluids = new Fluid[0];

  @SubscribeEvent
  public void onEntityConstruction(EntityEvent.EntityConstructing event) {
    if (event.entity instanceof EntityFluidCow) {
      final EntityFluidCow entityFluidCow = (EntityFluidCow) event.entity;

      if (entityFluidCow.getEntityFluid() == null) {
        final Random random = new Random();
        final Fluid entityFluid = getEntityFluid(getPossibleEntityFluids(random), random);
        entityFluidCow.setEntityFluid(entityFluid);
      }
    }
  }

  private Fluid getEntityFluid(final ArrayList<Fluid> possibleEntityFluids, final Random random) {
    int currentHighestSpawnChance = 0;
    Fluid currentEntityFluid = null;

    for (final Fluid possibleEntityFluid : possibleEntityFluids) {
      final EntityTypeData
          entityTypeData = EntityHelper.getEntityData(possibleEntityFluid.getName());
      final int currentSpawnChance = random.nextInt(entityTypeData.getSpawnRate());

      if (currentSpawnChance > currentHighestSpawnChance || currentEntityFluid == null) {
        currentEntityFluid = possibleEntityFluid;
        currentHighestSpawnChance = currentSpawnChance;
      }
    }

    return currentEntityFluid;
  }

  private ArrayList<Fluid> getPossibleEntityFluids(final Random random) {
    if (fluids.length < 1) {
      fluids = EntityHelper.getContainableFluids().values().toArray(fluids);
    }

    final int
        entityListSize =
        random.nextInt(SPAWN_UPPER_BOUNDARY - SPAWN_LOWER_BOUNDARY) + SPAWN_LOWER_BOUNDARY;
    int currentEntityCount = 0;
    ArrayList<Fluid> possibleEntityFluids = new ArrayList<Fluid>();

    while (currentEntityCount < entityListSize) {
      final int fluidIndex = random.nextInt(fluids.length - 1);
      possibleEntityFluids.add(fluids[fluidIndex]);
      currentEntityCount++;
    }

    return possibleEntityFluids;
  }
}

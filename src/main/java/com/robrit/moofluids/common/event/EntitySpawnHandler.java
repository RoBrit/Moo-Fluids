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
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;
import com.robrit.moofluids.common.util.ModInformation;

import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntitySpawnHandler {

  private static final int SPAWN_LOWER_BOUNDARY = 3;
  private static final int SPAWN_UPPER_BOUNDARY = 5;
  private static final Fluid[] containableFluids = EntityHelper.getContainableFluidsArray();
  private static final Random random = new Random();

  @SubscribeEvent
  public void onEntityConstruction(EntityEvent.EntityConstructing event) {
    if (event.entity instanceof EntityFluidCow) {
      final EntityFluidCow entityFluidCow = (EntityFluidCow) event.entity;

      if (entityFluidCow.getEntityFluid() == null) {
        final Fluid entityFluid = getEntityFluid();
        entityFluidCow.setEntityFluid(entityFluid);
      }
    }
  }

  private Fluid getEntityFluid() {
    if (containableFluids.length > 0) {
      Fluid currentEntityFluid = containableFluids[0];

      if (containableFluids.length > 1) {
        final int
            fluidsToCheck =
            random.nextInt(SPAWN_UPPER_BOUNDARY - SPAWN_LOWER_BOUNDARY) + SPAWN_LOWER_BOUNDARY;

        final Fluid[] possibleEntityFluids = new Fluid[fluidsToCheck];

        for (int currentFluidIndex = 0; currentFluidIndex < fluidsToCheck; currentFluidIndex++) {
          possibleEntityFluids[currentFluidIndex] =
              containableFluids[random.nextInt(containableFluids.length)];
          if (ModInformation.DEBUG_MODE) {
            LogHelper.info("POSSIBLE SPAWN FLUID: " + possibleEntityFluids[currentFluidIndex].getName());
          }
        }

        int highestSpawnChance = 0;
        for (final Fluid possibleEntityFluid : possibleEntityFluids) {
          final int
              spawnRate =
              EntityHelper.getEntityData(possibleEntityFluid.getName()).getSpawnRate();
          final boolean
              isSpawnable =
              EntityHelper.getEntityData(possibleEntityFluid.getName()).isSpawnable();

          if (!isSpawnable || spawnRate <= 0) {
            continue;
          }

          final int currentSpawnChance = random.nextInt(spawnRate);

          if (currentSpawnChance > highestSpawnChance) {
            currentEntityFluid = possibleEntityFluid;
            highestSpawnChance = currentSpawnChance;
          }
        }
      }

      return currentEntityFluid;
    }

    return null;
  }
}

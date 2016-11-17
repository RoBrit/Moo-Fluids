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
      EntityTypeData entityData = EntityHelper.getEntityData(currentEntityFluid.getName());

      if (!entityData.isSpawnable() || entityData.getSpawnRate() <= 0) {
        currentEntityFluid = null;
      }

      if (containableFluids.length > 1) {
        boolean cowSpawned = false;
        int cumulatedSpawnChances = EntityHelper.getCumulatedSpawnChances();
        int addedSpawnChances = 0;

        while (!cowSpawned) {
          int currentRandomChance = random.nextInt(cumulatedSpawnChances);

          for (Fluid currentFluid : containableFluids) {
            EntityTypeData currentEntityData = EntityHelper.getEntityData(currentFluid.getName());

            if (currentEntityData == null) {
              continue;
            }

            final boolean isSpawnable = currentEntityData.isSpawnable();
            final int spawnRate = currentEntityData.getSpawnRate();

            if (!isSpawnable || spawnRate <= 0) {
              continue;
            }

            addedSpawnChances += spawnRate;
            if (addedSpawnChances >= currentRandomChance) {
              cowSpawned = true;
              currentEntityFluid = currentFluid;
            }
          }
        }
      }

      return currentEntityFluid;
    }

    return null;
  }
}

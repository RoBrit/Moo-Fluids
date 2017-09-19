/*
 * EntitySpawnHandler.java
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

package com.robrit.moofluids.common.event;


import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.entity.EntityTypeData;
import com.robrit.moofluids.common.util.EntityHelper;

import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class EntitySpawnHandler {

  private static final Fluid[] SPAWNABLE_FLUIDS = getSpawnableFluids();
  private static final double CUMULATIVE_SPAWN_WEIGHT = getCumulativeSpawnWeight();

  @SubscribeEvent
  public void onEntityConstruction(EntityEvent.EntityConstructing event) {
    if (event.getEntity() instanceof EntityFluidCow) {
      final EntityFluidCow entityFluidCow = (EntityFluidCow) event.getEntity();

      if (entityFluidCow.getEntityFluid() == null) 
	  {
		Fluid temp = getEntityFluid();
		if(temp != null)
			entityFluidCow.setEntityFluid(temp);
		else
			entityFluidCow.setEntityFluid(SPAWNABLE_FLUIDS[0]);
      }
    }
  }

  private static Fluid getEntityFluid() {
    if (SPAWNABLE_FLUIDS.length > 0) {
      // Weighted random chance for activation
      double activationWeight = Math.random() * CUMULATIVE_SPAWN_WEIGHT;
      // Accumulated chance from iteration
      double accumulatedWeight = 0.0;

      for (final Fluid potentialEntityFluid : SPAWNABLE_FLUIDS) {
        final EntityTypeData entityData =
            EntityHelper.getEntityData(potentialEntityFluid.getName());

        if (entityData != null) 
		{
          accumulatedWeight += entityData.getSpawnRate();
		  if (accumulatedWeight >= activationWeight) {
			return potentialEntityFluid;
		  }
        }
      }
    }

    return null;
  }

  private static Fluid[] getSpawnableFluids() {
    final ArrayList<Fluid> spawnableFluids = new ArrayList<Fluid>();

    for (final Fluid fluid : EntityHelper.getContainableFluidsArray()) {
      final EntityTypeData entityData = EntityHelper.getEntityData(fluid.getName());

      if (entityData == null) {
        continue;
      }

      final boolean isSpawnable = entityData.isSpawnable();
      final int spawnRate = entityData.getSpawnRate();

      if (!isSpawnable || spawnRate <= 0) {
        continue;
      }
	  
      spawnableFluids.add(fluid);
    }

    return spawnableFluids.toArray(new Fluid[spawnableFluids.size()]);
  }

  private static double getCumulativeSpawnWeight() {
    double cumulativeSpawnWeight = 0.0;

    for (final Fluid spawnableFluid : SPAWNABLE_FLUIDS) {
      final EntityTypeData entityData = EntityHelper.getEntityData(spawnableFluid.getName());

      if (entityData == null) {
        continue;
      }

      cumulativeSpawnWeight += entityData.getSpawnRate();
    }

    return cumulativeSpawnWeight;
  }
}

/*
 * EntityAspectRegistry.java 
 *
 * Copyright (c) 2015 TheRoBrit
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

package com.robrit.moofluids.common.plugins.thaumcraft;

import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.util.EntityHelper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class AspectRegistry {

  public static void registerEntityAspects() {
    for (final Fluid fluid : EntityHelper.getContainableFluidsArray()) {
      AspectList entityAspects = new AspectList();
      final ItemStack fluidBlockItemStack = new ItemStack(fluid.getBlock());
      final boolean hasAspects = ThaumcraftApi.exists(fluidBlockItemStack.getItem(),
                                                      fluidBlockItemStack.getItemDamage());
      if (fluid.canBePlacedInWorld() && hasAspects) {
        entityAspects = ThaumcraftApiHelper.getObjectAspects(fluidBlockItemStack);
      } else {
        entityAspects = new AspectList().add(Aspect.EARTH, 3).add(Aspect.BEAST, 3);
        if (fluid.isGaseous()) {
          entityAspects.add(Aspect.AIR, 3);
        }
        if (fluid.getLuminosity() > 0) {
          entityAspects.add(Aspect.LIGHT, 3);
        }
        if (fluid.getTemperature() >= FluidRegistry.LAVA.getTemperature()) {
          entityAspects.add(Aspect.FIRE, 3);
        }
        if (fluid.getDensity() >= FluidRegistry.WATER.getDensity() * 3) {
          entityAspects.add(Aspect.TRAP, 3);
        }
      }
      final ThaumcraftApi.EntityTagsNBT entityIdentifierTag =
          new ThaumcraftApi.EntityTagsNBT(EntityFluidCow.NBT_TAG_FLUID_NAME, fluid.getName());
      
      ThaumcraftApi.registerEntityTag("EntityFluidCow", entityAspects, entityIdentifierTag);
    }
  }
}

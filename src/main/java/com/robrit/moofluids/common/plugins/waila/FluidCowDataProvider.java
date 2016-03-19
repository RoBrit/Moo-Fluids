/*
 * FluidCowDataProvider.java
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

package com.robrit.moofluids.common.plugins.waila;

import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.entity.EntityTypeData;
import com.robrit.moofluids.common.ref.UnlocalizedStrings;
import com.robrit.moofluids.common.util.LocalizationHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;

public class FluidCowDataProvider implements IWailaEntityProvider {

  @Override
  public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
    return null;
  }

  @Override
  public List<String> getWailaHead(Entity entity, List<String> currenttip,
                                   IWailaEntityAccessor accessor, IWailaConfigHandler config) {
    return currenttip;
  }

  @Override
  public List<String> getWailaBody(Entity entity, List<String> currenttip,
                                   IWailaEntityAccessor accessor, IWailaConfigHandler config) {
    if (accessor.getEntity() instanceof EntityFluidCow) {
      final EntityFluidCow entityFluidCow = (EntityFluidCow) accessor.getEntity();
      final EntityTypeData entityTypeData = entityFluidCow.getEntityTypeData();
      final FluidStack fluidStack = new FluidStack(entityFluidCow.getEntityFluid(), 0);

      currenttip.add(String.format(
          LocalizationHelper.localize(UnlocalizedStrings.FLUID_TOOLTIP),
          EnumChatFormatting.WHITE +
          entityFluidCow.getEntityFluid().getLocalizedName(fluidStack) +
          EnumChatFormatting.RESET));

      currenttip.add(String.format(
          LocalizationHelper.localize(UnlocalizedStrings.CURRENT_COOLDOWN_TOOLTIP),
          EnumChatFormatting.WHITE +
          String.valueOf(entityFluidCow.getCurrentUseCooldown())));

      currenttip.add(String.format(
          LocalizationHelper.localize(UnlocalizedStrings.BREEDING_ITEM_TOOLTIP),
          EnumChatFormatting.WHITE +
          entityTypeData.getBreedingItem().getDisplayName() +
          EnumChatFormatting.RESET));
    }

    return currenttip;
  }

  @Override
  public List<String> getWailaTail(Entity entity, List<String> currenttip,
                                   IWailaEntityAccessor accessor, IWailaConfigHandler config) {
    return currenttip;
  }

  @Override
  public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent,
                                   NBTTagCompound tag, World world) {
    return null;
  }
}
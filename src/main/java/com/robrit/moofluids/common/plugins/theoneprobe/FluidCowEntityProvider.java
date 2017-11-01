/*
 * FluidCowEntityProvider.java
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

package com.robrit.moofluids.common.plugins.theoneprobe;

import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.ref.ModInformation;
import com.robrit.moofluids.common.ref.UnlocalizedStrings;
import com.robrit.moofluids.common.util.LocalizationHelper;
import com.robrit.moofluids.common.util.TooltipHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;

public class FluidCowEntityProvider implements IProbeInfoEntityProvider {

  @Override
  public String getID() {
    return ModInformation.MOD_ID + "." + EntityFluidCow.ENTITY_NAME;
  }

  @Override
  public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player,
                                 World world, Entity entity, IProbeHitEntityData data) {
    if (entity instanceof EntityFluidCow) {
      final EntityFluidCow entityFluidCow = (EntityFluidCow) entity;
      final FluidStack fluidStack = new FluidStack(entityFluidCow.getEntityFluid(), 0);

      probeInfo.horizontal().text(String.format(
          TextFormatting.WHITE +
          LocalizationHelper.localize(UnlocalizedStrings.FLUID_TOOLTIP),
          TextFormatting.AQUA +
          entityFluidCow.getEntityFluid().getLocalizedName(fluidStack)));

      probeInfo.horizontal().text(String.format(
          TextFormatting.WHITE +
          LocalizationHelper.localize(UnlocalizedStrings.NEXT_USE_TOOLTIP),
          TextFormatting.AQUA +
          TooltipHelper.getTimeUntilNextUse(entityFluidCow.getNextUseCooldown() / 20)));
    }
  }
}
package com.robrit.moofluids.common.plugins.theoneprobe;

import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.plugins.waila.FluidCowEntityProvider;
import com.robrit.moofluids.common.ref.ModInformation;
import com.robrit.moofluids.common.ref.UnlocalizedStrings;
import com.robrit.moofluids.common.util.LocalizationHelper;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
public class CowInfoProvider implements IProbeInfoEntityProvider {

	@Override
	public String getID() {
		return ModInformation.MOD_ID + ".FluidCow";
	}

	@Override
	public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
			Entity entity, IProbeHitEntityData data) {
		if (entity instanceof EntityFluidCow) {
			EntityFluidCow cow = (EntityFluidCow)entity;
			FluidStack fluidStack = new FluidStack(cow.getEntityFluid(), 0);
			IProbeInfo row1 = probeInfo.horizontal();

			row1.text(String.format(
			          TextFormatting.WHITE +
			          LocalizationHelper.localize(UnlocalizedStrings.FLUID_TOOLTIP),
			          TextFormatting.AQUA +
			          cow.getEntityFluid().getLocalizedName(fluidStack)));
			
			IProbeInfo row2 = probeInfo.horizontal();

			row2.text(String.format(
			          TextFormatting.WHITE +
			          LocalizationHelper.localize(UnlocalizedStrings.NEXT_USE_TOOLTIP),
			          TextFormatting.AQUA +
			          FluidCowEntityProvider.getTimeUntilNextUse(cow.getNextUseCooldown() / 20)));
			
		}
	}

}

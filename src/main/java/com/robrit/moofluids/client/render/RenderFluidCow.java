/*
 * RenderFluidCow.java
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

package com.robrit.moofluids.client.render;

import com.robrit.moofluids.common.entity.EntityFluidCow;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.entity.EntityLivingBase;

public class RenderFluidCow extends RenderCow {

  public RenderFluidCow(ModelBase modelBase, float par2) {
    super(modelBase, par2);
  }

  @Override
  protected int getColorMultiplier(EntityLivingBase entityLivingBase, float par2, float par3) {
    return ((EntityFluidCow) entityLivingBase).getOverlay();
  }
}

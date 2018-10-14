/*
 * RenderFluidCow.java
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

package com.robrit.moofluids.client.render;

import com.robrit.moofluids.common.entity.EntityFluidCow;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderManager;

import net.minecraft.entity.passive.EntityCow;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFluidCow extends RenderCow {

  public RenderFluidCow(RenderManager renderManager) {
    super(renderManager);
  }

  @Override
  protected int getColorMultiplier(EntityCow entityLivingBase, float lightBrightness, float partialTickTime) {
    return ((EntityFluidCow) entityLivingBase).getOverlay();
  }

  public static class Factory implements IRenderFactory<EntityFluidCow> {
    @Override
    public Render<? super EntityFluidCow> createRenderFor(RenderManager renderManager) {
      return new RenderFluidCow(renderManager);
    }
  }
}

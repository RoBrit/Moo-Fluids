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
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderFluidCow extends RenderCow {

  private static final ResourceLocation cowTextures = new ResourceLocation("textures/entity/cow/cow.png");

  public RenderFluidCow(ModelBase modelBase, float par2) {
    super(modelBase, par2);
  }

  /**
   * Returns the location of an entity's texture. Doesn't seem to be called unless you call
   * Render.bindEntityTexture.
   *
   * @param entityFluidCow instance of FluidCow to get texture for
   */
  protected ResourceLocation getEntityTexture(EntityFluidCow entityFluidCow) {
    final String fluidName = entityFluidCow.getEntityFluid().getName();

      return cowTextures; //TODO: FIXME!
//    return EntityHelper.getEntityData(fluidName).getEntityTexture();
  }

  /**
   * Returns the location of an entity's texture. Doesn't seem to be called unless you call
   * Render.bindEntityTexture.
   *
   * @param entity instance of Entity to get texture for
   */
  protected ResourceLocation getEntityTexture(Entity entity) {
    return this.getEntityTexture((EntityFluidCow) entity);
  }
}

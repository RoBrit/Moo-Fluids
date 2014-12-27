/*
 * RenderEventCow.java
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

import com.robrit.moofluids.common.entity.INamedEntity;
import com.robrit.moofluids.common.util.ModInformation;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEventCow extends RenderCow {

  public RenderEventCow(ModelBase modelBase, float shadowSize) {
    super(modelBase, shadowSize);
  }

  @Override
  protected ResourceLocation getEntityTexture(Entity entity) {
    return new ResourceLocation(ModInformation.MOD_ID.toLowerCase(),
                                "textures/entity/" +
                                ((INamedEntity) entity).getEntityName() + ".png");
  }
}

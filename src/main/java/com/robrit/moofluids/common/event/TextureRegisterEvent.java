/*
 * TextureRegisterEvent.java
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

import com.robrit.moofluids.common.util.ColorHelper;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;
import com.robrit.moofluids.common.util.ModInformation;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TextureRegisterEvent {

  private static final byte TEXTURE_MAP_ID_BLOCK = 0;

  @SubscribeEvent
  public void onTextureStitch(TextureStitchEvent.Post event) {
    if (event.map.getTextureType() == TEXTURE_MAP_ID_BLOCK) {
      for (final Fluid fluid : EntityHelper.getContainableFluids().values()) {
        final String fluidName = fluid.getName();
        final int[][]
            textureData2d =
            event.map.getAtlasSprite(fluid.getIcon().getIconName()).getFrameTextureData(1);

        final Color meanColour = ColorHelper.getMeanColour(textureData2d);

        if (ModInformation.DEBUG_MODE) {
          LogHelper.info("Successfully added colour overlay for " + fluid.getLocalizedName());
          LogHelper.info(String.format("%s average colour R:%d, G:%d, B:%d",
                                       fluid.getLocalizedName(),
                                       meanColour.getRed(),
                                       meanColour.getGreen(),
                                       meanColour.getBlue()));
        }
      }
    }
  }
}

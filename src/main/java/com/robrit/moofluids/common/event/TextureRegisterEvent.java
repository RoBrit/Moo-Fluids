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

import com.robrit.moofluids.common.entity.EntityTypeData;
import com.robrit.moofluids.common.util.ColorHelper;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;
import com.robrit.moofluids.common.util.ModInformation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TextureRegisterEvent {

  private static final byte TEXTURE_MAP_ID_BLOCK = 0;
  private static final String SEPARATOR = "/";
  private static final String TEXTURE_LOCATION = "textures/entity/cow";
  private static final String BASE_TEXTURE = TEXTURE_LOCATION + SEPARATOR + "cow.png";

  public static void modifyTexture(final Color colourOverlay, final String fluidName) {
    try {

      final BufferedImage
          image =
          ImageIO.read(Minecraft.getMinecraft().getResourceManager()
                           .getResource(new ResourceLocation(BASE_TEXTURE))
                           .getInputStream());

      final File outputPath = new File(Minecraft.getMinecraft().mcDataDir + SEPARATOR +
                                       "assets" + SEPARATOR,
                                       ModInformation.MOD_ID.toLowerCase() + SEPARATOR +
                                       TEXTURE_LOCATION + SEPARATOR +
                                       fluidName + ".png");

      if (!outputPath.exists()) {
        int width = image.getWidth();
        int height = image.getHeight();

        Graphics g = image.getGraphics();
        g.setColor(colourOverlay);

        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {
            final int pixel = image.getRGB(x, y);
            if (!(pixel >> 24 == 0)) {
              g.fillRect(x, y, 1, 1);
            }
          }
        }

        if (!outputPath.getParentFile().exists() && !outputPath.getParentFile().mkdirs()) {
          return;
        }

        ImageIO.write(image, "png", outputPath);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void registerTexture(final String fluidName) {
    final ResourceLocation entityTexture = new ResourceLocation(ModInformation.MOD_ID.toLowerCase(),
                                                                TEXTURE_LOCATION + SEPARATOR +
                                                                fluidName + ".png");

    final EntityTypeData entityTypeData = EntityHelper.getEntityData(fluidName);
    entityTypeData.setTexture(entityTexture);
    EntityHelper.setEntityData(fluidName, entityTypeData);
  }

  @SubscribeEvent
  public void onTextureStitch(TextureStitchEvent.Post event) {
    if (event.map.getTextureType() == TEXTURE_MAP_ID_BLOCK) {
      for (final Fluid fluid : EntityHelper.getContainableFluids().values()) {
        final String fluidName = fluid.getName();
        final int[][]
            textureData2d =
            event.map.getAtlasSprite(fluid.getIcon().getIconName()).getFrameTextureData(1);

        final Color meanColour = ColorHelper.getMeanColour(textureData2d);

        final EntityTypeData entityTypeData = EntityHelper.getEntityData(fluidName);
        entityTypeData.setOverlay(meanColour);
        EntityHelper.setEntityData(fluidName, entityTypeData);

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

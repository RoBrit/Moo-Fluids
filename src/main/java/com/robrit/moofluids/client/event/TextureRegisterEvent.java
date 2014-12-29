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

package com.robrit.moofluids.client.event;

import com.robrit.moofluids.common.entity.EntityTypeData;
import com.robrit.moofluids.common.util.ColorHelper;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;
import com.robrit.moofluids.common.util.ModInformation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
        try {
          final String fluidName = fluid.getName();
          final IIcon fluidIcon = fluid.getIcon();
          int[][] textureData2d = new int[fluidIcon.getIconWidth()][fluidIcon.getIconHeight()];
          boolean hasColour = false;

          if (event.map.getAtlasSprite(fluidIcon.getIconName()) != null) {
            textureData2d =
                event.map.getAtlasSprite(fluidIcon.getIconName()).getFrameTextureData(0);
            hasColour = true;
          }

          final EntityTypeData entityTypeData = EntityHelper.getEntityData(fluidName);

          if (hasColour) {
            final Color meanColour = ColorHelper.getMeanColour(textureData2d);
            entityTypeData.setOverlay(new Color(meanColour.getRed(),
                                                meanColour.getGreen(),
                                                meanColour.getBlue(),
                                                128).getRGB());
          } else {
            final int fluidColor = fluid.getColor();
            entityTypeData.setOverlay(new Color((fluidColor) & 0xFF,
                                                (fluidColor >> 8) & 0xFF,
                                                (fluidColor >> 16) & 0xFF,
                                                128).getRGB());
          }

          EntityHelper.setEntityData(fluidName, entityTypeData);

          if (ModInformation.DEBUG_MODE) {
            LogHelper.info("Successfully added colour overlay for " + fluid.getName());
            LogHelper.info("Successfully added colour overlay for " + fluidIcon.getIconName());
          }
        } catch (final Exception ex) {
          LogHelper.error("Encountered an issue when attempting to manipulate texture");
          ex.printStackTrace();
        }
      }
    }
  }
}

/*
 * ColorHelper.java
 *
 * Copyright (c) 2014-2016 TheRoBrit
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

package com.robrit.moofluids.common.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorHelper {

  private static final int COLOR_COMPONENT_MAX_VALUE = 255;

  public static Color getMeanColour(int[] dataArray) {
    short[] allRedStored = new short[dataArray.length];
    short[] allGreenStored = new short[dataArray.length];
    short[] allBlueStored = new short[dataArray.length];

    for (int pixelIndex = 0; pixelIndex < dataArray.length; pixelIndex++) {
      allRedStored[pixelIndex] = (short) (dataArray[pixelIndex] >> 16 & 255);
      allGreenStored[pixelIndex] = (short) (dataArray[pixelIndex] >> 8 & 255);
      allBlueStored[pixelIndex] = (short) (dataArray[pixelIndex] & 255);
    }

    int aggregateRed = 0;
    int aggregateGreen = 0;
    int aggregateBlue = 0;

    for (int colourIndex = 0; colourIndex < dataArray.length; colourIndex++) {
      aggregateRed += allRedStored[colourIndex];
      aggregateGreen += allGreenStored[colourIndex];
      aggregateBlue += allBlueStored[colourIndex];
    }

    short meanRed = COLOR_COMPONENT_MAX_VALUE;
    short meanGreen = COLOR_COMPONENT_MAX_VALUE;
    short meanBlue = COLOR_COMPONENT_MAX_VALUE;

    meanRed = (short) (aggregateRed / (allRedStored.length > 0 ? allRedStored.length : 1));
    meanGreen = (short) (aggregateGreen / (allGreenStored.length > 0 ? allGreenStored.length : 1));
    meanBlue = (short) (aggregateBlue / (allBlueStored.length > 0 ? allBlueStored.length : 1));

    return new Color(meanRed, meanGreen, meanBlue, 128);
  }

  public static Color getMeanColour(int[][] data2dArray) {
    final List<Integer> list = new ArrayList<Integer>();

    for (int dataIndexX = 0; dataIndexX < data2dArray.length; dataIndexX++) {
      for (int dataIndexY = 0; dataIndexY < data2dArray[dataIndexX].length; dataIndexY++) {
        list.add(data2dArray[dataIndexX][dataIndexY]);
      }
    }

    int[] data1dArray = new int[list.size()];

    for (int dataIndex = 0; dataIndex < data1dArray.length; dataIndex++) {
      data1dArray[dataIndex] = list.get(dataIndex);
    }

    return getMeanColour(data1dArray);
  }
}
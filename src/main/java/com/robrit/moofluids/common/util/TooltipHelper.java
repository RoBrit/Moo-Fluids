/*
 * TooltipHelper.java
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

package com.robrit.moofluids.common.util;

import com.robrit.moofluids.common.ref.UnlocalizedStrings;

public class TooltipHelper {
  public static String getTimeUntilNextUse(int totalSeconds) {

    final int MINUTES_IN_AN_HOUR = 60;
    final int SECONDS_IN_A_MINUTE = 60;

    final int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
    final int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
    final int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
    final int hours = totalMinutes / MINUTES_IN_AN_HOUR;

    final StringBuilder formattedTime = new StringBuilder();

    if (hours > 0) {
      formattedTime.append(hours).append(" ");

      if (hours > 1) {
        formattedTime.append(LocalizationHelper.localize(UnlocalizedStrings.TIME_HOURS));
      } else {
        formattedTime.append(LocalizationHelper.localize(UnlocalizedStrings.TIME_HOUR));
      }

      formattedTime.append(" ");
    }

    if (minutes > 0) {
      formattedTime.append(minutes).append(" ");

      if (minutes > 1) {
        formattedTime.append(LocalizationHelper.localize(UnlocalizedStrings.TIME_MINUTES));
      } else {
        formattedTime.append(LocalizationHelper.localize(UnlocalizedStrings.TIME_MINUTE));
      }

      formattedTime.append(" ");
    }

    if (seconds > 0) {
      formattedTime.append(seconds).append(" ");

      if (seconds > 1) {
        formattedTime.append(LocalizationHelper.localize(UnlocalizedStrings.TIME_SECONDS));
      } else {
        formattedTime.append(LocalizationHelper.localize(UnlocalizedStrings.TIME_SECOND));
      }
    }

    if (hours == 0 && minutes == 0 && seconds == 0) {
      formattedTime.append(LocalizationHelper.localize(UnlocalizedStrings.TIME_NOW));
    }

    return formattedTime.substring(0, 1).toUpperCase() + formattedTime.substring(1).toLowerCase();
  }
}
/*
 * LocalizationHelper.java
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

package com.robrit.moofluids.common.util;

import net.minecraft.util.StatCollector;

public class LocalizationHelper {

  private static final String PREFIX = "moofluids.";

  public static String localize(final String unlocalizedString) {
    return localize(unlocalizedString, true);
  }

  public static String localize(String unlocalizedString, final boolean prependPrefix) {
    if (prependPrefix) {
      unlocalizedString = PREFIX + unlocalizedString;
    }

    return StatCollector.translateToLocal(unlocalizedString);
  }
}
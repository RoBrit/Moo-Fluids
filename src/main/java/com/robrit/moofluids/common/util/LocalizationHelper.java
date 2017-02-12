/*
 * LocalizationHelper.java
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

import com.robrit.moofluids.common.ref.ModInformation;

import net.minecraft.util.text.translation.I18n;

public class LocalizationHelper {

  private static final String PREFIX = ModInformation.MOD_ID.toLowerCase() + ".";

  public static String localize(final String unlocalizedString) {
    return localize(unlocalizedString, true);
  }

  public static String localize(final String unlocalizedString, final boolean prependPrefix) {
    return prependPrefix ? I18n.translateToLocal(PREFIX + unlocalizedString)
                         : I18n.translateToLocal(unlocalizedString);
  }
}
/*
 * EntityHelper.java
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

import net.minecraftforge.fluids.Fluid;

import java.util.TreeMap;

public class EntityHelper {

  private static TreeMap<String, Fluid> containableFluids = new TreeMap<String, Fluid>();

  public static Fluid getContainableFluid(final String fluidName) {
    if (containableFluids.containsKey(fluidName)) {
      return containableFluids.get(fluidName);
    }

    return null;
  }

  public static void setContainableFluid(final String fluidName, final Fluid fluid) {
    containableFluids.put(fluidName, fluid);
  }

  public static boolean hasContainableFluid(final String fluidName) {
    return containableFluids.containsKey(fluidName);
  }
}
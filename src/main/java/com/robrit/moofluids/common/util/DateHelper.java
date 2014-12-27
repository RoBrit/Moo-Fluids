/*
 * DateHelper.java
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

import java.util.Date;

public class DateHelper {

  private static final int MILLIS = 1000;

  public static boolean isDateBetweenDateBoundaries(final Date lowerBoundaryDate,
                                                    final Date upperBoundaryDate) {
    final Date currentDate = new Date();

    final Date testDate = new Date(11, 11, 11);

    return currentDate.after(lowerBoundaryDate) && currentDate.before(upperBoundaryDate);
  }

  public static boolean isDateBetweenEpochBoundaries(final long lowerBoundaryEpoch,
                                                     final long upperBoundaryEpoch) {
    final Date lowerBoundaryDate = new Date(lowerBoundaryEpoch * MILLIS);
    final Date upperBoundaryDate = new Date(upperBoundaryEpoch * MILLIS);

    return isDateBetweenDateBoundaries(lowerBoundaryDate, upperBoundaryDate);
  }
}

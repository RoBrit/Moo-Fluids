/*
 * DateHelper.java
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

import java.util.Calendar;

public class DateHelper {

  public static boolean isDateBetweenBoundaries(final int lowerBoundaryDay,
                                                final int lowerBoundaryMonth,
                                                final int lowerBoundaryYear,
                                                final int upperBoundaryDay,
                                                final int upperBoundaryMonth,
                                                final int upperBoundaryYear) {
    final Calendar currentTime = Calendar.getInstance();
    final Calendar upperBoundaryDate = Calendar.getInstance();
    final Calendar lowerBoundaryDate = Calendar.getInstance();

    lowerBoundaryDate.set(Calendar.MILLISECOND, 0);
    lowerBoundaryDate.set(Calendar.SECOND, 0);
    lowerBoundaryDate.set(Calendar.MINUTE, 0);
    lowerBoundaryDate.set(Calendar.HOUR, 0);
    lowerBoundaryDate.set(Calendar.DAY_OF_MONTH, lowerBoundaryDay);
    lowerBoundaryDate.set(Calendar.MONTH, lowerBoundaryMonth - 1); // Months are zero bound
    lowerBoundaryDate.set(Calendar.YEAR, lowerBoundaryYear);

    upperBoundaryDate.set(Calendar.MILLISECOND, 0);
    upperBoundaryDate.set(Calendar.SECOND, 0);
    upperBoundaryDate.set(Calendar.MINUTE, 0);
    upperBoundaryDate.set(Calendar.HOUR, 0);
    upperBoundaryDate.set(Calendar.DAY_OF_MONTH, upperBoundaryDay);
    upperBoundaryDate.set(Calendar.MONTH, upperBoundaryMonth - 1); // Months are zero bound
    upperBoundaryDate.set(Calendar.YEAR, upperBoundaryYear);

    return currentTime.after(lowerBoundaryDate) && currentTime.before(upperBoundaryDate);
  }

  public static boolean isDateBetweenBoundaries(final int lowerBoundaryDay,
                                                final int lowerBoundaryMonth,
                                                final int upperBoundaryDay,
                                                final int upperBoundaryMonth) {
    final Calendar currentTime = Calendar.getInstance();
    return isDateBetweenBoundaries(lowerBoundaryDay, lowerBoundaryMonth,
                                   currentTime.get(Calendar.YEAR),
                                   upperBoundaryDay, upperBoundaryMonth,
                                   currentTime.get(Calendar.YEAR));
  }

  public static boolean isDateBetweenBoundaries(final int lowerBoundaryDay,
                                                final int upperBoundaryDay) {
    final Calendar currentTime = Calendar.getInstance();
    return isDateBetweenBoundaries(lowerBoundaryDay, currentTime.get(Calendar.MONTH),
                                   currentTime.get(Calendar.YEAR),
                                   upperBoundaryDay, currentTime.get(Calendar.MONTH),
                                   currentTime.get(Calendar.YEAR));
  }
}

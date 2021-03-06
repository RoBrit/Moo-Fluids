/*
 * BurnDamageSource.java
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

package com.robrit.moofluids.common.util.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class BurnDamageSource extends EntityDamageSource {

  public BurnDamageSource(final String damageName, final Entity entity) {
    super(damageName, entity);
    setFireDamage();
    setDifficultyScaled();
  }
}

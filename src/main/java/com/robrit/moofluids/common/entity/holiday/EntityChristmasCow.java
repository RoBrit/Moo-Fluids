/*
 * EntityChristmasCow.java
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

package com.robrit.moofluids.common.entity.holiday;

import com.robrit.moofluids.common.entity.INamedEntity;
import com.robrit.moofluids.common.util.EntityHelper;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityChristmasCow extends EntityCow implements INamedEntity {

  public static final String ENTITY_NAME = "EntityChristmasCow";

  public EntityChristmasCow(final World world) {
    super(world);
  }

  @Override
  public String getEntityName() {
    return ENTITY_NAME;
  }

  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    for (int side = 0; side < 4; ++side) {
      int x = MathHelper.floor(posX + (double) ((float) (side % 2 * 2 - 1) * 0.25F));
      int y = MathHelper.floor(posY);
      int z = MathHelper.floor(posZ + (double) ((float) (side / 2 % 2 * 2 - 1) * 0.25F));

      BlockPos pos = new BlockPos(x, y, z);

      if (world.getBlockState(pos).getBlock() == Blocks.AIR
          && world.getBiome(pos).getFloatTemperature(pos) < 2F
          && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos)) {
        double randX = (double) ((float) posX + rand.nextFloat());
        double randY = (double) ((float) posY + rand.nextFloat());
        double randZ = (double) ((float) posZ + rand.nextFloat());

        world.spawnParticle(EnumParticleTypes.SNOWBALL, randX, randY, randZ,
                               0.0D, 0.0D, 0.0D);

        world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
      }
    }
  }

  @Override
  protected ResourceLocation getLootTable() {
    return EntityHelper.getLootTable(getEntityName());
  }

  @Override
  public EntityChristmasCow createChild(EntityAgeable entityAgeable) {
    return new EntityChristmasCow(world);
  }
}

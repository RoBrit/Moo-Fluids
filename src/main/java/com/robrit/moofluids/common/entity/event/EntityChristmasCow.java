/*
 * EnttiyChristmasCow.java
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

package com.robrit.moofluids.common.entity.event;


import com.robrit.moofluids.common.entity.INamedEntity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityChristmasCow extends EntityCow implements INamedEntity {

  private static final String ENTITY_NAME = "Christmas Cow";

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
      int x = MathHelper.floor_double(posX + (double) ((float) (side % 2 * 2 - 1) * 0.25F));
      int y = MathHelper.floor_double(posY);
      int z = MathHelper.floor_double(posZ + (double) ((float) (side / 2 % 2 * 2 - 1) * 0.25F));

      BlockPos pos = new BlockPos(x, y, z);

      if (worldObj.getBlockState(pos).getBlock() == Blocks.air
          && worldObj.getBiomeGenForCoords(pos).getFloatTemperature(pos) < 2F
          && Blocks.snow_layer.canPlaceBlockAt(worldObj, pos)) {
        double randX = (double) ((float) posX + rand.nextFloat());
        double randY = (double) ((float) posY + rand.nextFloat());
        double randZ = (double) ((float) posZ + rand.nextFloat());

        worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, randX, randY, randZ,
                               0.0D, 0.0D, 0.0D);

        worldObj.setBlockState(pos, Blocks.snow_layer.getDefaultState());
      }
    }
  }

  @Override
  protected Item getDropItem() {
    return Items.cookie;
  }

  @Override
  protected void dropFewItems(boolean hasRecentlyBeenHitByPlayer, int levelOfLootingUsed) {
    int maxSpawnedAmount = rand.nextInt(3) + rand.nextInt(1 + levelOfLootingUsed);
    int currentSpawnedAmount = 0;
    int numberToDropExtra = 100; /* The chance to drop extra item(s)/blocks(s) */

    for (currentSpawnedAmount = 0; currentSpawnedAmount < maxSpawnedAmount;
         currentSpawnedAmount++) {
      dropItem(getDropItem(), 4);
    }
    /* 1 in numberToDropExtra chance of occurring (1/numberToDropExtra) */
    if (rand.nextInt(numberToDropExtra - 1) + 1 == 1) {
      dropItem(Items.diamond, 1); /* Drops an EntityItem into the world with the given ID and the specified amount */
    }
  }

  @Override
  public EntityChristmasCow createChild(EntityAgeable entityAgeable) {
    return new EntityChristmasCow(worldObj);
  }
}

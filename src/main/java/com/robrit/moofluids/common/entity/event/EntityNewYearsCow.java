/*
 * EntityNewYearsCow.java
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

package com.robrit.moofluids.common.entity.event;

import com.robrit.moofluids.common.entity.INamedEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFirework;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityNewYearsCow extends EntityCow implements INamedEntity {

  private static final String ENTITY_NAME = "New Years Cow";

  public EntityNewYearsCow(final World world) {
    super(world);
  }

  @Override
  public String getEntityName() {
    return ENTITY_NAME;
  }

  /* Items dropped */
  @Override
  protected void dropFewItems(boolean hasRecentlyBeenHitByPlayer, int levelOfLootingUsed) {
    final int maxSpawnedAmount = rand.nextInt(3) + rand.nextInt(1 + levelOfLootingUsed);
    final int numberToDropExtra = 20; /* The chance to drop extra item(s)/blocks(s) */

    for (int currentSpawnedAmount = 0; currentSpawnedAmount < maxSpawnedAmount;
         currentSpawnedAmount++) {
      dropItem(getDropItem(), 4);
    }
    /* 1 in numberToDropExtra chance of occurring (1/numberToDropExtra) */
    if (rand.nextInt(numberToDropExtra - 1) + 1 == 1) {
      dropItem(Items.fireworks, 1); /* Drops an EntityItem into the world with the given ID and the specified amount */
    }
  }

  /* The way an entity interacts when it dies */
  @Override
  protected void onDeathUpdate() {
    if (worldObj.isRemote) {
      final byte maxParticles = (byte) ((byte) rand.nextInt(10) + 1);
      byte currentParticles = 0;
      while (currentParticles < maxParticles) {
        spawnParticle();
        currentParticles++;
      }
    }
    super.onDeathUpdate();
  }

  @Override
  public void onEntityUpdate() {
    super.onEntityUpdate();
    if (worldObj.isRemote) {
      spawnParticle();
    }
  }

  @SideOnly(Side.CLIENT)
  private void spawnParticle() {
    final Minecraft minecraft = Minecraft.getMinecraft();
    if (minecraft.gameSettings.particleSetting != 2) {
      final EntityFX entityFX = new EntityFirework.SparkFX(worldObj, posX, posY + 1D, posZ,
                                                          rand.nextGaussian() * 0.15D,
                                                          -motionY * 0.5D,
                                                          rand.nextGaussian() * 0.15D,
                                                          minecraft.effectRenderer);
      entityFX.setRBGColorF(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
      minecraft.effectRenderer.addEffect(entityFX);
    }
  }

  @Override
  public EntityNewYearsCow createChild(EntityAgeable entityAgeable) {
    return new EntityNewYearsCow(worldObj);
  }
}

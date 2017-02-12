/*
 * EntityValentinesCow.java
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

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityValentinesCow extends EntityCow implements INamedEntity {

  private static final String ENTITY_NAME = "EntityValentinesCow";

  public EntityValentinesCow(final World world) {
    super(world);
  }

  @Override
  public String getEntityName() {
    return ENTITY_NAME;
  }

  @Override
  protected ResourceLocation getLootTable() {
    return EntityHelper.getLootTable(getEntityName());
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
      final EntityHeartFX.Factory heartFXFactory = new EntityHeartFX.Factory();
      final EntityFX entityFX =
          heartFXFactory.getEntityFX(1, worldObj,
                                     posX + rand.nextDouble(),
                                     posY + rand.nextDouble(),
                                     posZ + rand.nextDouble(),
                                     rand.nextGaussian() * 0.30D, -motionY * 0.5D,
                                     rand.nextGaussian() * 0.30D);
      minecraft.effectRenderer.addEffect(entityFX);
    }
  }

  @Override
  public EntityValentinesCow createChild(EntityAgeable entityAgeable) {
    return new EntityValentinesCow(worldObj);
  }
}

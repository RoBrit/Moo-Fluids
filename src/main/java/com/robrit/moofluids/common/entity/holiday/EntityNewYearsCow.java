/*
 * EntityNewYearsCow.java
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
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityNewYearsCow extends EntityCow implements INamedEntity {

  private static final String ENTITY_NAME = "EntityNewYearsCow";

  public EntityNewYearsCow(final World world) {
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
  protected void onDeathUpdate() {
    if (world.isRemote) {
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
    if (world.isRemote) {
      spawnParticle();
    }
  }

  @SideOnly(Side.CLIENT)
  private void spawnParticle() {
    final Minecraft minecraft = Minecraft.getMinecraft();
    if (minecraft.gameSettings.particleSetting != 2) {
      final ParticleFirework.Factory particleFireworkFactory = new ParticleFirework.Factory();
      final Particle particle =
          particleFireworkFactory.createParticle(1, world,
                                                 posX, posY + 1D, posZ,
                                                 rand.nextGaussian() * 0.15D,
                                                 -motionY * 0.5D,
                                                 rand.nextGaussian() * 0.15D);

      particle.setRBGColorF(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
      minecraft.effectRenderer.addEffect(particle);
    }
  }

  @Override
  public EntityNewYearsCow createChild(EntityAgeable entityAgeable) {
    return new EntityNewYearsCow(world);
  }
}

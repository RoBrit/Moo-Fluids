/*
 * EntityTypeData.java
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

package com.robrit.moofluids.common.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityTypeData {

  @SideOnly(Side.CLIENT)
  public ResourceLocation entityTexture;

  /* Configuration values */
  private boolean entityIsSpawnable;
  private int entitySpawnRate;

  private int entityNormalDamageAmount;
  private int entityFireDamageAmount;
  private PotionEffect[] entityPotionEffects;

  private ItemStack[] entityBreedingItems;
  private ItemStack[] entityDroppedItems;

  private int entityGrowUpTime;
  private int entityMaxUseCooldown;

  @SideOnly(Side.CLIENT)
  public ResourceLocation getEntityTexture() {
    return entityTexture;
  }

  @SideOnly(Side.CLIENT)
  public void setEntityTexture(final ResourceLocation entityTexture) {
    this.entityTexture = entityTexture;
  }

  public boolean isEntityIsSpawnable() {
    return entityIsSpawnable;
  }

  public void setEntityIsSpawnable(final boolean entityIsSpawnable) {
    this.entityIsSpawnable = entityIsSpawnable;
  }

  public int getEntitySpawnRate() {
    return entitySpawnRate;
  }

  public void setEntitySpawnRate(final int entitySpawnRate) {
    this.entitySpawnRate = entitySpawnRate;
  }

  public int getEntityNormalDamageAmount() {
    return entityNormalDamageAmount;
  }

  public void setEntityNormalDamageAmount(final int entityNormalDamageAmount) {
    this.entityNormalDamageAmount = entityNormalDamageAmount;
  }

  public int getEntityFireDamageAmount() {
    return entityFireDamageAmount;
  }

  public void setEntityFireDamageAmount(final int entityFireDamageAmount) {
    this.entityFireDamageAmount = entityFireDamageAmount;
  }

  public PotionEffect[] getEntityPotionEffects() {
    return entityPotionEffects;
  }

  public void setEntityPotionEffects(final PotionEffect[] entityPotionEffects) {
    this.entityPotionEffects = entityPotionEffects;
  }

  public ItemStack[] getEntityBreedingItems() {
    return entityBreedingItems;
  }

  public void setEntityBreedingItems(final ItemStack[] entityBreedingItems) {
    this.entityBreedingItems = entityBreedingItems;
  }

  public ItemStack[] getEntityDroppedItems() {
    return entityDroppedItems;
  }

  public void setEntityDroppedItems(final ItemStack[] entityDroppedItems) {
    this.entityDroppedItems = entityDroppedItems;
  }

  public int getEntityGrowUpTime() {
    return entityGrowUpTime;
  }

  public void setEntityGrowUpTime(final int entityGrowUpTime) {
    this.entityGrowUpTime = entityGrowUpTime;
  }

  public int getEntityMaxUseCooldown() {
    return entityMaxUseCooldown;
  }

  public void setEntityMaxUseCooldown(final int entityMaxUseCooldown) {
    this.entityMaxUseCooldown = entityMaxUseCooldown;
  }
}

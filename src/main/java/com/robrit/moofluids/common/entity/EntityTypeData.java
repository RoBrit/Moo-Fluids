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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTypeData {

  /* Non-configurable values */
  @SideOnly(Side.CLIENT)
  private ResourceLocation texture;
  private int overlay = 0x00000000;

  private boolean causeFireDamage = false;
  private boolean causeNormalDamage = false;

  /* Configurable values */
  private boolean isSpawnable;
  private int spawnRate;

  private int fireDamageAmount;
  private int normalDamageAmount;
  private PotionEffect[] potionEffects;

  private ItemStack breedingItem = new ItemStack(Items.wheat, 1);
  private ItemStack[] droppedItems;

  private int growUpTime;
  private int maxUseCooldown;

  private boolean damageEntities;
  private boolean damagePlayers;

  @SideOnly(Side.CLIENT)
  public ResourceLocation getTexture() {
    return texture;
  }

  @SideOnly(Side.CLIENT)
  public void setTexture(final ResourceLocation texture) {
    this.texture = texture;
  }

  @SideOnly(Side.CLIENT)
  public int getOverlay() {
    return overlay;
  }

  @SideOnly(Side.CLIENT)
  public void setOverlay(final int overlay) {
    this.overlay = overlay;
  }

  public boolean canCauseFireDamage() {
    return causeFireDamage;
  }

  public void setCauseFireDamage(final boolean causeFireDamage) {
    this.causeFireDamage = causeFireDamage;
  }

  public boolean canCauseNormalDamage() {
    return causeNormalDamage;
  }

  public void setCauseNormalDamage(final boolean causeNormalDamage) {
    this.causeNormalDamage = causeNormalDamage;
  }

  public boolean isSpawnable() {
    return isSpawnable;
  }

  public void setSpawnable(final boolean spawnable) {
    this.isSpawnable = spawnable;
  }

  public int getSpawnRate() {
    return spawnRate;
  }

  public void setSpawnRate(final int spawnRate) {
    this.spawnRate = spawnRate;
  }

  public int getNormalDamageAmount() {
    return normalDamageAmount;
  }

  public void setNormalDamageAmount(final int normalDamageAmount) {
    this.normalDamageAmount = normalDamageAmount;
  }

  public int getFireDamageAmount() {
    return fireDamageAmount;
  }

  public void setFireDamageAmount(final int fireDamageAmount) {
    this.fireDamageAmount = fireDamageAmount;
  }

  public PotionEffect[] getPotionEffects() {
    return potionEffects;
  }

  public void setPotionEffects(final PotionEffect[] potionEffects) {
    this.potionEffects = potionEffects;
  }

  public ItemStack getBreedingItem() {
    return breedingItem;
  }

  public void setBreedingItems(final ItemStack breedingItem) {
    this.breedingItem = breedingItem;
  }

  public ItemStack[] getDroppedItems() {
    return droppedItems;
  }

  public void setDroppedItems(final ItemStack[] droppedItems) {
    this.droppedItems = droppedItems;
  }

  public int getGrowUpTime() {
    return growUpTime;
  }

  public void setGrowUpTime(final int growUpTime) {
    this.growUpTime = growUpTime;
  }

  public int getMaxUseCooldown() {
    return maxUseCooldown;
  }

  public void setMaxUseCooldown(final int maxUseCooldown) {
    this.maxUseCooldown = maxUseCooldown;
  }

  public boolean canDamageEntities() {
    return damageEntities;
  }

  public void setDamageEntities(final boolean damagesEntities) {
    this.damageEntities = damagesEntities;
  }

  public boolean canDamagePlayers() {
    return damagePlayers;
  }

  public void setDamagePlayers(final boolean damagesPlayers) {
    this.damagePlayers = damagesPlayers;
  }
}

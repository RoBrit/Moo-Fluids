/*
 * EntityFluidCow.java
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

package com.robrit.moofluids.common.entity;

import com.robrit.moofluids.common.ref.ModInformation;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.damage.AttackDamageSource;
import com.robrit.moofluids.common.util.damage.BurnDamageSource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;

public class EntityFluidCow extends EntityCow implements IEntityAdditionalSpawnData, INamedEntity {

  private static final DataParameter<Integer> DATA_WATCHER_CURRENT_USE_COOLDOWN =
      EntityDataManager.createKey(EntityFluidCow.class, DataSerializers.VARINT);
  public static final String ENTITY_NAME = "EntityFluidCow";
  public static final String NBT_TAG_FLUID_NAME = "FluidName";
  public static final String NBT_TAG_CURRENT_USE_COOLDOWN = "CurrentUseCooldown";
  private int currentUseCooldown;
  private Fluid entityFluid;
  private EntityTypeData entityTypeData;

  public EntityFluidCow(final World world) {
    super(world);
    setEntityTypeData(EntityHelper.getEntityData(getEntityFluid().getName()));
    setCurrentUseCooldown(entityTypeData.getMaxUseCooldown());

    if (getEntityFluid().getTemperature() >= FluidRegistry.LAVA.getTemperature()) {
      isImmuneToFire = true;
    }
  }

  @Override
  public String getEntityName() {
    return ENTITY_NAME;
  }

  @Override
  protected void entityInit() {
    super.entityInit();
    dataManager.register(DATA_WATCHER_CURRENT_USE_COOLDOWN, 0);
  }

  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    if (currentUseCooldown > 0) {
      setCurrentUseCooldown(currentUseCooldown - 1);
    }
  }

  @Override
  public boolean processInteract(EntityPlayer entityPlayer, EnumHand hand,
                                 ItemStack currentItemStack) {
    if (!isChild()) {

      if (ModInformation.DEBUG_MODE) {
        setCurrentUseCooldown(0);
      }

      if (getCurrentUseCooldown() == 0) {
        if (!entityPlayer.capabilities.isCreativeMode) {
          setCurrentUseCooldown(entityTypeData.getMaxUseCooldown());
        }
        if (attemptToGetFluidFromCow(currentItemStack, entityPlayer)) {
          return true;
        } else if (attemptToHealCowWithFluidContainer(currentItemStack, entityPlayer)) {
          return true;
        } else if (attemptToBreedCow(currentItemStack, entityPlayer)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void collideWithEntity(final Entity entity) {
    if (entityTypeData.canDamageEntities()) {
      if (!(entity instanceof EntityFluidCow)) {
        applyDamagesToEntity(entity);
      }
    }

    super.collideWithEntity(entity);
  }

  @Override
  public void onCollideWithPlayer(final EntityPlayer entityPlayer) {
    if (entityTypeData.canDamagePlayers()) {
      applyDamagesToEntity(entityPlayer);
    }

    super.onCollideWithPlayer(entityPlayer);
  }

  @Override
  public boolean attackEntityFrom(final DamageSource damageSource, final float damageAmount) {
    if (damageSource.getEntity() instanceof EntityPlayer) {
      final EntityPlayer entityPlayer = (EntityPlayer) damageSource.getEntity();
      if (entityPlayer.getHeldItem(EnumHand.MAIN_HAND) == null) {
        applyDamagesToEntity(entityPlayer);
      }
    }
    return super.attackEntityFrom(damageSource, damageAmount);
  }

  @Override
  public boolean isBreedingItem(final ItemStack currentItemStack) {
    return currentItemStack != null &&
           currentItemStack.getItem() == entityTypeData.getBreedingItem().getItem();
  }

  @Override
  public boolean canMateWith(EntityAnimal entityAnimal) {
    if (entityAnimal != this) {
      if (isInLove() && entityAnimal.isInLove()) {
        if (entityAnimal instanceof EntityFluidCow) {
          final Fluid mateEntityFluid = ((EntityFluidCow) entityAnimal).getEntityFluid();
          if (getEntityFluid().getName().equals(mateEntityFluid.getName())) {
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  public EntityFluidCow createChild(final EntityAgeable entityAgeable) {
    final EntityFluidCow childEntity = new EntityFluidCow(world);
    childEntity.setEntityFluid(entityFluid);

    return childEntity;
  }

  private void applyDamagesToEntity(final Entity entity) {
    if (entity instanceof EntityLivingBase) {
      if (entityTypeData.canCauseFireDamage()) {
        byte ticksOfDamage = 8;

        if (entity instanceof EntityPlayer) {
          final EntityPlayer entityPlayer = (EntityPlayer) entity;
          final int armorInventoryLength = entityPlayer.inventory.armorInventory.length;
          int currentArmorSlot;

          for (currentArmorSlot = 0; currentArmorSlot < armorInventoryLength; currentArmorSlot++) {
            if (entityPlayer.inventory.armorItemInSlot(currentArmorSlot) != null) {
              ticksOfDamage -= 2;
            }
          }
        }

        entity.attackEntityFrom(new BurnDamageSource("burn", this),
                                entityTypeData.getFireDamageAmount());
        entity.setFire(ticksOfDamage);

      }
      if (entityTypeData.canCauseNormalDamage()) {
        entity.attackEntityFrom(new AttackDamageSource("whacked", this),
                                entityTypeData.getNormalDamageAmount());
      }
    }
  }

  private boolean attemptToGetFluidFromCow(final ItemStack currentItemStack,
                                           final EntityPlayer entityPlayer) {
    boolean canGetFluid = false;

    if (currentItemStack != null && entityFluid != null) {
      ItemStack filledItemStack;
      if (FluidContainerRegistry.isEmptyContainer(currentItemStack)) {
        if (FluidContainerRegistry
                .fillFluidContainer(
                    new FluidStack(entityFluid, FluidContainerRegistry.BUCKET_VOLUME),
                    currentItemStack) != null) {

          filledItemStack =
              FluidContainerRegistry
                  .fillFluidContainer(
                      new FluidStack(entityFluid, FluidContainerRegistry.BUCKET_VOLUME),
                      currentItemStack);

          if (currentItemStack.stackSize-- == 1) {
            entityPlayer.inventory.setInventorySlotContents(
                entityPlayer.inventory.currentItem,
                filledItemStack.copy());
          } else if (!entityPlayer.inventory.addItemStackToInventory(filledItemStack.copy())) {
            entityPlayer.dropItem(filledItemStack.copy(), false);
          }

          canGetFluid = true;
        }
      }

      if (!canGetFluid && currentItemStack.getItem() instanceof ItemBucket &&
          FluidRegistry.isUniversalBucketEnabled()) {
        filledItemStack =
            UniversalBucket.getFilledBucket(
                ForgeModContainer.getInstance().universalBucket, entityFluid);
        if (currentItemStack.stackSize-- == 1) {
          entityPlayer.inventory.setInventorySlotContents(
              entityPlayer.inventory.currentItem,
              filledItemStack.copy());
        } else if (!entityPlayer.inventory.addItemStackToInventory(filledItemStack.copy())) {
          entityPlayer.dropItem(filledItemStack.copy(), false);
        }

        canGetFluid = true;
      }
    }

    return canGetFluid;
  }

  private boolean attemptToHealCowWithFluidContainer(final ItemStack currentItemStack,
                                                     final EntityPlayer entityPlayer) {
    boolean cowHealed = false;
    if (currentItemStack != null && FluidContainerRegistry.isFilledContainer(currentItemStack)) {
      ItemStack emptyItemStack;
      if (entityFluid != null) {
        for (final FluidContainerRegistry.FluidContainerData containerData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
          if (containerData.fluid.getFluid().getName().equalsIgnoreCase(entityFluid.getName())) {
            if (containerData.filledContainer.isItemEqual(currentItemStack)) {
              emptyItemStack = containerData.emptyContainer;
              if (currentItemStack.stackSize-- == 1) {
                entityPlayer.inventory.setInventorySlotContents(
                    entityPlayer.inventory.currentItem,
                    emptyItemStack.copy());
              } else if (!entityPlayer.inventory.addItemStackToInventory(emptyItemStack.copy())) {
                entityPlayer.dropItem(emptyItemStack.copy(), false);
              }
              heal(4F);
              cowHealed = true;
            }
          }
        }
      }
    }
    return cowHealed;
  }

  private boolean attemptToBreedCow(final ItemStack currentItemStack,
                                    final EntityPlayer entityPlayer) {
    if (currentItemStack != null &&
        isBreedingItem(currentItemStack) &&
        getGrowingAge() == 0) {
      if (!entityPlayer.capabilities.isCreativeMode) {
        currentItemStack.stackSize--;

        if (currentItemStack.stackSize <= 0) {
          entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
        }
      }

      setInLove(entityPlayer);

      return true;
    }

    return false;
  }

  public Fluid getEntityFluid() {
    return entityFluid;
  }

  public void setEntityFluid(final Fluid entityFluid) {
    this.entityFluid = entityFluid;
  }

  public int getCurrentUseCooldown() {
    return currentUseCooldown;
  }

  public void setCurrentUseCooldown(final int currentUseCooldown) {
    dataManager.set(DATA_WATCHER_CURRENT_USE_COOLDOWN, currentUseCooldown);
    this.currentUseCooldown = currentUseCooldown;
  }

  public EntityTypeData getEntityTypeData() {
    return entityTypeData;
  }

  public void setEntityTypeData(final EntityTypeData entityTypeData) {
    this.entityTypeData = entityTypeData;
  }

  @SideOnly(Side.CLIENT)
  public int getOverlay() {
    return entityTypeData.getOverlay();
  }

  @Override
  public void writeEntityToNBT(final NBTTagCompound nbtTagCompound) {
    super.writeEntityToNBT(nbtTagCompound);
    nbtTagCompound.setString(NBT_TAG_FLUID_NAME, getEntityFluid().getName());
    nbtTagCompound.setInteger(NBT_TAG_CURRENT_USE_COOLDOWN, getCurrentUseCooldown());
  }

  @Override
  public void readEntityFromNBT(final NBTTagCompound nbtTagCompound) {
    super.readEntityFromNBT(nbtTagCompound);
    setEntityFluid(EntityHelper.getContainableFluid(nbtTagCompound.getString(NBT_TAG_FLUID_NAME)));
    setCurrentUseCooldown(nbtTagCompound.getInteger(NBT_TAG_CURRENT_USE_COOLDOWN));
  }

  @Override
  public void writeSpawnData(final ByteBuf buffer) {
    ByteBufUtils.writeUTF8String(buffer, entityFluid.getName());
    ByteBufUtils.writeVarInt(buffer, currentUseCooldown, 4);
  }

  @Override
  public void readSpawnData(final ByteBuf additionalData) {
    setEntityFluid(EntityHelper.getContainableFluid(ByteBufUtils.readUTF8String(additionalData)));
    setCurrentUseCooldown(ByteBufUtils.readVarInt(additionalData, 4));
    entityTypeData = EntityHelper.getEntityData(getEntityFluid().getName());
  }
}

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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import io.netty.buffer.ByteBuf;

public class EntityFluidCow extends EntityCow implements IEntityAdditionalSpawnData, INamedEntity {

  private static final DataParameter<Integer> DATA_WATCHER_NEXT_USE_COOLDOWN =
      EntityDataManager.createKey(EntityFluidCow.class, DataSerializers.VARINT);
  public static final String ENTITY_NAME = "EntityFluidCow";
  public static final String NBT_TAG_FLUID_NAME = "FluidName";
  public static final String NBT_TAG_NEXT_USE_COOLDOWN = "NextUseCooldown";
  private static final int MAX_SYNC_DELAY = 10; /* in ticks */
  private int ticks = 0;
  private Fluid entityFluid;
  private EntityTypeData entityTypeData;

  public EntityFluidCow(final World world) {
    super(world);
    setEntityTypeData(EntityHelper.getEntityData(getEntityFluid().getName()));
    setNextUseCooldown(entityTypeData.getMaxUseCooldown());

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
    dataManager.register(DATA_WATCHER_NEXT_USE_COOLDOWN, 0);
  }

  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    if (!world.isRemote && dataManager.get(DATA_WATCHER_NEXT_USE_COOLDOWN) > 0) {
      int cooldown = dataManager.get(DATA_WATCHER_NEXT_USE_COOLDOWN);
      if (++ticks >= cooldown || ticks >= MAX_SYNC_DELAY) {
        cooldown = Math.max(0, cooldown - ticks);
        setNextUseCooldown(cooldown);
      }
    }
  }

  @Override
  public boolean processInteract(EntityPlayer entityPlayer, EnumHand hand) {
    if (!isChild()) {

      if (ModInformation.DEBUG_MODE) {
        setNextUseCooldown(0);
      }

      if (getNextUseCooldown() == 0) {
        ItemStack currentItemStack = entityPlayer.getHeldItem(hand);
        if (entityPlayer instanceof FakePlayer) {
          setNextUseCooldown(entityTypeData.getMaxAutomationCooldown());
        } else if (!entityPlayer.capabilities.isCreativeMode) {
          setNextUseCooldown(entityTypeData.getMaxUseCooldown());
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
    if (damageSource instanceof EntityDamageSource) {
      EntityDamageSource entityDamageSource = (EntityDamageSource)damageSource;
      if (entityDamageSource.getTrueSource() instanceof EntityPlayer) {
        final EntityPlayer entityPlayer = (EntityPlayer) damageSource.getTrueSource();
        if (entityPlayer.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
          applyDamagesToEntity(entityPlayer);
        }
      }
    }
    return super.attackEntityFrom(damageSource, damageAmount);
  }

  @Override
  public boolean isBreedingItem(final ItemStack currentItemStack) {
    return !currentItemStack.isEmpty() &&
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
          final int armorInventoryLength = entityPlayer.inventory.armorInventory.size();
          int currentArmorSlot;

          for (currentArmorSlot = 0; currentArmorSlot < armorInventoryLength; currentArmorSlot++) {
            if (!entityPlayer.inventory.armorInventory.get(currentArmorSlot).isEmpty()) {
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

    if (!currentItemStack.isEmpty() && entityFluid != null) {
      ItemStack filledItemStack = ItemHandlerHelper.copyStackWithSize(currentItemStack, 1);
      IFluidHandlerItem fluidHandlerItem = FluidUtil.getFluidHandler(filledItemStack);
      if (fluidHandlerItem != null) {
        if (fluidHandlerItem.fill(
                new FluidStack(entityFluid, Fluid.BUCKET_VOLUME), true) == Fluid.BUCKET_VOLUME) {

          filledItemStack = fluidHandlerItem.getContainer();

          currentItemStack.shrink(1);
          if (currentItemStack.isEmpty()) {
            entityPlayer.inventory.setInventorySlotContents(
                entityPlayer.inventory.currentItem,
                filledItemStack.copy());
          } else {
            ItemHandlerHelper.giveItemToPlayer(entityPlayer, filledItemStack.copy());
          }

          canGetFluid = true;
        }
      }
    }

    return canGetFluid;
  }

  private boolean attemptToHealCowWithFluidContainer(final ItemStack currentItemStack,
                                                     final EntityPlayer entityPlayer) {
    boolean cowHealed = false;
    if (!currentItemStack.isEmpty() && entityFluid != null) {
      IFluidHandlerItem fluidHandlerItem = FluidUtil.getFluidHandler(currentItemStack);
      if (fluidHandlerItem != null) {
        FluidStack containedFluid = fluidHandlerItem
                .drain(new FluidStack(entityFluid, Fluid.BUCKET_VOLUME), false);
        ItemStack emptyItemStack;
        if (containedFluid != null &&
                containedFluid.getFluid().getName().equalsIgnoreCase(entityFluid.getName())) {
          fluidHandlerItem.drain(new FluidStack(entityFluid, Fluid.BUCKET_VOLUME), false);
          emptyItemStack = fluidHandlerItem.getContainer();
          currentItemStack.shrink(1);
          if (currentItemStack.isEmpty()) {
            entityPlayer.inventory.setInventorySlotContents(
                    entityPlayer.inventory.currentItem,
                    emptyItemStack.copy());
          } else {
            ItemHandlerHelper.giveItemToPlayer(entityPlayer, emptyItemStack.copy());
          }
          heal(4F);
          cowHealed = true;
        }
      }
    }
    return cowHealed;
  }

  private boolean attemptToBreedCow(final ItemStack currentItemStack,
                                    final EntityPlayer entityPlayer) {
    if (isBreedingItem(currentItemStack) &&
        getGrowingAge() == 0) {
      if (!entityPlayer.capabilities.isCreativeMode) {
        currentItemStack.shrink(1);

        if (currentItemStack.isEmpty()) {
          entityPlayer.inventory.setInventorySlotContents(
                  entityPlayer.inventory.currentItem, ItemStack.EMPTY);
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

  public int getNextUseCooldown() {
    return Math.max(0, dataManager.get(DATA_WATCHER_NEXT_USE_COOLDOWN) - ticks);
  }

  /* Must be done server side */
  public void setNextUseCooldown(final int nextUseCooldown) {
    dataManager.set(DATA_WATCHER_NEXT_USE_COOLDOWN, nextUseCooldown);
    ticks = 0;
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
    nbtTagCompound.setInteger(NBT_TAG_NEXT_USE_COOLDOWN, getNextUseCooldown());
  }

  @Override
  public void readEntityFromNBT(final NBTTagCompound nbtTagCompound) {
    super.readEntityFromNBT(nbtTagCompound);
    setEntityFluid(EntityHelper.getContainableFluid(nbtTagCompound.getString(NBT_TAG_FLUID_NAME)));
    setNextUseCooldown(nbtTagCompound.getInteger(NBT_TAG_NEXT_USE_COOLDOWN));
  }

  @Override
  public void writeSpawnData(final ByteBuf buffer) {
    ByteBufUtils.writeUTF8String(buffer, entityFluid.getName());
  }

  @Override
  public void readSpawnData(final ByteBuf additionalData) {
    setEntityFluid(EntityHelper.getContainableFluid(ByteBufUtils.readUTF8String(additionalData)));
    entityTypeData = EntityHelper.getEntityData(getEntityFluid().getName());
  }
}

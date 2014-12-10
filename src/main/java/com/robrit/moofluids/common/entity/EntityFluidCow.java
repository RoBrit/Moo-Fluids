/*
 * EntityFluidCow.java
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

import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.ModInformation;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class EntityFluidCow extends EntityCow {

  private static final String NBT_TAG_FLUID_NAME = "FluidName";
  private static final String NBT_TAG_CURRENT_USE_COOLDOWN = "CurrentUseCooldown";

  private static final int DATA_WATCHER_ID_CURRENT_USE_COOLDOWN = 23;

  private Fluid entityFluid = EntityHelper.getContainableFluid(FluidRegistry.WATER.getName());
  private int maxUseCooldown;
  private int currentUseCooldown;

  public EntityFluidCow(final World world) {
    super(world);
  }

  public EntityFluidCow(final World world, final Fluid entityFluid) {
    this(world);
    setEntityFluid(entityFluid);
    setMaxUseCooldown(EntityHelper.getEntityData(entityFluid.getName()).getEntityMaxUseCooldown());
    setCurrentUseCooldown(getMaxUseCooldown());
  }

  @Override
  protected void entityInit() {
    super.entityInit();

    dataWatcher.addObject(DATA_WATCHER_ID_CURRENT_USE_COOLDOWN, 0);
  }

  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    if (currentUseCooldown > 0) {
      setCurrentUseCooldown(currentUseCooldown--);
    }
  }

  @Override
  public boolean interact(final EntityPlayer entityPlayer) {
    if (!isChild()) {
      final ItemStack currentItemStack = entityPlayer.inventory.getCurrentItem();

      if (ModInformation.DEBUG_MODE) {
        setCurrentUseCooldown(0);
      }

      if ((getCurrentUseCooldown() == 0)) {
        if (!entityPlayer.capabilities.isCreativeMode) {
          setCurrentUseCooldown(getMaxUseCooldown());
        }
        if (attemptToGetFluidFromCow(currentItemStack, entityPlayer)) {
          return true;
        } else if (attemptToHealCowWithFluidContainer(currentItemStack, entityPlayer)) {
          return true;
        }
      }
    }
    return false;
  }

  /* Used to attempt to fill a fluid container with the fluid that the MooFluids Cow gives */
  private boolean attemptToGetFluidFromCow(final ItemStack currentItemStack,
                                           final EntityPlayer entityPlayer) {
    boolean canGetFluid = false;

    if (currentItemStack != null && FluidContainerRegistry.isEmptyContainer(currentItemStack)) {
      ItemStack filledItemStack;
      if (entityFluid != null) {
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
            entityPlayer.inventory
                .setInventorySlotContents(entityPlayer.inventory.currentItem,
                                          filledItemStack.copy());
          } else if (!entityPlayer.inventory.addItemStackToInventory(filledItemStack.copy())) {
            entityPlayer.dropPlayerItemWithRandomChoice(filledItemStack.copy(), false);
          }

          canGetFluid = true;
        }
      }
    }
    return canGetFluid;
  }

  /* Used to attempt to heal a MooFluids Cow with a filled container of the fluid that the Cow gives */
  private boolean attemptToHealCowWithFluidContainer(ItemStack currentItemStack,
                                                     EntityPlayer entityPlayer) {
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
                entityPlayer.inventory
                    .setInventorySlotContents(entityPlayer.inventory.currentItem,
                                              emptyItemStack.copy());
              } else if (!entityPlayer.inventory.addItemStackToInventory(emptyItemStack.copy())) {
                entityPlayer.dropPlayerItemWithRandomChoice(emptyItemStack.copy(), false);
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
    this.currentUseCooldown = currentUseCooldown;
  }

  public int getMaxUseCooldown() {
    return maxUseCooldown;
  }

  public void setMaxUseCooldown(final int maxUseCooldown) {
    this.maxUseCooldown = maxUseCooldown;
  }
}

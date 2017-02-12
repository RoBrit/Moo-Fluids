/*
 * EntityEasterCow.java
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEasterCow extends EntityCow implements INamedEntity {

  private static final String ENTITY_NAME = "EntityEasterCow";
  private int field_175540_bm = 0; // Possibly jump/movement cooldowns
  private int field_175535_bn = 0; // Possibly jump/movement cooldowns
  private boolean canJump = false;
  private int currentMoveTypeDuration = 0;

  public EntityEasterCow(final World world) {
    super(world);
    jumpHelper = new EntityEasterCow.JumpHelper(this);
    moveHelper = new EntityEasterCow.MoveHelper(this);
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
  }

  @Override
  protected void initEntityAI() {
    tasks.addTask(0, new EntityAISwimming(this));
    tasks.addTask(1, new EntityAIPanic(this, 2.0D));
    tasks.addTask(2, new EntityAIMate(this, 1.0D));
    tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.golden_carrot, false));
    tasks.addTask(4, new EntityAIAvoidEntity<EntityPlayer>
        (this, EntityPlayer.class, 8.0F, 2.2D, 2.2D));
    tasks.addTask(4, new EntityAIAvoidEntity<EntityWolf>
        (this, EntityWolf.class, 10.0F, 2.2D, 2.2D));
    tasks.addTask(6, new EntityAIWander(this, 0.6D));
    tasks.addTask(7, new EntityAILookIdle(this));
    tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
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
  public EntityEasterCow createChild(final EntityAgeable entityAgeable) {
    return new EntityEasterCow(worldObj);
  }

  @Override
  protected float getJumpUpwardsMotion() {
    if (!isCollidedHorizontally && (!moveHelper.isUpdating() || moveHelper.getY() <= posY + 0.5D)) {
      final PathEntity pathEntity = navigator.getPath();

      if (pathEntity != null &&
          pathEntity.getCurrentPathIndex() < pathEntity.getCurrentPathLength()) {
        final Vec3d vec3d = pathEntity.getPosition(this);

        if (vec3d.yCoord > posY) {
          return 0.5F;
        }
      }

      return moveHelper.getSpeed() <= 0.6D ? 0.2F : 0.3F;
    } else {
      return 0.5F;
    }
  }

  @Override
  protected void jump() {
    super.jump();
    final double moveSpeed = moveHelper.getSpeed();

    if (moveSpeed > 0.0D) {
      double d1 = motionX * motionX + motionZ * motionZ;

      if (d1 < 0.010000000000000002D) {
        moveFlying(0.0F, 1.0F, 0.1F);
      }
    }

    if (!worldObj.isRemote) {
      worldObj.setEntityState(this, (byte) 1);
    }
  }

  public void setMovementSpeed(double newSpeed) {
    getNavigator().setSpeed(newSpeed);
    moveHelper.setMoveTo(moveHelper.getX(), moveHelper.getY(), moveHelper.getZ(), newSpeed);
  }

  @Override
  public void setJumping(boolean jumping) {
    super.setJumping(jumping);

    if (jumping) {
      playSound(getJumpSound(), getSoundVolume(),
                ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
    }
  }

  public void func_184770_cZ() {
    setJumping(true);
    field_175535_bn = 10;
    field_175540_bm = 0;
  }

  @Override
  public void updateAITasks() {
    if (currentMoveTypeDuration > 0) {
      --currentMoveTypeDuration;
    }

    if (onGround) {
      if (!canJump) {
        setJumping(false);
        func_175517_cu();
      }

      if (currentMoveTypeDuration == 0) {
        final EntityLivingBase entityLivingBase = getAttackTarget();

        if (entityLivingBase != null && getDistanceSqToEntity(entityLivingBase) < 16.0D) {
          calculateRotationYaw(entityLivingBase.posX, entityLivingBase.posZ);
          moveHelper.setMoveTo(entityLivingBase.posX, entityLivingBase.posY,
                               entityLivingBase.posZ, moveHelper.getSpeed());
          func_184770_cZ();
          canJump = true;
        }
      }

      final EntityEasterCow.JumpHelper
          entityEasterCow$jumpHelper =
          (EntityEasterCow.JumpHelper) jumpHelper;

      if (!entityEasterCow$jumpHelper.getIsJumping()) {
        if (moveHelper.isUpdating() && currentMoveTypeDuration == 0) {
          final PathEntity pathEntity = navigator.getPath();
          Vec3d vec3d = new Vec3d(moveHelper.getX(), moveHelper.getY(), moveHelper.getZ());

          if (pathEntity != null &&
              pathEntity.getCurrentPathIndex() < pathEntity.getCurrentPathLength()) {
            vec3d = pathEntity.getPosition(this);
          }

          calculateRotationYaw(vec3d.xCoord, vec3d.zCoord);
          func_184770_cZ();
        }
      } else if (!entityEasterCow$jumpHelper.func_180065_d()) {
        func_175518_cr();
      }
    }

    canJump = onGround;
  }

  private void calculateRotationYaw(double x, double z) {
    rotationYaw = (float) (MathHelper.atan2(z - posZ, x - posX) * (180D / Math.PI)) - 90.0F;
  }

  private void func_175518_cr() {
    ((EntityEasterCow.JumpHelper) jumpHelper).func_180066_a(true);
  }

  private void func_175520_cs() {
    ((EntityEasterCow.JumpHelper) jumpHelper).func_180066_a(false);
  }

  private void updateMoveTypeDuration() {
    if (moveHelper.getSpeed() < 2.2D) {
      currentMoveTypeDuration = 10;
    } else {
      currentMoveTypeDuration = 1;
    }
  }

  private void func_175517_cu() {
    updateMoveTypeDuration();
    func_175520_cs();
  }

  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    if (field_175540_bm != field_175535_bn) {
      ++field_175540_bm;
    } else if (this.field_175535_bn != 0) {
      field_175540_bm = 0;
      field_175535_bn = 0;
      setJumping(false);
    }
  }

  protected SoundEvent getJumpSound() {
    return SoundEvents.entity_horse_jump;
  }

  @SideOnly(Side.CLIENT)
  public void handleStatusUpdate(byte id) {
    if (id == 1) {
      this.createRunningParticles();
      this.field_175535_bn = 10;
      this.field_175540_bm = 0;
    } else {
      super.handleStatusUpdate(id);
    }
  }

  private class JumpHelper extends EntityJumpHelper {

    private EntityEasterCow theEntity;
    private boolean field_180068_d = false;

    public JumpHelper(EntityEasterCow entity) {
      super(entity);
      theEntity = entity;
    }

    public boolean getIsJumping() {
      return isJumping;
    }

    public boolean func_180065_d() {
      return field_180068_d;
    }

    public void func_180066_a(boolean p_180066_1_) {
      field_180068_d = p_180066_1_;
    }

    @Override
    public void doJump() {
      if (isJumping) {
        theEntity.func_184770_cZ();
        isJumping = false;
      }
    }
  }

  private static class MoveHelper extends EntityMoveHelper {

    private EntityEasterCow theEntity;
    private double movementSpeed;

    public MoveHelper(EntityEasterCow entity) {
      super(entity);
      theEntity = entity;
    }

    @Override
    public void onUpdateMoveHelper() {
      if (theEntity.onGround && !theEntity.isJumping &&
          !((EntityEasterCow.JumpHelper) theEntity.jumpHelper).getIsJumping()) {
        theEntity.setMovementSpeed(0.0D);
      } else if (isUpdating()) {
        theEntity.setMovementSpeed(movementSpeed);
      }

      super.onUpdateMoveHelper();
    }

    @Override
    public void setMoveTo(double x, double y, double z, double speedIn) {
      if (theEntity.isInWater()) {
        speedIn = 1.5D;
      }

      super.setMoveTo(x, y, z, speedIn);

      if (speedIn > 0.0D) {
        movementSpeed = speedIn;
      }
    }
  }
}

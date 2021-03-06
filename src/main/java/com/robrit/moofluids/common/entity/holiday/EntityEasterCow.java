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
import net.minecraft.pathfinding.Path;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEasterCow extends EntityCow implements INamedEntity {

  public static final String ENTITY_NAME = "EntityEasterCow";
  private int jumpTicks;
  private int jumpDuration;
  private boolean wasOnGround;
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
    tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.GOLDEN_CARROT, false));
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
    return new EntityEasterCow(world);
  }

  @Override
  protected float getJumpUpwardsMotion() {
    if (!collidedHorizontally && (!moveHelper.isUpdating() || moveHelper.getY() <= posY + 0.5D)) {
      final Path path = navigator.getPath();

      if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength()) {
        final Vec3d vec3d = path.getPosition(this);

        if (vec3d.y > posY) {
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
        moveRelative(0.0F, 0.0F, 1.0F, 0.1F);
      }
    }

    if (!world.isRemote) {
      world.setEntityState(this, (byte) 1);
    }
  }

  public void setMovementSpeed(double newSpeed) {
    getNavigator().setSpeed(newSpeed);
    moveHelper.setMoveTo(moveHelper.getX(), moveHelper.getY(), moveHelper.getZ(), newSpeed);
  }

  @Override
  public void setJumping(boolean jumping) {
    super.setJumping(jumping);

    if (jumping && !isInWater()) {
      playSound(getJumpSound(), getSoundVolume(),
                ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
    }
  }

  public void startJumping() {
    setJumping(true);
    jumpDuration = 10;
    jumpTicks = 0;
  }

  @Override
  public void updateAITasks() {
    if (currentMoveTypeDuration > 0) {
      --currentMoveTypeDuration;
    }

    if (onGround) {
      if (!wasOnGround) {
        setJumping(false);
        checkLandingDelay();
      }

      if (currentMoveTypeDuration == 0) {
        final EntityLivingBase entityLivingBase = getAttackTarget();

        if (entityLivingBase != null && getDistanceSq(entityLivingBase) < 16.0D) {
          calculateRotationYaw(entityLivingBase.posX, entityLivingBase.posZ);
          moveHelper.setMoveTo(entityLivingBase.posX, entityLivingBase.posY,
                               entityLivingBase.posZ, moveHelper.getSpeed());
          startJumping();
          wasOnGround = true;
        }
      }

      final EntityEasterCow.JumpHelper
          entityEasterCow$jumpHelper =
          (EntityEasterCow.JumpHelper) jumpHelper;

      if (!entityEasterCow$jumpHelper.getIsJumping()) {
        if (moveHelper.isUpdating() && currentMoveTypeDuration == 0) {
          final Path path = navigator.getPath();
          Vec3d vec3d = new Vec3d(moveHelper.getX(), moveHelper.getY(), moveHelper.getZ());

          if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength()) {
            vec3d = path.getPosition(this);
          }

          calculateRotationYaw(vec3d.x, vec3d.z);
          startJumping();
        }
      } else if (!entityEasterCow$jumpHelper.canJump()) {
        enableJumpControl();
      }
    }

    wasOnGround = onGround;
  }

  private void calculateRotationYaw(double x, double z) {
    rotationYaw = (float) (MathHelper.atan2(z - posZ, x - posX) * (180D / Math.PI)) - 90.0F;
  }

  private void enableJumpControl() {
    ((EntityEasterCow.JumpHelper) jumpHelper).setCanJump(true);
  }

  private void disableJumpControl() {
    ((EntityEasterCow.JumpHelper) jumpHelper).setCanJump(false);
  }

  private void updateMoveTypeDuration() {
    if (moveHelper.getSpeed() < 2.2D) {
      currentMoveTypeDuration = 10;
    } else {
      currentMoveTypeDuration = 1;
    }
  }

  private void checkLandingDelay() {
    updateMoveTypeDuration();
    disableJumpControl();
  }

  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    if (jumpTicks != jumpDuration) {
      ++jumpTicks;
    } else if (this.jumpDuration != 0) {
      jumpTicks = 0;
      jumpDuration = 0;
      setJumping(false);
    }
  }

  protected SoundEvent getJumpSound() {
    return SoundEvents.ENTITY_HORSE_JUMP;
  }

  @SideOnly(Side.CLIENT)
  public void handleStatusUpdate(byte id) {
    if (id == 1) {
      this.createRunningParticles();
      this.jumpDuration = 10;
      this.jumpTicks = 0;
    } else {
      super.handleStatusUpdate(id);
    }
  }

  private class JumpHelper extends EntityJumpHelper {

    private EntityEasterCow theEntity;
    private boolean canJump;

    public JumpHelper(EntityEasterCow entity) {
      super(entity);
      theEntity = entity;
    }

    public boolean getIsJumping() {
      return isJumping;
    }

    public boolean canJump() {
      return canJump;
    }

    public void setCanJump(boolean p_180066_1_) {
      canJump = p_180066_1_;
    }

    @Override
    public void doJump() {
      if (isJumping) {
        theEntity.startJumping();
        isJumping = false;
      }
    }
  }

  private static class MoveHelper extends EntityMoveHelper {

    private EntityEasterCow theEntity;
    private double nextJumpSpeed;

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
        theEntity.setMovementSpeed(nextJumpSpeed);
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
        nextJumpSpeed = speedIn;
      }
    }
  }
}

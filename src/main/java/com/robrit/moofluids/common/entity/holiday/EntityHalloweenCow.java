/*
 * EntityHalloweenCow.java
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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityHalloweenCow extends EntityCow implements INamedEntity {

  private static final String ENTITY_NAME = "EntityHalloweenCow";

  public EntityHalloweenCow(final World world) {
    super(world);
  }

  @Override
  protected void initEntityAI() {
    super.initEntityAI();
    tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
    applyEntityAI();
  }

  protected void applyEntityAI() {
    this.tasks.addTask(4, new EntityHalloweenCow.AICowAttack(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>
        (this, EntityPlayer.class, true));
    targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityVillager>
        (this, EntityVillager.class, false));
    targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityIronGolem>
        (this, EntityIronGolem.class, true));
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
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
  public void onLivingUpdate() {
    super.onLivingUpdate();
  }

  @Override
  public EntityHalloweenCow createChild(EntityAgeable entityAgeable) {
    return new EntityHalloweenCow(worldObj);
  }

  @Override
  public boolean attackEntityAsMob(Entity entityIn) {
    return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0F);
  }

  private static class AICowAttack extends EntityAIAttackMelee {

    public AICowAttack(EntityHalloweenCow entity) {
      super(entity, 1.4D, true);
    }

    @Override
    protected double func_179512_a(EntityLivingBase attackTarget) {
      return (double) (4.0F + attackTarget.width);
    }
  }
}

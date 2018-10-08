/*
 * ConfigurationHandler.java
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

package com.robrit.moofluids.common.event;

import com.robrit.moofluids.common.entity.EntityTypeData;
import com.robrit.moofluids.common.ref.ConfigurationData;
import com.robrit.moofluids.common.ref.ModInformation;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigurationHandler {

  private static Configuration configuration;
  private static File configFile;

  public static void init() {
    setConfiguration(new Configuration(configFile));
  }

  public static void updateConfiguration() {
    updateGlobalConfiguration();
    updateFluidConfiguration();
  }

  public static void updateGlobalConfiguration() {
    try {
      configuration.load();

      /* Category comments */
      configuration.addCustomCategoryComment(ConfigurationData.GLOBAL_FLUID_COW_SPAWN_RATE_KEY,
                                             ConfigurationData.GLOBAL_FLUID_COW_SPAWN_RATE_COMMENT);

      ConfigurationData.GLOBAL_FLUID_COW_SPAWN_RATE_VALUE =
          configuration.get(ConfigurationData.GLOBAL_FLUID_COW_SPAWN_RATE_KEY,
                            ConfigurationData.GLOBAL_FLUID_COW_SPAWN_RATE_KEY,
                            ConfigurationData.GLOBAL_FLUID_COW_SPAWN_RATE_DEFAULT_VALUE).getInt();

      ConfigurationData.EVENT_ENTITIES_ENABLED_VALUE =
          configuration.get(ConfigurationData.EVENT_ENTITIES_ENABLED_KEY,
                            ConfigurationData.EVENT_ENTITIES_ENABLED_KEY,
                            ConfigurationData.EVENT_ENTITIES_ENABLED_DEFAULT_VALUE).getBoolean();
    } catch (Exception exception) {
      LogHelper.error("Unable to read configuration for " + ModInformation.MOD_NAME);
      LogHelper.error(exception);
    } finally {
      if (configuration.hasChanged()) {
        configuration.save();
      }
    }
  }

  public static void updateFluidConfiguration() {
    try {
      configuration.load();

      for (final Fluid containableFluid : EntityHelper.getContainableFluids().values()) {
        final String containableFluidLocalizedName =
            containableFluid.getLocalizedName(new FluidStack(containableFluid, 0));
        final String entityName = containableFluidLocalizedName + " " + "Cow";
        final EntityTypeData entityTypeData = new EntityTypeData();

        /* Configurable entity data */
        entityTypeData.setSpawnable(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_IS_SPAWNABLE_KEY,
                              ConfigurationData.ENTITY_IS_SPAWNABLE_DEFAULT_VALUE).getBoolean());
        entityTypeData.setSpawnRate(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_SPAWN_RATE_KEY,
                              ConfigurationData.ENTITY_SPAWN_RATE_DEFAULT_VALUE).getInt());
        entityTypeData.setNormalDamageAmount(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_NORMAL_DAMAGE_AMOUNT_KEY,
                              ConfigurationData.ENTITY_NORMAL_DAMAGE_AMOUNT_DEFAULT_VALUE)
                .getInt());
        entityTypeData.setFireDamageAmount(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_FIRE_DAMAGE_AMOUNT_KEY,
                              ConfigurationData.ENTITY_FIRE_DAMAGE_AMOUNT_DEFAULT_VALUE).getInt());
        entityTypeData.setNormalDamageAmount(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_NORMAL_DAMAGE_AMOUNT_KEY,
                              ConfigurationData.ENTITY_NORMAL_DAMAGE_AMOUNT_DEFAULT_VALUE)
                .getInt());
        entityTypeData.setGrowUpTime(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_GROW_UP_TIME_KEY,
                              ConfigurationData.ENTITY_GROW_UP_TIME_DEFAULT_VALUE).getInt());
        entityTypeData.setMaxUseCooldown(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_MAX_USE_COOLDOWN_KEY,
                              ConfigurationData.ENTITY_MAX_USE_COOLDOWN_DEFAULT_VALUE).getInt());
        entityTypeData.setMaxAutomationCooldown(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_MAX_AUTOMATION_COOLDOWN_KEY,
                              ConfigurationData.ENTITY_MAX_AUTOMATION_COOLDOWN_DEFAULT_VALUE)
                .getInt());
        entityTypeData.setDamagePlayers(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_CAN_DAMAGE_PLAYER_KEY,
                              ConfigurationData.ENTITY_CAN_DAMAGE_PLAYER_DEFAULT_VALUE)
                .getBoolean());
        entityTypeData.setDamageEntities(
            configuration.get(entityName,
                              ConfigurationData.ENTITY_CAN_DAMAGE_OTHER_ENTITIES_KEY,
                              ConfigurationData.ENTITY_CAN_DAMAGE_OTHER_ENTITIES_DEFAULT_VALUE)
                .getBoolean());

        /* Non-configurable entity data */
        entityTypeData.setCauseFireDamage(entityTypeData.getFireDamageAmount() > 0);
        entityTypeData.setCauseNormalDamage(entityTypeData.getNormalDamageAmount() > 0);

        EntityHelper.setEntityData(containableFluid.getName(), entityTypeData);
      }
    } catch (Exception exception) {
      LogHelper.error("Unable to read configuration for " + ModInformation.MOD_NAME);
      LogHelper.error(exception);
    } finally {
      if (configuration.hasChanged()) {
        configuration.save();
      }
    }
  }

  public static Configuration getConfiguration() {
    return configuration;
  }

  public static void setConfiguration(Configuration newConfiguration) {
    configuration = newConfiguration;
  }

  public static File getConfigFile() {
    return configFile;
  }

  public static void setConfigFile(File newConfigFile) {
    configFile = newConfigFile;
  }

  @SubscribeEvent
  public static void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equalsIgnoreCase(ModInformation.MOD_ID)) {
      updateConfiguration();
    }
  }
}

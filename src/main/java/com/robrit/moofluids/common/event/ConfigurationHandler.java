/*
 * ConfigurationHandler.java
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

package com.robrit.moofluids.common.event;

import com.robrit.moofluids.common.entity.EntityTypeData;
import com.robrit.moofluids.common.util.EntityHelper;
import com.robrit.moofluids.common.util.LogHelper;
import com.robrit.moofluids.common.util.ModInformation;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.File;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ConfigurationHandler {

  /* Configuration keys */
  private static final String ENTITY_IS_SPAWNABLE_KEY = "Is Spawnable?";
  private static final String ENTITY_SPAWN_RATE_KEY = "Spawn Rate";
  private static final String ENTITY_NORMAL_DAMAGE_AMOUNT_KEY = "Normal Damage Amount";
  private static final String ENTITY_FIRE_DAMAGE_AMOUNT_KEY = "Fire Damage Amount";
  private static final String ENTITY_GROW_UP_TIME_KEY = "Grow Up Time";
  private static final String ENTITY_MAX_USE_COOLDOWN_KEY = "Max Use Cooldown";

  /* Configuration default values */
  private static final boolean ENTITY_IS_SPAWNABLE_DEFAULT_VALUE = true;
  private static final int ENTITY_SPAWN_RATE_DEFAULT_VALUE = 8;
  private static final int ENTITY_NORMAL_DAMAGE_AMOUNT_DEFAULT_VALUE = 0;
  private static final int ENTITY_FIRE_DAMAGE_AMOUNT_DEFAULT_VALUE = 2;
  private static final int ENTITY_GROW_UP_TIME_DEFAULT_VALUE = 8000; /* Quarter of a MC day */
  private static final int ENTITY_MAX_USE_COOLDOWN_DEFAULT_VALUE = 4000; /* Eighth of a MC day */

  private static Configuration configuration;
  private static File configFile;

  public static void init() {
    setConfiguration(new Configuration(configFile));
  }

  public static void updateConfiguration() {
    try {
      configuration.load();

      for (final Fluid containableFluid : EntityHelper.getContainableFluids().values()) {
        final String containableFluidLocalizedName = containableFluid.getLocalizedName();
        final String
            localizationForCow =
            LanguageRegistry.instance().getStringLocalization("entity.Cow.name");
        final String entityName = containableFluidLocalizedName + " " + localizationForCow;

        final EntityTypeData entityTypeData = new EntityTypeData();

        /* Configurable entity data */

        entityTypeData.setSpawnable(
            configuration.get(entityName,
                              ENTITY_IS_SPAWNABLE_KEY,
                              ENTITY_IS_SPAWNABLE_DEFAULT_VALUE)
                .getBoolean());
        entityTypeData.setSpawnRate(
            configuration.get(entityName,
                              ENTITY_SPAWN_RATE_KEY,
                              ENTITY_SPAWN_RATE_DEFAULT_VALUE)
                .getInt());
        entityTypeData.setNormalDamageAmount(
            configuration.get(entityName,
                              ENTITY_NORMAL_DAMAGE_AMOUNT_KEY,
                              ENTITY_NORMAL_DAMAGE_AMOUNT_DEFAULT_VALUE)
                .getInt());
        entityTypeData.setFireDamageAmount(
            configuration
                .get(entityName,
                     ENTITY_FIRE_DAMAGE_AMOUNT_KEY,
                     ENTITY_FIRE_DAMAGE_AMOUNT_DEFAULT_VALUE)
                .getInt());

        entityTypeData.setGrowUpTime(
            configuration.get(entityName,
                              ENTITY_GROW_UP_TIME_KEY,
                              ENTITY_GROW_UP_TIME_DEFAULT_VALUE)
                .getInt());
        entityTypeData.setMaxUseCooldown(
            configuration.get(entityName,
                              ENTITY_MAX_USE_COOLDOWN_KEY,
                              ENTITY_MAX_USE_COOLDOWN_DEFAULT_VALUE)
                .getInt());

        /* Non-configurable entity data */

        entityTypeData.setCauseFireDamage(
            containableFluid.getTemperature() >= FluidRegistry.LAVA.getTemperature());

        entityTypeData.setHeavy(
            containableFluid.getDensity() > FluidRegistry.WATER.getDensity() * 3);

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
    if (event.modID.equalsIgnoreCase(ModInformation.MOD_ID)) {
      updateConfiguration();
    }
  }
}

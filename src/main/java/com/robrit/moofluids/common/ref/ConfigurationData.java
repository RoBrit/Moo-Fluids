/*
 * ConfigurationData.java
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

package com.robrit.moofluids.common.ref;

public class ConfigurationData {

  /* Configuration keys */
  public static final String GLOBAL_FLUID_COW_SPAWN_RATE_KEY = "Fluid Cow Global Spawn Rate";
  public static final String ENTITY_IS_SPAWNABLE_KEY = "Is Spawnable?";
  public static final String ENTITY_SPAWN_RATE_KEY = "Spawn Rate";
  public static final String ENTITY_NORMAL_DAMAGE_AMOUNT_KEY = "Normal Damage Amount";
  public static final String ENTITY_FIRE_DAMAGE_AMOUNT_KEY = "Fire Damage Amount";
  public static final String ENTITY_GROW_UP_TIME_KEY = "Grow Up Time";
  public static final String ENTITY_MAX_USE_COOLDOWN_KEY = "Max Use Cooldown";
  public static final String ENTITY_MAX_AUTOMATION_COOLDOWN_KEY = "Max Automation Cooldown";
  public static final String ENTITY_CAN_DAMAGE_PLAYER_KEY = "Can Damage Player";
  public static final String ENTITY_CAN_DAMAGE_OTHER_ENTITIES_KEY = "Can Damage Other Entities";
  public static final String EVENT_ENTITIES_ENABLED_KEY = "Event Entities Enabled";

  /* Configuration default values */
  public static final int GLOBAL_FLUID_COW_SPAWN_RATE_DEFAULT_VALUE = 8;
  public static final boolean ENTITY_IS_SPAWNABLE_DEFAULT_VALUE = true;
  public static final int ENTITY_SPAWN_RATE_DEFAULT_VALUE = 100;
  public static final int ENTITY_NORMAL_DAMAGE_AMOUNT_DEFAULT_VALUE = 0;
  public static final int ENTITY_FIRE_DAMAGE_AMOUNT_DEFAULT_VALUE = 0;
  public static final int ENTITY_GROW_UP_TIME_DEFAULT_VALUE = 8000; /* Quarter of a MC day */
  public static final int ENTITY_MAX_USE_COOLDOWN_DEFAULT_VALUE = 4000; /* Eighth of a MC day */
  public static final int ENTITY_MAX_AUTOMATION_COOLDOWN_DEFAULT_VALUE = 4000;
  public static final boolean ENTITY_CAN_DAMAGE_PLAYER_DEFAULT_VALUE = true;
  public static final boolean ENTITY_CAN_DAMAGE_OTHER_ENTITIES_DEFAULT_VALUE = true;
  public static final boolean EVENT_ENTITIES_ENABLED_DEFAULT_VALUE = true;

  /* Configuration values */
  public static int GLOBAL_FLUID_COW_SPAWN_RATE_VALUE;
  public static boolean EVENT_ENTITIES_ENABLED_VALUE;

  /* Configuration comments */
  public static final String
      GLOBAL_FLUID_COW_SPAWN_RATE_COMMENT =
      "The chance of Fluid Cows spawning versus other entities. (8 is the same chance as normal Cows)";
}

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

  public static final String CONFIG_VERSION = "1";

  /* Configuration categories */
  public static final String CATEGORY_GLOBAL = "Global";
  public static final String CATEGORY_FLUIDS = "Individual Fluids";
  public static final String CATEGORY_FLUID_FILTER = ConfigurationData.CATEGORY_GLOBAL + "." + "Fluid Filter";

  /* Configuration keys */
  public static final String GLOBAL_FLUID_COW_SPAWN_RATE_KEY = "Fluid Cow Global Spawn Rate";
  public static final String ENTITY_IS_SPAWNABLE_KEY = "Is Spawnable?";
  public static final String ENTITY_SPAWN_RATE_KEY = "Spawn Rate";
  public static final String ENTITY_NORMAL_DAMAGE_AMOUNT_KEY = "Normal Damage Amount";
  public static final String ENTITY_FIRE_DAMAGE_AMOUNT_KEY = "Fire Damage Amount";
  public static final String ENTITY_GROW_UP_TIME_KEY = "Grow Up Time";
  public static final String ENTITY_MAX_USE_COOLDOWN_KEY = "Max Use Cooldown";
  public static final String ENTITY_CAN_DAMAGE_PLAYER_KEY = "Can Damage Player";
  public static final String ENTITY_CAN_DAMAGE_OTHER_ENTITIES_KEY = "Can Damage Other Entities";
  public static final String EVENT_ENTITIES_ENABLED_KEY = "Event Entities Enabled";
  public static final String FILTER_TYPE_KEY = "Blacklist";
  public static final String FILTER_LIST_KEY = "Fluids";

  /* Configuration default values */
  public static final int GLOBAL_FLUID_COW_SPAWN_RATE_DEFAULT_VALUE = 8;
  public static final boolean ENTITY_IS_SPAWNABLE_DEFAULT_VALUE = true;
  public static final int ENTITY_SPAWN_RATE_DEFAULT_VALUE = 100;
  public static final int ENTITY_NORMAL_DAMAGE_AMOUNT_DEFAULT_VALUE = 0;
  public static final int ENTITY_FIRE_DAMAGE_AMOUNT_DEFAULT_VALUE = 0;
  public static final int ENTITY_GROW_UP_TIME_DEFAULT_VALUE = 8000; /* Quarter of a MC day */
  public static final int ENTITY_MAX_USE_COOLDOWN_DEFAULT_VALUE = 4000; /* Eighth of a MC day */
  public static final boolean ENTITY_CAN_DAMAGE_PLAYER_DEFAULT_VALUE = true;
  public static final boolean ENTITY_CAN_DAMAGE_OTHER_ENTITIES_DEFAULT_VALUE = true;
  public static final boolean EVENT_ENTITIES_ENABLED_DEFAULT_VALUE = true;
  public static final boolean FILTER_TYPE_DEFAULT = true;
  public static final String[] FILTER_LIST_DEFAULT ={};

  /* Configuration values */
  public static int GLOBAL_FLUID_COW_SPAWN_RATE_VALUE;
  public static boolean EVENT_ENTITIES_ENABLED_VALUE;

  /* Configuration comments */
  public static final String CATEGORY_GLOBAL_COMMENT = "Global settings.";
  public static final String CATEGORY_FLUIDS_COMMENT = "Settings for each type of Fluid Cow.";
  public static final String FILTER_TYPE_COMMENT =
          "If true, cows that match this list won't spawn." + System.lineSeparator() +
          "If false, ONLY cows that match this list will spawn." + System.lineSeparator() +
          "This overrides the \"" + ENTITY_IS_SPAWNABLE_KEY + "\" setting on the individual cows below.";
  public static final String FILTER_LIST_COMMENT =
          "This will compare against the internal name, unlocalized name, and localized name of a fluid." +
           System.lineSeparator() + "If any of those contain an entry on this list, it will be filtered.";
    ;

  public static final String
      GLOBAL_FLUID_COW_SPAWN_RATE_COMMENT =
      "The chance of Fluid Cows spawning versus other entities. (8 is the same chance as normal Cows)";
}

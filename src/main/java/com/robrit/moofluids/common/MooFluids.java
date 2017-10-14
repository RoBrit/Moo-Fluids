/*
 * MooFluids.java
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

package com.robrit.moofluids.common;

import com.robrit.moofluids.common.event.ConfigurationHandler;
import com.robrit.moofluids.common.proxy.IProxy;
import com.robrit.moofluids.common.ref.ModInformation;
import com.robrit.moofluids.common.util.LogHelper;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModInformation.MOD_ID, name = ModInformation.MOD_NAME,
     version = ModInformation.MOD_VERSION, dependencies = ModInformation.MOD_DEPENDENCIES,
     certificateFingerprint = ModInformation.MOD_FINGERPRINT)
public class MooFluids {

  @Mod.Instance(ModInformation.MOD_ID)
  private static MooFluids instance;

  @SidedProxy(clientSide = ModInformation.PROXY_CLIENT_LOCATION,
              serverSide = ModInformation.PROXY_SERVER_LOCATION)
  private static IProxy proxy;

  public static MooFluids getInstance() {
    return instance;
  }

  public static IProxy getProxy() {
    return proxy;
  }

  // Functions to be called before any event lifecycle
  static {
    FluidRegistry.enableUniversalBucket();
    FluidRegistry.addBucketForFluid(FluidRegistry.WATER);
    FluidRegistry.addBucketForFluid(FluidRegistry.LAVA);
  }

  @Mod.EventHandler
  public static void preInit(FMLPreInitializationEvent event) {
    ConfigurationHandler.setConfigFile(event.getSuggestedConfigurationFile());
    ConfigurationHandler.init();
    ConfigurationHandler.updateGlobalConfiguration();
    proxy.initContainableFluids();
    ConfigurationHandler.updateFluidConfiguration();
    proxy.registerEntities();
    proxy.registerEntitySpawns();
    proxy.registerEventHandlers();

    if (ModInformation.DEBUG_MODE) {
      LogHelper.info(String.format("Finished pre-initialisation stage for %s",
                                   ModInformation.MOD_ID));
    }
  }

  @Mod.EventHandler
  public static void init(FMLInitializationEvent event) {
    proxy.registerPlugins();

    if (ModInformation.DEBUG_MODE) {
      LogHelper.info(String.format("Finished initialisation stage for %s", ModInformation.MOD_ID));
    }
  }

  @Mod.EventHandler
  public static void postInit(FMLPostInitializationEvent event) {
    if (ModInformation.DEBUG_MODE) {
      LogHelper.info(String.format("Finished post-initialisation stage for %s",
                                   ModInformation.MOD_ID));
    }
  }

  @Mod.EventHandler
  public void invalidFingerprint(FMLFingerprintViolationEvent event) {
    if (ModInformation.MOD_FINGERPRINT.equals("@FINGERPRINT@")) {
      LogHelper.error("No fingerprint found!");
    } else {
      LogHelper.warn("Invalid fingerprint found!");
    }
  }
}

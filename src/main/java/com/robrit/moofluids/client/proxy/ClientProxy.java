/*
 * ClientProxy.java
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

package com.robrit.moofluids.client.proxy;

import com.robrit.moofluids.client.event.TextureRegisterEvent;
import com.robrit.moofluids.client.render.RenderEventCow;
import com.robrit.moofluids.client.render.RenderFluidCow;
import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.entity.holiday.EntityChristmasCow;
import com.robrit.moofluids.common.entity.holiday.EntityEasterCow;
import com.robrit.moofluids.common.entity.holiday.EntityHalloweenCow;
import com.robrit.moofluids.common.entity.holiday.EntityNewYearsCow;
import com.robrit.moofluids.common.entity.holiday.EntityValentinesCow;
import com.robrit.moofluids.common.proxy.CommonProxy;
import com.robrit.moofluids.common.ref.ConfigurationData;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

  @Override
  public void registerEventHandlers() {
    super.registerEventHandlers();
    MinecraftForge.EVENT_BUS.register(new TextureRegisterEvent());
  }

  @Override
  public void registerEntities() {
    super.registerEntities();
    RenderingRegistry.registerEntityRenderingHandler(EntityFluidCow.class,
                                                     new RenderFluidCow.Factory());

    if (ConfigurationData.EVENT_ENTITIES_ENABLED_VALUE) {
      final RenderEventCow.Factory renderFactory = new RenderEventCow.Factory();
      RenderingRegistry.registerEntityRenderingHandler(EntityChristmasCow.class, renderFactory);
      RenderingRegistry.registerEntityRenderingHandler(EntityEasterCow.class, renderFactory);
      RenderingRegistry.registerEntityRenderingHandler(EntityHalloweenCow.class, renderFactory);
      RenderingRegistry.registerEntityRenderingHandler(EntityNewYearsCow.class, renderFactory);
      RenderingRegistry.registerEntityRenderingHandler(EntityValentinesCow.class, renderFactory);
    }
  }
}

/**
 * Copyright (c) 2012-2013 William Karnavas 
 * All Rights Reserved
 */

/**
 * 
 * This file is part of MineTunes.
 * 
 * MineTunes is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * MineTunes is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MineTunes. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.minetunes.forgeLauncher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "MineTunes", name = "MineTunes", version = "3.10", useMetadata = true)
public class MinetunesForgeLauncher {

	@Instance("MineTunes")
	public static MinetunesForgeLauncher instance = new MinetunesForgeLauncher();

	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		try {
			Class launcherClass = Class.forName("com.minetunes.ForgeLauncher");
			Method launchMethod = launcherClass.getMethod("launch");
			launchMethod.invoke(null);
			System.out.println("MineTunes launched successfully!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("MineTunes: ForgeLauncher class not found!");
			System.err
					.println("Please install the rest of MineTunes before trying again.");
			System.err.println("MINETUNES FAILED TO LOAD");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

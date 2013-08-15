/**
 * Copyright (c) 2013 William Karnavas 
 * All Rights Reserved
 * 
 * This file is part of SavoyCraft.
 * 
 * SavoyCraft is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SavoyCraft is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SavoyCraft. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.minetunes.tempoGui;

import net.minecraft.src.Minecraft;

/**
 *
 */
public class FontRendererUtil {
	/**
	 * 
	 * @param input
	 *            null-safe
	 * @param pixels
	 * @return
	 */
	public static String trimToLength(String input, int pixels) {
		if (input == null) {
			return null;
		}
		if (Minecraft.getMinecraft().fontRenderer.getStringWidth(input) <= pixels) {
			return input;
		}
		StringBuilder sb = new StringBuilder();
		int pos = 0;
		while (Minecraft.getMinecraft().fontRenderer.getStringWidth(sb
				.toString()) < pixels && pos < input.length()) {
			sb.append(input.charAt(pos));
			pos++;
		}
		// Remove extra char that tripped loop end condition
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}

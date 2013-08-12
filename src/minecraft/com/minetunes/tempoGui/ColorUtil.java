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

import org.lwjgl.opengl.GL11;

/**
 * Converters, based around converting to and from rgba float arrays to
 * variously arranged int representations.
 */
public class ColorUtil {

	/**
	 * 
	 * @param color
	 *            a r g b
	 * @return r g b a
	 */
	public static float[] argbIntTo4f(int color) {
		return new float[] { (float) ((color & 0x00ff0000) >> 16) / 255f,
				(float) ((color & 0x0000ff00) >> 8) / 255f,
				(float) ((color & 0x000000ff) >> 0) / 255f,
				(float) ((color & 0xff000000L) >> 24) / 255f };
	}

	/**
	 * 
	 * @param color
	 *            r g b a
	 * @return r g b a
	 */
	public static float[] rgbaIntTo4f(int color) {
		return new float[] { (float) ((color & 0xff000000L) >> 24) / 255f,
				(float) ((color & 0x00ff0000) >> 16) / 255f,
				(float) ((color & 0x0000ff00) >> 8) / 255f,
				(float) ((color & 0x000000ff) >> 0) / 255f };
	}

	/**
	 * 
	 * @param color
	 *            r g b
	 * @return r g b a
	 */
	public static float[] rgbIntTo4f(int color) {
		return new float[] { (float) ((color & 0x00ff0000) >> 16) / 255f,
				(float) ((color & 0x0000ff00) >> 8) / 255f,
				(float) ((color & 0x000000ff) >> 0) / 255f, 1 };
	}

	/**
	 * 
	 * @param color
	 *            r g b a
	 * @return r g b a
	 */
	public static int floatsToRgbaInt(float[] color) {
		return (int) (color[0] * 255.0) << 24 + (int) (color[1] * 255.0) << 16 + (int) (color[2] * 255.0) << 8 + (int) (color[3] * 255.0) << 0;
	}

	/**
	 * 
	 * @param color
	 *            r g b a
	 * @return r g b
	 */
	public static int floatsToRgbInt(float[] color) {
		return (int) (color[0] * 255.0) << 16 + (int) (color[1] * 255.0) << 8 + (int) (color[2] * 255.0) << 0;
	}

	/**
	 * 
	 * @param color
	 *            r g b a
	 * @return a r g b
	 */
	public static int floatsToArgbInt(float[] color) {
		return (int) (color[3] * 255.0) << 24 + (int) (color[0] * 255.0) << 16 + (int) (color[1] * 255.0) << 8 + (int) (color[2] * 255.0) << 0;
	}

	public static void setGLColor(int argbColor) {
		float[] c = argbIntTo4f(argbColor);
		GL11.glColor4f(c[0], c[1], c[2], c[3]);
	}

}

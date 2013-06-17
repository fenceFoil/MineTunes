/**
 * Copyright (c) 2013 William Karnavas 
 * All Rights Reserved
 * 
 * This file is part of SignWatcher.
 * 
 * SignWatcher is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SignWatcher is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SignWatcher. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.fencefoil.signWatcher;

import java.util.Arrays;

/**
 * A sign's basic attributes. Not as complete as a SignTileEntity, and used when
 * we only know some aspects of a sign's data.
 * 
 * @since 0.5
 * 
 */
public class Sign {

	private String[] text;
	private int x, y, z;

	public Sign(String[] text, int x, int y, int z) {
		super();
		this.text = text;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Sign [text=" + Arrays.toString(text) + ", x=" + x + ", y=" + y
				+ ", z=" + z + "]";
	}
}

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
package com.minetunes.signs.keywords;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

import com.minetunes.Point3D;
import com.minetunes.ditty.Ditty;

/**
 * @author William
 * 
 */
public class LoudKeyword extends SignTuneKeyword {

	/**
	 * @param wholeKeyword
	 */
	public LoudKeyword(String wholeKeyword) {
		super(wholeKeyword);
	}

	@Override
	public Point3D execute(Ditty ditty, Point3D location,
			TileEntitySign signTileEntity, Point3D nextSign, World world,
			StringBuilder readMusicString) {
		// Word currently has no effect; this could change
		// sometime
		ditty.setLoud(true);
		return null;
	}

}

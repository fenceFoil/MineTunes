/**
 * Copyright (c) 2012-2013 William Karnavas 
 * All Rights Reserved
 */

/**
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

import java.io.DataInput;
import java.io.IOException;

/**
 * Provides a hook for when new signs are created
 * 
 */
public class Packet130UpdateSignHooked extends Packet130UpdateSign {

	@Override
	public void readPacketData(DataInput par1DataInputStream)
			throws IOException {
		super.readPacketData(par1DataInputStream);

		SignWatcher.onSignReadFromPacket(this.xPosition, this.yPosition,
				this.zPosition, this.signLines);
	}

}

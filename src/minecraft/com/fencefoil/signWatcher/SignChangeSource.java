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

public enum SignChangeSource {
	/**
	 * Sign was received from a server packet.
	 */
	PACKET_CREATED,
	/**
	 * The player closed the sign editor gui, and a sign change event was sent
	 * in response. Note that a packet is sent in this process as well, so a
	 * PACKET_CREATED may also be sent in this situation, possibly instead of
	 * this source.
	 */
	SIGN_EDITOR_CLOSED,
	/**
	 * Sign was noted as missing, removed, or unloaded in a manual check of
	 * signs in the world.
	 */
	MANUAL_CHECK_REMOVED,
	/**
	 * Sign was discovered in a manual check of signs in the world.
	 */
	MANUAL_CHECK_FOUND,
	/**
	 * A TileEntitySign was loaded from an NBT tag.
	 */
	LOADED_FROM_NBT,
	/**
	 *
	 */
	UNKNOWN,
	/**
	 * 
	 */
	OTHER
}

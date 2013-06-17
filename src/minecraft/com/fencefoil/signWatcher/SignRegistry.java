/**
 * Copyright (c) 2012 William Karnavas 
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

import java.util.HashSet;
import java.util.Map;

import net.minecraft.src.Packet;


/**
 * The main class of SignWatcher.
 * 
 * @since 0.5
 * 
 */
public class SignRegistry {
	private static HashSet<SignChangedListener> signChangedListeners = new HashSet<SignChangedListener>();

	/**
	 * This must be called as the game loads. It sets up the Sign Registry in
	 * general and replaces things in MineCraft with new versions containing
	 * hooks.
	 */
	public static void init() {
		// Set up modified sign update packet
		// Put it into Packet's directories of packet types, replacing the
		// normal packet

		// In an ideal world, we would use one line like this, but can't due to
		// visibility
		// Packet.packetClassToIdMap.put(Packet62LevelSoundMineTunes.class,
		// Integer.valueOf(62));

		// Instead, we use reflection
		try {
			Object packetClassToIdMapObj = Finder.getUniqueTypedFieldFromClass(
					Packet.class, Map.class, null);
			if (packetClassToIdMapObj != null) {
				Map packetClassToIdMap = (Map) packetClassToIdMapObj;
				packetClassToIdMap.put(Packet130UpdateSignHooked.class,
						Integer.valueOf(130));

				// Also put into the other map of packets
				// Only do this if the first map was found and added to
				// successfully
				Packet.packetIdToClassMap.addKey(130,
						Packet130UpdateSignHooked.class);
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addSignChangedListener(SignChangedListener l) {
		signChangedListeners.add(l);
	}

	public static void removeSignChangedListener(SignChangedListener l) {
		signChangedListeners.remove(l);
	}
	
	

	protected static void onSignReadFromPacket(int xPosition, int yPosition,
			int zPosition, String[] signLines) {
		
	}
}

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

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.minecraft.src.GuiEditSign;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntitySign;

import com.fencefoil.signWatcher.interfaces.SignChangedListener;

/**
 * The main class of SignWatcher. To use, call init() as your mod loads early in
 * the game, preferably before the first world is loaded. Then, register an
 * instance of SignChangedListener with this class to receive events whenever a
 * sign is changed or loaded in the world.<br>
 * <br>
 * This library is designed to be Forge-independent, client-side only.<br>
 * <br>
 * As of Minecraft 1.5.2 (June 2013), there are dependable ways to detect sign
 * changes. In <b>Multiplayer</b>, all changes (except removals) can be detected
 * by watching for 'Packet130UpdateSign'. In singleplayer, the packet-hook
 * events do not occur at all. In <b>SinglePlayer</b>, you must call
 * scanWorldForSignsManually to check the world for added and removed sign tile
 * entities. This method works in multiplayer too, and (over the packet hook)
 * tells you when signs are unloaded. <br>
 * <br>
 * Alternatively, if you modify TileEntitySign in your mod, there is an
 * alternative and more effiecent route. (This is NOT RECOMMENDED for many
 * reasons, among them being that the mod MineTunes will stomp your override
 * flat and eat its hat) or extend it (same disclaimer)). The alternative is to
 * extend onLoadFromNBT (or similar) in TileEntitySign to call super(..) and
 * then "onSignReadFromNBT" in this class. If you do that, and manually check
 * for the sign editor closing and note a change to that sign as well, you do
 * not need to call scanWorldForSignsManually.<br>
 * <br>
 * In summary, this library (if the manual method is used) will inform you of:<br>
 * Newly loaded signs (manual, packethook)<br>
 * Unloading signs (manual)<br>
 * Signs that the player has finished editing(manual, packethook)<br>
 * Signs that other players have finished editing (packethook)<br>
 * And possibly more.<br>
 * 
 * @since 0.5
 * 
 */
public class SignWatcher {
	public static int getMinorVersion() {
		return 0;
	}

	public static int getMajorVersion() {
		return 1;
	}

	private static HashSet<SignChangedListener> signChangedListeners = new HashSet<SignChangedListener>();

	private static HashSet<TileEntitySign> knownSigns = new HashSet<TileEntitySign>();

	private static boolean debugMessages = false;

	/**
	 * This must be called as the game loads. It sets up SignWatcher in general
	 * and replaces things in MineCraft with new versions containing hooks. It
	 * is okay to call this multiple times, or in multiple mods' load methods.
	 * 
	 * @since 0.5
	 */
	public static void init(boolean setUpHookedPacket) {
		if (setUpHookedPacket) {
			// Set up modified sign update packet
			// Put it into Packet's directories of packet types, replacing the
			// normal packet

			// In an ideal world, we would use one line like this, but can't due
			// to
			// visibility and a check to prevent overriding existing packets...
			// like
			// we're doing
			// Packet.packetClassToIdMap.put(Packet62LevelSoundMineTunes.class,
			// Integer.valueOf(62));

			// Instead, we use reflection to prod the relevant fields manually
			try {
				Object packetClassToIdMapObj = Finder
						.getUniqueTypedFieldFromClass(Packet.class, Map.class,
								null);
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
	}

	private static boolean signGuiOpenBefore = false;
	private static TileEntitySign signBeingEdited = null;

	/**
	 * This method will prompt change events for everything except <i>other
	 * players</i> editing their newly-placed signs. This is covered by the sign
	 * packet events, which require a call to init(true) as your mod loads and
	 * nothing else.
	 * 
	 * @since 0.5
	 */
	public static void scanWorldForSignsManually() {
		// Fail safely if anything in Minecraft is still null
		if (Minecraft.getMinecraft() == null
				|| Minecraft.getMinecraft().theWorld == null
				|| Minecraft.getMinecraft().theWorld.loadedTileEntityList == null) {
			return;
		}

		// Get a list of all loaded Sign Tile Entities in the world
		List tileEntitiesRaw = Minecraft.getMinecraft().theWorld.loadedTileEntityList;
		HashSet<TileEntitySign> tileEntitySigns = new HashSet<TileEntitySign>();
		for (int i = 0; i < tileEntitiesRaw.size(); i++) {
			Object o = tileEntitiesRaw.get(i);
			if (o instanceof TileEntitySign) {
				tileEntitySigns.add((TileEntitySign) o);
			}
		}

		HashSet<TileEntitySign> newSigns, removedSigns;

		// Search for new signs
		newSigns = (HashSet<TileEntitySign>) tileEntitySigns.clone();
		newSigns.removeAll(knownSigns);

		// Search for removed signs
		removedSigns = (HashSet<TileEntitySign>) knownSigns.clone();
		removedSigns.removeAll(tileEntitySigns);

		// React to findings
		for (TileEntitySign t : newSigns) {
			fireSignChangedEvent(new SignChangedEvent(new Sign(t.signText,
					t.xCoord, t.yCoord, t.zCoord),
					SignChangeSource.MANUAL_CHECK_FOUND));
		}
		for (TileEntitySign t : removedSigns) {
			fireSignChangedEvent(new SignChangedEvent(new Sign(t.signText,
					t.xCoord, t.yCoord, t.zCoord),
					SignChangeSource.MANUAL_CHECK_REMOVED));
		}

		// Update knownSigns
		knownSigns = tileEntitySigns;

		// Next, check for the sign gui closing
		boolean signGuiOpenRightNow = (Minecraft.getMinecraft().currentScreen instanceof GuiEditSign);
		if (signGuiOpenBefore && signGuiOpenRightNow == false) {
			// Throw a sign changed event for the sign being edited
			if (signBeingEdited != null) {
				fireSignChangedEvent(new SignChangedEvent(new Sign(
						signBeingEdited.signText, signBeingEdited.xCoord,
						signBeingEdited.yCoord, signBeingEdited.zCoord),
						SignChangeSource.SIGN_EDITOR_CLOSED));
			}
		}

		// Update sign gui open flat
		if (signGuiOpenRightNow) {
			GuiEditSign editor = (GuiEditSign) Minecraft.getMinecraft().currentScreen;
			TileEntitySign signEntity = null;
			try {
				signEntity = (TileEntitySign) Finder
						.getUniqueTypedFieldFromClass(GuiEditSign.class,
								TileEntitySign.class, editor);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (signEntity != null) {
				signBeingEdited = signEntity;
			}
		}
		signGuiOpenBefore = signGuiOpenRightNow;
	}

	/**
	 * @since 0.5
	 * @param l
	 */
	public static void addSignChangedListener(SignChangedListener l) {
		signChangedListeners.add(l);
	}

	/**
	 * @since 0.5
	 * @param l
	 */
	public static void removeSignChangedListener(SignChangedListener l) {
		signChangedListeners.remove(l);
	}

	/**
	 * @since 0.5
	 */
	public static void onSignReadFromNBT(int xPosition, int yPosition,
			int zPosition, String[] signLines) {

		fireSignChangedEvent(new SignChangedEvent(new Sign(signLines,
				xPosition, yPosition, zPosition),
				SignChangeSource.LOADED_FROM_NBT));
	}

	/**
	 * SignWatcher internal method.
	 * 
	 * @param xPosition
	 * @param yPosition
	 * @param zPosition
	 * @param signLines
	 */
	protected static void onSignReadFromPacket(int xPosition, int yPosition,
			int zPosition, String[] signLines) {

		fireSignChangedEvent(new SignChangedEvent(new Sign(signLines,
				xPosition, yPosition, zPosition),
				SignChangeSource.PACKET_CREATED));
	}

	/**
	 * Sends a message to all registered sign changed listeners
	 * 
	 * @param e
	 */
	private static void fireSignChangedEvent(SignChangedEvent e) {
		if (debugMessages) {
			System.out.println(e);
		}
		for (SignChangedListener l : signChangedListeners) {
			l.signChanged(e);
		}
	}

	public static boolean getDebugMessages() {
		return debugMessages;
	}

	public static void setDebugMessages(boolean debugMessages) {
		SignWatcher.debugMessages = debugMessages;
	}
}

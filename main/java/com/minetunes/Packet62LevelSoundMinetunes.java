/**
 * CHANGES FROM MOJANG CODE
 * Copyright (c) 2012 William Karnavas 
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
package com.minetunes;

import java.io.DataInput;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;

import com.minetunes.config.MinetunesConfig;
import com.minetunes.noteblocks.BlockNoteMinetunes;

public class Packet62LevelSoundMinetunes extends Packet62LevelSound {
	private static boolean apologized = false;

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(DataInput par1DataInputStream)
			throws IOException {
		super.readPacketData(par1DataInputStream);

		// Handle noteblock muting
		if (MinetunesConfig.getBoolean("noteblock.mute")) {
			if (getSoundName().startsWith("note.")) {
				setNoteNameWithReflection("");
			}
		} else if (getSoundName().startsWith("note.")) {

			// Check for noteblock at location
			if (Minecraft.getMinecraft().theWorld.getBlock(
					(int) (getEffectX() - 0.5), (int) (getEffectY() - 0.5),
					(int) (getEffectZ() - 0.5)) == Blocks.noteblock) {
				// Handle noteblock octave adjustment
				int adjust = BlockNoteMinetunes.getOctaveAdjust(
						(int) (getEffectX() - 0.5), (int) (getEffectY() - 0.5),
						(int) (getEffectZ() - 0.5));
				if (adjust != 0) {
					// TODO String newSoundName = getSoundName() + "_" + adjust
					// + "o";
					String newSoundName = BlockNoteMinetunes
							.getNoteTypeForBlock(
									Minecraft.getMinecraft().theWorld,
									(int) (getEffectX() - 0.5),
									(int) (getEffectY() - 0.5),
									(int) (getEffectZ() - 0.5))
							+ "_" + adjust + "o";
					setNoteNameWithReflection(newSoundName);
					// apologize();
				}
			}
		}
	}

	public static void apologize() {
		if (!apologized) {
			apologized = true;
			Minetunes
					.showTextAsLyricNow("MineTunes: Minecraft 1.6 won't let me play high and low noteblocks yet.");
		}
	}

	private void setNoteNameWithReflection(String value) {
		try {
			Finder.setUniqueTypedFieldFromClass(Packet62LevelSound.class,
					String.class, this, value);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

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
package com.minetunes.blockTune;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import net.minecraft.src.ItemStack;

import com.minetunes.resources.ResourceManager;

/**
 * 
 *
 */
public class BlockUtils {
	/**
	 * Block IDs that cannot be judged as the same block (for purposes of
	 * instrument blocks) by their block ID alone. MUST BE SORTED.<br>
	 * <br>
	 * Last updated: Minecraft 1.5.1, where block ids went up to 158dec.
	 */
	private static final int[] idsWhereMetaMatters = { 6, 17, 18, 34, 35, 43,
			44, 59, 69, 70, 72, 78, 84, 92, 93, 94, 97, 98, 104, 105, 107, 115,
			118, 120, 125, 126, 127, 139, 140, 141, 142, 147, 148, 155 };

	/**
	 * Like idsWhereMetaMatters except that there is nothing special about
	 * comparing the meta values: if they're different, the blocks are
	 * different, period. Contrast with saplings (2 bits specify types, 1 is
	 * just a counter for their growth that should be ignored) and several other
	 * blocks. Will be shorter than or equal in length to idsWhereMetaMatters.
	 * MUST BE SORTED. <br>
	 * <br>
	 * Last updated: Minecraft 1.5.1, where block ids went up to 158dec.
	 */
	private static final int[] idsWhereMetaMattersStrictly = { 24, 35, 43, 59,
			70, 72, 78, 84, 92, 97, 98, 104, 105, 115, 118, 125, 139, 140, 141,
			142, 147, 148, 155 };

	private static final HashMap<Integer, Integer> metaCompareMasks = new HashMap<Integer, Integer>();
	static {
		// 6, 17, 18, 44, 69, 93, 94, 107, 120, 126, 127

		// Only care about first two bits
		metaCompareMasks.put(6, 0x3);
		metaCompareMasks.put(17, 0x3);
		metaCompareMasks.put(18, 0x3);

		// Only care about first three bits
		metaCompareMasks.put(44, 0x7);
		metaCompareMasks.put(126, 0x7);

		// Only care about last bit
		metaCompareMasks.put(69, 0x8);

		// Only care about top two bits
		metaCompareMasks.put(93, 0xC);
		metaCompareMasks.put(94, 0xC);
		metaCompareMasks.put(127, 0xC);

		// Only care about third bit
		metaCompareMasks.put(107, 0x4);
		metaCompareMasks.put(120, 0x4);
	}

	/**
	 * Compares two blocks to see if they are "essentially" the same. Redstone
	 * wire at any power, stairs in any direction, and water both flowing and
	 * source are examples of blocks that will be the same through this method.
	 * Special cases are also handled, such as saplings (only compare type, or
	 * first two bits of metadata) and cocoa sacks (only stage matters, not
	 * orientation).
	 * 
	 * @param id1
	 * @param meta1
	 * @param id2
	 * @param meta2
	 * @return true if blocks are "essentially" the same
	 */
	public static boolean blocksAreSimilar(int id1, int meta1, int id2,
			int meta2) {
		// If ids are dissimilar, then duh, the blocks are different
		if (id1 != id2) {
			return false;
		}

		if (contains(idsWhereMetaMatters, id1)) {
			// Meta matters; can we get away with a meta == meta?
			if (contains(idsWhereMetaMattersStrictly, id2)) {
				// Yes!
				return meta1 == meta2;
			} else {
				// No, there's a special comparison
				Integer mask = metaCompareMasks.get(id1);
				if (mask == null) {
					// If there was no special mask, use all four bits to
					// compare
					mask = 0xf;
				}
				// Mask metadata values and compare remaining bits
				if ((meta1 & mask) == (meta2 & mask)) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			// Just compare ids (was done already at top of method)
			return true;
		}
	}

	/**
	 * Convenience method for blocksaresimilar(int int int int)
	 * 
	 * @param itemStack
	 * @param compID
	 * @param compMeta
	 * @return
	 */
	public static boolean blocksAreSimilar(ItemStack itemStack, int compID,
			int compMeta) {
		return blocksAreSimilar(itemStack.itemID, itemStack.getItemDamage(),
				compID, compMeta);
	}

	/**
	 * True if passed array contains v
	 * 
	 * @param array
	 * @param v
	 * @return
	 */
	public static boolean contains(int[] array, int v) {
		for (int i : array) {
			if (i == v) {
				return true;
			}
		}
		return false;
	}

	/* ===================Instrument Associations=================== */

	private static Properties blockInstruments = new Properties();
	static {
		try {
			blockInstruments.load(new ByteArrayInputStream(ResourceManager
					.loadCached("blockTune/blockInstruments.txt")
					.getBytes()));
		} catch (IOException e) {
			System.err
					.println("MineTunes: Could not load Block to Instrument mappings.");
			e.printStackTrace();
		}
	}

	/**
	 * Gets the instrument associated with this particular block. Compared using
	 * BlockUtils.blocksAreSimilar(int, int, int, int).
	 * 
	 * @param id
	 * @param meta
	 * @return null if no instrument is specified in particular
	 */
	public static Integer getInstForBlock(int id, int meta) {
		for (String key : blockInstruments.stringPropertyNames()) {
			// Get this key's ID and meta
			Integer keyID = null, keyMeta = 0;
			if (key.matches("\\d+\\.\\d+")) {
				// Key meta and id specified
				String[] splitKey = key.split("\\.");
				keyID = Integer.parseInt(splitKey[0]);
				keyMeta = Integer.parseInt(splitKey[1]);
			} else if (key.matches("\\d+")) {
				// Key id specified
				keyID = Integer.parseInt(key);
			} else {
				// Invalid key
				System.err
						.println("MineTune: BUG: Invalid key in resources/blockTune/blockInstruments.txt");
				continue;
			}

			if (keyID == null) {
				// Invalid key
				continue;
			}

			// Check to ensure that the given block and the key's block are
			// similar
			if (!blocksAreSimilar(keyID, keyMeta, id, meta)) {
				continue;
			}

			// Match found if a continue has not been hit!
			// Turn the property entry into an integer and return
			String value = blockInstruments.getProperty(key);
			if (value.matches("\\d+")) {
				return Integer.parseInt(value);
			} else {
				return null;
			}
		}

		// No entry found; return null
		return null;
	}
}

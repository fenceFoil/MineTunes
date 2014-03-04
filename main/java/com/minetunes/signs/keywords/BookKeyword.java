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

import java.util.LinkedList;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.minetunes.Minetunes;
import com.minetunes.Point3D;
import com.minetunes.books.BookTuneParser;
import com.minetunes.books.BookWrapper;
import com.minetunes.books.booktunes.BookSection;
import com.minetunes.books.booktunes.BookTune;
import com.minetunes.books.booktunes.MidiFileSection;
import com.minetunes.books.booktunes.PartSection;
import com.minetunes.ditty.Ditty;
import com.minetunes.ditty.event.ParticleEvent;
import com.minetunes.dittyXML.DittyXMLParser;
import com.minetunes.dittyXML.MissingContainerException;
import com.minetunes.dittyXML.UnnamedLyricsException;
import com.minetunes.dittyXML.UnnumberedLyricException;
import com.minetunes.particle.ParticleRequest;
import com.minetunes.signs.SignTuneParser;
import com.minetunes.signs.keywords.argparser.ArgParser;

/**
 * @author William
 * 
 */
public class BookKeyword extends SignTuneKeyword {

	/**
	 * @param wholeKeyword
	 */
	public BookKeyword(String wholeKeyword) {
		super(wholeKeyword);
		// No arguments
		argParser = new ArgParser();
	}

	@Override
	public Point3D execute(Ditty ditty, Point3D location,
			TileEntitySign signTileEntity, Point3D nextSign, World world,
			StringBuilder readMusicString) {

		LinkedList<ItemStack> nearbyBooks = Minetunes.getFramedItemsNearby(
				world, location, 2, Item.writableBook.itemID,
				Item.writtenBook.itemID);

		for (ItemStack item : nearbyBooks) {
			boolean failure = false;
			BookWrapper book = BookWrapper.wrapBook(item);

			// XXX: hijack the parse here to play midi books with auto-play in
			// them.
			BookTune tune = new BookTune();
			if (tune.loadFromBooks(book) && tune.getSections().size() > 0) {
				if ((tune.getPartSection() == null || tune.getPartSection()
						.getOf() == 1)) {
					// Look for a midi file section with auto-play enabled
					for (BookSection s : tune.getSections()) {
						if (s instanceof MidiFileSection) {
							if (((MidiFileSection) s).isAutoPlay()) {
								// Found.

								MidiFileSection midiSection = (MidiFileSection) s;
								// if (midiSection.getName() != null) {
								// Minecraft.getMinecraft().ingameGUI
								// .setRecordPlayingMessage(midiSection
								// .getName());
								// }

								if (midiSection.getData() != null) {
									byte[] midiData = midiSection.getData();
									Minetunes.playMidiFile(midiData);
								}
							}
						}
					}
				} else if (tune.getPartSection() != null) {
					// Multibook midi file
					PartSection partSection = tune.getPartSection();
					if (partSection.getPart() > partSection.getOf()) {
						ditty.addErrorMessage("A book in set \""
								+ partSection.getSet()
								+ "\" claims it is number "
								+ partSection.getPart() + " of "
								+ partSection.getOf() + ".");
						failure = true;
					} else if (ditty.getMidiParts().containsKey(partSection)) {
						ditty.addErrorMessage("There is a duplicate of book "
								+ partSection.getPart() + " of set "
								+ partSection.getSet() + " in this SignTune.");
						failure = true;
					} else {

						// Check for presence of conflicting versions of same
						// set in booktune, and conflicting "of" values against
						// all parts read up to now
						for (PartSection p : ditty.getMidiParts().keySet()) {
							if (p.getSet().equals(partSection.getSet())) {
								if ((p.getVer() != partSection.getVer())) {
									ditty.addErrorMessage("There are multiple versions of BookTune set "
											+ p.getSet()
											+ " mixed together in this SignTune.");
									failure = true;
									break;
								} else if (p.getOf() != partSection.getOf()) {
									ditty.addErrorMessage("Part " + p.getPart()
											+ " of BookTune set " + p.getSet()
											+ " claims the set is " + p.getOf()
											+ " long, but part "
											+ partSection.getPart()
											+ " claims the set is "
											+ partSection.getOf() + " long.");
									failure = true;
									break;
								}
							}
						}

						// If that last version check didn't throw an error...
						if (!failure) {
							MidiFileSection midiSection = null;

							for (BookSection bookSection : tune.getSections()) {
								if (bookSection instanceof MidiFileSection) {
									midiSection = (MidiFileSection) bookSection;
								}
							}

							if (midiSection != null) {
								// Add midi section to ditty to parse at run
								ditty.getMidiParts().put(partSection,
										midiSection);
							}
						}
					}
				}
			}

			try {
				LinkedList<Element> mcdittyelements = BookTuneParser
						.getDittyXMLContainers(book);
				DittyXMLParser.parseDittyXMLContainers(mcdittyelements, ditty);
			} catch (SAXException e) {
				failure = true;
				e.printStackTrace();
			} catch (MissingContainerException e) {
				failure = true;
				e.printStackTrace();
			} catch (UnnamedLyricsException e) {
				failure = true;
				e.printStackTrace();
			} catch (UnnumberedLyricException e) {
				failure = true;
				e.printStackTrace();
			}

			if (failure) {
				for (int i = 0; i < 20; i++) {
					String type = "lava";
					if (Minetunes.rand.nextInt(3) == 0) {
						type = "smoke";
					} else if (Minetunes.rand.nextInt(2) == 0) {
						type = "explode";
					}
					int id = ditty.addDittyEvent(new ParticleEvent(
							new ParticleRequest(location, type), ditty
									.getDittyID()));
					ditty.addMusicStringTokens(readMusicString,
							SignTuneParser.TIMED_EVENT_TOKEN + id, false);
				}
			}
		}

		// System.out.println (ditty.getMusicString());
		readMusicString.append(ditty.getMusicString());

		// No change in sign flow
		return null;
	}
}

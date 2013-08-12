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
package com.minetunes.gui;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import net.minecraft.src.GuiScreenBook;
import net.minecraft.src.ItemStack;

import com.minetunes.Finder;
import com.minetunes.base64.Base64;
import com.minetunes.books.BookWrapper;
import com.minetunes.books.booktunes.BookSection;
import com.minetunes.books.booktunes.BookTune;
import com.minetunes.books.booktunes.MidiFileSection;
import com.minetunes.books.booktunes.PartSection;
import com.minetunes.resources.SFXSynth;
import com.minetunes.sfx.SFXManager;
import com.minetunes.tempoGui.TGButton;
import com.minetunes.tempoGui.TGFrame;
import com.minetunes.tempoGui.TGList;
import com.minetunes.tempoGui.TGListener;
import com.minetunes.tempoGui.TGPanel;
import com.minetunes.tempoGui.TGTextLabel;
import com.minetunes.tempoGui.event.TGEvent;
import com.minetunes.tempoGui.event.TGListEvent;

/**
 * Base64.encodeBytes(data, Base64.GZIP)
 * 
 * Currently only used for MIDI books
 * 
 */
public class BookEditorGui extends TGFrame {

	private static final double BOOK_CAPACITY_MINUS_ONE_PAGE = 49d * 255d;
	private BookWrapper book;
	private BookTune bookTune;

	private MidiFileSection currMidiFileSection = new MidiFileSection();

	private static File loadedMidiFile;
	private static byte[] loadedMidiData;
	private static String loadedMidiDataBase64;

	private TGTextLabel midiNameLabel;
	private TGTextLabel midiSizeLabel;
	private TGTextLabel midiBooksRqdLabel;

	private TGTextLabel currNameLabel;

	private TGList bookButtonList;

	private TGPanel currBookPanel, loadedMidiPanel, bookButtonPanel;

	// private double booksRqd = 0;
	// private double compressedBytes = 0;

	public String tex = "/com/minetunes/resources/textures/signEditor1.png";
	private static TGTextLabel bookButtonListTitleLabel;
	private static LinkedList<String> partButtonData = new LinkedList<String>();

	/**
	 * @param bookGui
	 */
	public BookEditorGui(GuiScreenBook backScreen) {
		super(backScreen, "Write MIDI BookTune");

		reloadCurrBook(backScreen);
	}

	private void reloadCurrBook(GuiScreenBook backScreen) {
		// Get the book to edit from the book gui
		ItemStack bookItem = null;
		try {
			bookItem = (ItemStack) Finder.getUniqueTypedFieldFromClass(
					GuiScreenBook.class, ItemStack.class, backScreen);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bookItem != null) {
			book = BookWrapper.wrapBook(bookItem);
		}

		// Create new booktune
		bookTune = new BookTune();

		// Load BookTune from the book being edited
		bookTune.loadFromBooks(book);

		// Get its midi file section if it has one
		for (BookSection s : bookTune.getSections()) {
			if (s instanceof MidiFileSection) {
				currMidiFileSection = (MidiFileSection) s;
				break;
			}
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		// Update current book info
		if (currMidiFileSection != null) {
			currNameLabel.setLabelText(currMidiFileSection.getName() + " Part "
					+ (currMidiFileSection.getPart() + 1));
		} else {
			currNameLabel.setLabelText("No MIDI");
		}

		if (loadedMidiFile != null) {
			bookButtonListTitleLabel.setLabelText(loadedMidiFile.getName());
		}
	}

	private void closeAndSave() {
		// // Write new book
		// try {
		// String bookTuneString = bookTune.saveToXML();
		// book.fillWithText(bookTuneString, false, true, true);
		// book.flushPages();
		// book.sendBookToServer(false);
		// } catch (XMLStreamException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (FactoryConfigurationError e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		mc.displayGuiScreen(backScreen);
	}

	@Override
	public void initGui() {
		super.initGui();

		setFrameSize(400, 200);

		setupCompoenents();
		
		if (loadedMidiFile != null) {
			loadMidiFile(loadedMidiFile);
		}
	}

	private void setupCompoenents() {
		removeAll();
		currBookPanel = new TGPanel(0, 0, getFrameWidth() / 2,
				(getFrameHeight() - 20) / 2, false);
		loadedMidiPanel = new TGPanel(0, (getFrameHeight() - 20) / 2,
				getFrameWidth() / 2, (getFrameHeight() - 20) / 2, false);
		bookButtonPanel = new TGPanel(getFrameWidth() / 2, 0,
				getFrameWidth() / 2, getFrameHeight() - 20, false);
		add(currBookPanel);
		add(loadedMidiPanel);
		add(bookButtonPanel);

		final BookEditorGui thisGui = this;

		currBookPanel.add(currNameLabel = new TGTextLabel(currBookPanel
				.getWidth() / 2, 20, ""));
		currBookPanel.add(new TGTextLabel(currBookPanel.getWidth() / 2, 5,
				"This Book Contains:"));

		loadedMidiPanel.add(midiNameLabel = new TGTextLabel(loadedMidiPanel
				.getWidth() / 2, 5, ""));
		loadedMidiPanel.add(midiSizeLabel = new TGTextLabel(loadedMidiPanel
				.getWidth() / 2, 20, ""));
		loadedMidiPanel.add(midiBooksRqdLabel = new TGTextLabel(loadedMidiPanel
				.getWidth() / 2, 35, ""));

		TGButton browseButton = (TGButton) new TGButton(
				loadedMidiPanel.getWidth() / 2 - 40, 50, 80, 20,
				"Select File...").addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent e) {
				// Show a file selector
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						FileDialog d = new FileDialog((Frame) null,
								"Choose a MIDI:", FileDialog.LOAD);
						d.show();
						String fileName = d.getFile();
						if (fileName == null) {
							return;
						} else {
							File file = new File(d.getDirectory() + fileName);
							thisGui.loadMidiFile(file);
						}
					}

				});
				t.start();
				t.setName("MineTunes MIDI File Chooser");
				// Import txt file
			}
		});
		loadedMidiPanel.add(browseButton);

		bookButtonList = new TGList(0, 20, bookButtonPanel.getWidth(),
				bookButtonPanel.getHeight() - 40, false, 0,
				new LinkedList<String>());
		bookButtonList.setButtonHeight(20);
		bookButtonPanel.add(bookButtonList);
		bookButtonList.addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent event) {
				if (event instanceof TGListEvent) {
					TGListEvent e = (TGListEvent) event;

					// Save midi file

					bookTune.getSections().clear();

					if (partButtonData.size() > 1) {
						PartSection partSec = new PartSection();
						partSec.setOf(partButtonData.size());
						partSec.setPart(e.getIndex() + 1);
						partSec.setSet(loadedMidiFile.getName());
						bookTune.getSections().add(partSec);
					}

					MidiFileSection sec = new MidiFileSection();
					sec.setBase64Data(partButtonData.get(e.getIndex()));
					sec.setAutoPlay(true);
					sec.setName(loadedMidiFile.getName());
					if (partButtonData.size() > 1) {
						sec.setPart(e.getIndex());
					}
					bookTune.getSections().add(sec);

					String bookTuneString = "";
					try {
						bookTuneString = bookTune.saveToXML();
					} catch (XMLStreamException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FactoryConfigurationError e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					book.fillWithText(bookTuneString, false, true, true);
					book.flushPages();
					book.sendBookToServer(false);

					reloadCurrBook((GuiScreenBook) backScreen);
				}
			}
		});
		bookButtonPanel.add(bookButtonListTitleLabel = new TGTextLabel(
				bookButtonPanel.getWidth() / 2, 5, ""));

		TGButton closeButton = (TGButton) new TGButton(
				getFrameWidth() / 2 - 100, getFrameHeight() - 20, 200, 20,
				"Close").addListener(new TGListener() {

			@Override
			public void onTGEvent(TGEvent arg0) {
				closeAndSave();
			}
		});
		add(closeButton);
	}

	protected void loadMidiFile(File file) {
		if (!file.exists()) {
			return;
		}

		loadedMidiFile = file;

		byte[] fileData;
		try {
			fileData = readFile(file);
		} catch (IOException e) {
			e.printStackTrace();
			// Don't overwrite existing midiFileSection
			return;
		}

		loadedMidiData = fileData;
		try {
			loadedMidiDataBase64 = Base64.encodeBytes(loadedMidiData,
					Base64.GZIP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		midiSizeLabel.setLabelText(String.format("%,.2f",
				((double) loadedMidiData.length / 1024d))
				+ " KB ---> "
				+ String.format("%,.2f",
						(loadedMidiDataBase64.length() / 1024d)) + " KB");
		double booksRqd = (double) (loadedMidiDataBase64.length())
				/ BOOK_CAPACITY_MINUS_ONE_PAGE;
		midiBooksRqdLabel.setLabelText("Books Required: "
				+ String.format("%,.2f", booksRqd));

		// Update write book buttons
		LinkedList<String> partButtonLabels = new LinkedList<String>();
		partButtonData.clear();
		for (int i = 0; i < Math.ceil(booksRqd); i++) {
			partButtonLabels.add("Write Part " + (i + 1));

			// Note corresponding base64 data to write into book
			partButtonData.add(loadedMidiDataBase64.substring(
					(int) BOOK_CAPACITY_MINUS_ONE_PAGE * i, Math.min(
							(int) BOOK_CAPACITY_MINUS_ONE_PAGE * (i + 1),
							loadedMidiDataBase64.length())));
		}
		bookButtonList.setItems(partButtonLabels);
		bookButtonList.updateItems();

		// currMidiFileSection = new MidiFileSection();
		// currMidiFileSection.setName(file.getName());
		// currMidiFileSection.setData(fileData);
		// currMidiFileSection.setAutoPlay(true);

		// // Remove existing midi sections
		// for (int i = 0; i < bookTune.getSections().size(); i++) {
		// if (bookTune.getSections().get(i) instanceof MidiFileSection) {
		// bookTune.getSections().remove(i);
		// i--;
		// }
		// }
		// bookTune.getSections().add(currMidiFileSection);

		// // Book tune too long for this book?
		// try {
		// compressedBytes = bookTune.saveToXML().length();
		// // TODO Use 49 pages instead of 50 to account for overhead. This is
		// // guesswork.
		// booksRqd = (double) (compressedBytes)
		// / BOOK_CAPACITY_MINUS_ONE_PAGE;
		// } catch (XMLStreamException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (FactoryConfigurationError e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private byte[] readFile(File f) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		FileInputStream fileIn = new FileInputStream(f);
		while (true) {
			int readByte = fileIn.read();
			if (readByte < 0) {
				break;
			}

			byteOut.write(readByte);
		}
		fileIn.close();
		byteOut.flush();
		return byteOut.toByteArray();
	}

}

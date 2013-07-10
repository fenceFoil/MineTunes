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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiScreenBook;
import net.minecraft.src.ItemStack;

import org.lwjgl.input.Keyboard;

import com.minetunes.Finder;
import com.minetunes.books.BookWrapper;
import com.minetunes.books.booktunes.BookSection;
import com.minetunes.books.booktunes.BookTune;
import com.minetunes.books.booktunes.MidiFileSection;

/**
 * Currently only used for MIDI books
 * 
 */
public class BookEditorGui extends GuiScreen {

	private GuiScreen backScreen;
	private BookWrapper book;
	private BookTune bookTune;

	private MidiFileSection midiFileSection = new MidiFileSection();

	private double booksRqd = 0;
	private double compressedBytes = 0;

	public String tex = "/com/minetunes/resources/textures/signEditor1.png";

	/**
	 * @param bookGui
	 */
	public BookEditorGui(GuiScreenBook backScreen) {
		// setBackScreen(backScreen);

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
				midiFileSection = (MidiFileSection) s;
				break;
			}
		}
	}

	public GuiScreen getBackScreen() {
		return backScreen;
	}

	public void setBackScreen(GuiScreen backScreen) {
		this.backScreen = backScreen;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();

		drawCenteredString(fontRenderer, "Choose a MIDI File", width / 2, 30,
				0xffffff);

		if (midiFileSection != null) {
			if (midiFileSection.getName() != null) {
				drawCenteredString(fontRenderer, midiFileSection.getName(),
						width / 2, 85, 0xffffff);
				if (midiFileSection.getData() != null && compressedBytes != 0) {
					drawCenteredString(
							fontRenderer,
							String.format(
									"%,.2f",
									((double) midiFileSection.getData().length / 1024d))
									+ " KB ---> "
									+ String.format("%,.2f",
											(compressedBytes / 1024d)) + " KB",
							width / 2, 135, 0xffffff);
				}
			}
		}

		if (booksRqd != 0) {
			int color = 0x00ff00;
			if (booksRqd > 1.0) {
				color = 0xff5500;
			}
			drawCenteredString(fontRenderer, "Books Required: " + String.format("%,.2f", booksRqd), width / 2, height - 90, color);
		}

		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == Keyboard.KEY_ESCAPE) {
			closeAndSave();
		}
	}

	private void closeAndSave() {
		// Write new book
		try {
			String bookTuneString = bookTune.saveToXML();
			book.fillWithText(bookTuneString, false, true, true);
			book.flushPages();
			book.sendBookToServer(false);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mc.displayGuiScreen(backScreen);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		// TODO Auto-generated method stub
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		// TODO Auto-generated method stub
		super.mouseMovedOrUp(par1, par2, par3);
	}

	@Override
	public void initGui() {
		super.initGui();

		GuiButtonL browseButton = new GuiButtonL("browseMidi", width / 2 - 40,
				100, 80, 20, "Select MIDI");
		final BookEditorGui thisGui = this;
		browseButton.addListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
		buttonList.add(browseButton);

		GuiButtonL saveButton = new GuiButtonL("save", width / 2 - 100,
				height - 60, 200, 20, "Save & Close");
		buttonList.add(saveButton);
		saveButton.addListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeAndSave();
			}
		});
	}

	protected void loadMidiFile(File file) {
		if (!file.exists()) {
			return;
		}

		byte[] fileData;
		try {
			fileData = readFile(file);
		} catch (IOException e) {
			e.printStackTrace();
			// Don't overwrite existing midiFileSection
			return;
		}

		midiFileSection = new MidiFileSection();
		midiFileSection.setName(file.getName());
		midiFileSection.setData(fileData);
		midiFileSection.setAutoPlay(true);

		// Remove existing midi sections
		for (int i = 0; i < bookTune.getSections().size(); i++) {
			if (bookTune.getSections().get(i) instanceof MidiFileSection) {
				bookTune.getSections().remove(i);
				i--;
			}
		}
		bookTune.getSections().add(midiFileSection);

		// Book tune too long for this book?
		try {
			compressedBytes = bookTune.saveToXML().length();
			booksRqd = (double) (compressedBytes) / (50d * 255d);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub
		super.updateScreen();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

}

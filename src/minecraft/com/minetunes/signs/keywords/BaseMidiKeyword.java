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

import com.minetunes.signs.ParsedSign;
import com.minetunes.signs.keywords.argparser.ArgParser;
import com.minetunes.signs.keywords.argparser.SimpleFilenameArg;

/**
 * @author William
 * 
 */
public class BaseMidiKeyword extends SignTuneKeyword {

	protected String midiFile;
	protected SimpleFilenameArg filenameArg = new SimpleFilenameArg(
			"File Name", false);

	public BaseMidiKeyword(String wholeKeyword) {
		super(wholeKeyword);
		argParser = new ArgParser().addLine().addLine(filenameArg);
	}

	@Override
	public <T extends SignTuneKeyword> void parseWithMultiline(
			ParsedSign parsedSign, int keywordLine, T k) {
		super.parseWithMultiline(parsedSign, keywordLine, k);

		// String[] signText = parsedSign.getSignText();
		//
		// // Read the filename from line 2
		// String givenFilename = signText[1].trim();
		// if (SignParser.recognizeKeyword(signText[1]) != null) {
		// // If a keyword is found beneath the midi sign, notify user
		// setErrorMessage("Instead of a filename, a keyword was found beneath a Midi keyword.");
		// setErrorMessageType(ERROR);
		// // Bad filename: non-alphanumeric characters
		// } else if (!givenFilename.matches("[\\d\\w]*")
		// && (!givenFilename.equals(""))) {
		// setErrorMessage("A MIDI file name should only contains letters and numbers (no spaces)");
		// setErrorMessageType(ERROR);
		// // Bad filename: non-alphanumeric characters
		// } else if (givenFilename.equals("")) {
		// // Empty filenames are frowned upon
		// setErrorMessage("No MIDI file is given after a MIDI keyword.");
		// setErrorMessageType(ERROR);
		// }

		// Save the newly-approved filename
		setMidiFile(filenameArg.getFilename());
	}

	@Override
	public boolean isMultiline() {
		return true;
	}

	@Override
	public boolean isFirstLineOnly() {
		return false;
	}

	public String getMidiFile() {
		return midiFile;
	}

	public void setMidiFile(String midiFile) {
		this.midiFile = midiFile;
	}

	@Override
	public int getLineCount() {
		return 2;
	}

}

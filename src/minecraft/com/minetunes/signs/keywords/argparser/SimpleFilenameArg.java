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
package com.minetunes.signs.keywords.argparser;

import java.util.LinkedList;

/**
 * A MIDI filename, as in a SaveMidi keyword. Only allows simple alphanumeric
 * characters for security, to prevent names like "..\..\..\boot.ini"
 */
public class SimpleFilenameArg extends Arg {
	private String filename;

	public SimpleFilenameArg(String name, boolean optional) {
		super(name, optional);
	}

	@Override
	public void reset() {
		super.reset();
		filename = null;
	}

	@Override
	public int parse(LinkedList<String> tokens) {
		super.parse(tokens);

		// Max of one token used
		String token = tokens.peekFirst();

		// Check for null token
		if (token == null) {
			if (!isOptional()) {
				addMissingError();
			}
			return 0;
		}
		
		// Check for no token
		if (token.equals("")) {
			if (!isOptional()) {
				addMissingError();
			}
		}

		// Note that arg has been parsed
		setParsed(true);

		// Confirm arg matches regex
		String regex = "[\\w]+";
		if (!token.matches(regex)) {
			addParseError(new ArgParseError(this,
					"must contain only letters, numbers, and _", true));
			return 0;
		}
		setFilename(token);

		return 1;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String label) {
		this.filename = label;
	}

}

/**
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

import java.io.File;
import java.io.FileFilter;

public class MidiFileFilter implements FileFilter {

	private String[] extensions = { ".mid", ".midi", ".tmp" };

	@Override
	public boolean accept(File f) {
		if (!f.getName().contains(".")) {
			return false;
		}

		boolean isMidi = false;
		for (String extension : extensions) {
			if (f.getName().substring(f.getName().lastIndexOf('.'))
					.equalsIgnoreCase(extension)) {
				isMidi = true;
			}
		}
		return isMidi;
	}

}

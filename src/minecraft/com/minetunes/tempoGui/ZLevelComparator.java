/**
 * Copyright (c) 2013 William Karnavas 
 * All Rights Reserved
 * 
 * This file is part of SavoyCraft.
 * 
 * SavoyCraft is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SavoyCraft is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SavoyCraft. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.minetunes.tempoGui;

import java.util.Comparator;

/**
 * Sorts components by z level, from highest to lowest.
 * 
 */
public class ZLevelComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object arg0, Object arg1) {
		if (!(arg0 instanceof TGComponent)) {
			return 0;
		}
		if (!(arg1 instanceof TGComponent)) {
			return 0;
		}

		TGComponent comp1 = (TGComponent) arg0;
		TGComponent comp2 = (TGComponent) arg1;
		if (comp1.getZLevel() > comp2.getZLevel()) {
			return -1;
		} else if (comp2.getZLevel() < comp2.getZLevel()) {
			return 1;
		} else {
			return 0;
		}
	}

}

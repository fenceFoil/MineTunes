/**
 * Copyright (c) 2012 William Karnavas 
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
package com.fencefoil.signWatcher.interfaces;

import com.fencefoil.signWatcher.SignChangedEvent;

/**
 * An instance of this can be registered with ******** to receive an event
 * whenever a sign is changed.
 * 
 * @since 0.5
 * 
 */
public interface SignChangedListener {

	/**
	 * Called whenever a sign has been changed. May be called multiple times per
	 * change, for the same sign.
	 * 
	 * @param event
	 *            note that the "text" field in the sign event should not be
	 *            modified, as it may be a reference to a sign text array still
	 *            being used in MineCraft
	 */
	public void signChanged(SignChangedEvent event);
}

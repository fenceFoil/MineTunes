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

import com.minetunes.tempoGui.event.TGEvent;

/**
 * Any component can have TGListeners registered with it. Some components fire
 * sub-classes of TGEvent to indicate actions, others, like simple buttons, just
 * fire a TGEvent with no other info, and some don't use registered listeners at
 * all. Compare ActionListener in Swing.
 * 
 */
public interface TGListener {
	public void onTGEvent(TGEvent event);
}

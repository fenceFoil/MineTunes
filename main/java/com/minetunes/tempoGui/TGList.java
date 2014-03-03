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

import java.util.LinkedList;

import com.minetunes.tempoGui.event.TGAddButtonEvent;
import com.minetunes.tempoGui.event.TGEvent;
import com.minetunes.tempoGui.event.TGListEvent;

/**
 * A list of buttons, that automatically adds a scrollbar if the list is long
 * enough. Also provided is an optional small, green "+" or add button at the
 * end of the list. To use, provide a list of items with the constructor. If you
 * need to change the list after that, use "setItems" or change the list with
 * "getItems," then call updateItems(). This component throws two classes of
 * events, for normal button clicks and clicking on the "add button."
 * 
 * @author BJ
 * 
 */
public class TGList extends TGScrollPanel {

	private LinkedList<String> items;
	private boolean addButtonEnabled;
	private TGButton addButton;
	private int buttonHeight = 14;

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param showBorder
	 * @param letterboxHeight
	 */
	public TGList(int x, int y, int width, int height, boolean showBorder,
			int letterboxHeight, LinkedList<String> listItems) {
		super(x, y, width, height, showBorder, letterboxHeight);
		setItems((LinkedList<String>) listItems.clone());
		updateItems();
	}

	public void updateItems() {
		components.clear();

		final TGList thisList = this;
		int buttonSpacing = buttonHeight / 10 + buttonHeight;
		for (int i = 0; i < items.size(); i++) {
			final int iFinal = i;
			TGButton newButton = new TGButton(scrollbar.getWidth() + 3,
					buttonSpacing * i, getWidth() - scrollbar.getWidth() * 2
							- 3 * 2, buttonHeight, items.get(i));
			newButton.addListener(new TGListener() {

				@Override
				public void onTGEvent(TGEvent event) {
					fireTGEvent(new TGListEvent(thisList, iFinal));
				}
			});
			components.add(newButton);
		}

		if (addButtonEnabled) {
			int addButtonWidth = (int) ((float) buttonHeight * 3.6f);
			int buttonMaxWidth = getWidth() - scrollbar.getWidth() * 2 - 3 * 2;
			addButton = new TGButton(getWidth() / 2 - addButtonWidth / 2, buttonSpacing
					* items.size(), addButtonWidth, buttonHeight, "+");
			addButton.addListener(new TGListener() {

				@Override
				public void onTGEvent(TGEvent event) {
					fireTGEvent(new TGAddButtonEvent(thisList));
				}
			});
			addButton.setBgColor(0xff44ff22);
			components.add(addButton);
		}
	}

	public LinkedList<String> getItems() {
		return items;
	}

	public void setItems(LinkedList<String> items) {
		this.items = items;
	}

	public boolean isAddButtonEnabled() {
		return addButtonEnabled;
	}

	public void setAddButtonEnabled(boolean addButtonEnabled) {
		this.addButtonEnabled = addButtonEnabled;

		updateItems();
	}

	public int getButtonHeight() {
		return buttonHeight;
	}

	public void setButtonHeight(int buttonHeight) {
		this.buttonHeight = buttonHeight;
	}

}

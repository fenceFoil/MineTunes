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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.minetunes.tempoGui.event.TGEvent;

/**
 * A panel of components, with separate tabs of components accessible by buttons
 * along the top.
 * 
 * @author BJ
 * 
 */
public class TGTabbedPanel extends TGComponent {

	private static final int TAB_BUTTON_HEIGHT = 14;
	private int selectedTab = 0;
	private boolean showBorder = false;

	private LinkedHashMap<String, LinkedList<TGComponent>> tabs = new LinkedHashMap<String, LinkedList<TGComponent>>();
	private LinkedList<TGButton> tabButtons = new LinkedList<TGButton>();

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TGTabbedPanel(int x, int y, int width, int height, boolean showBorder) {
		super(x, y, width, height);
		setShowBorder(showBorder);
	}

	/**
	 * Creates new tabs
	 * 
	 * @param names
	 */
	public void addTabs(String... names) {
		for (String s : names) {
			tabs.put(s, new LinkedList<TGComponent>());
		}
		updateTabButtons();
	}

	/**
	 * Adds components to the given tab
	 * 
	 * @param tab
	 * @param component
	 */
	public void addTo(String tab, TGComponent... component) {
		if (getTabIndex(tab) < 0) {
			return;
		}

		// Update new component's offset (along with all others)
		updateTabComponents();

		tabs.get(tab).addAll(Arrays.asList(component));
	}

	/**
	 * Finds a tab index for a given name
	 * 
	 * @param name
	 * @return -1 if not found
	 */
	protected int getTabIndex(String name) {
		int currTab = 0;
		for (String s : tabs.keySet()) {
			if (name.equals(s)) {
				return currTab;
			}
			currTab++;
		}
		return -1;
	}

	/**
	 * Removes components from any tab they are present upon
	 * 
	 * @param component
	 */
	public void remove(TGComponent... component) {
		for (LinkedList<TGComponent> l : tabs.values()) {
			l.removeAll(Arrays.asList(component));
		}
	}

	/**
	 * Removes all tabs
	 */
	public void removeAllTabs() {
		tabs.clear();
		selectedTab = 0;
		updateTabButtons();
	}

	/**
	 * Removes all components but leaves tabs
	 */
	public void removeAll() {
		for (LinkedList<TGComponent> l : tabs.values()) {
			l.clear();
		}
	}

	/**
	 * Removes all components from one tab. Does not remove any tabs.
	 * 
	 * @param tab
	 */
	public void removeAll(String tab) {
		if (getTabIndex(tab) < 0) {
			return;
		}

		tabs.get(tab).clear();
	}

	protected void updateTabButtons() {
		tabButtons.clear();
		int currX = 0;
		for (String s : tabs.keySet()) {
			final TGButton b = new TGButton(currX, 0, TAB_BUTTON_HEIGHT, s);
			b.addListener(new TGListener() {

				@Override
				public void onTGEvent(TGEvent event) {
					switchTab(b.getLabel());
				}
			});
			b.setScreenOffset(getScreenOffsetX(), getScreenOffsetY());
			tabButtons.add(b);

			// // Invert foreground and background colors
			// b.setBgColor(fgColor);
			// b.setFgColor(bgColor);

			currX += b.getWidth() + 3;
		}
	}

	public void switchTab(String name) {
		if (getTabIndex(name) < 0) {
			return;
		}

		selectedTab = getTabIndex(name);
	}

	@Override
	public void draw(int mx, int my) {
		super.draw(mx, my);

		if (showBorder) {
			drawBorder(0);
		}

		// Draw components on selected tab
		for (TGComponent c : getTabComponents(selectedTab)) {
			c.draw(mx, my - TAB_BUTTON_HEIGHT);
		}

		// Draw tab buttons
		for (TGButton b : tabButtons) {
			b.draw(mx, my);
		}
	}

	/**
	 * Gets the tab components for a given tab index from the LinkedHashMap
	 * 
	 * @param index
	 * @return an empty list if not found
	 */
	protected LinkedList<TGComponent> getTabComponents(int index) {
		int iterationsLeft = index;
		for (LinkedList<TGComponent> l : tabs.values()) {
			if (iterationsLeft <= 0) {
				return l;
			}

			iterationsLeft--;
		}
		return new LinkedList<TGComponent>();
	}

	@Override
	public void keyTyped(char keyChar, char keyCode) {
		super.keyTyped(keyChar, keyCode);

		// Draw components on selected tab
		for (TGComponent c : getTabComponents(selectedTab)) {
			c.keyTyped(keyChar, keyCode);
		}

		// Draw tab buttons
		for (TGButton b : tabButtons) {
			b.keyTyped(keyChar, keyCode);
		}
	}

	@Override
	public void mouseClicked(int mx, int my, int button) {
		super.mouseClicked(mx, my, button);

		// Draw components on selected tab
		for (TGComponent c : getTabComponents(selectedTab)) {
			c.mouseClicked(mx, my - TAB_BUTTON_HEIGHT, button);
		}

		// Draw tab buttons
		for (TGButton b : tabButtons) {
			b.mouseClicked(mx, my, button);
		}
	}

	@Override
	public void mouseMovedOrUp(int mx, int my, int button) {
		super.mouseMovedOrUp(mx, my, button);

		// Draw components on selected tab
		for (TGComponent c : getTabComponents(selectedTab)) {
			c.mouseMovedOrUp(mx, my - TAB_BUTTON_HEIGHT, button);
		}

		// Draw tab buttons
		for (TGButton b : tabButtons) {
			b.mouseMovedOrUp(mx, my, button);
		}
	}

	public boolean isShowBorder() {
		return showBorder;
	}

	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}

	@Override
	public void setScreenOffsetX(int xScreenOffset) {
		super.setScreenOffsetX(xScreenOffset);
		updateTabButtons();
		updateTabComponents();
	}

	protected void updateTabComponents() {
		for (LinkedList<TGComponent> l : tabs.values()) {
			for (TGComponent c : l) {
				c.setScreenOffset(getScreenOffsetX(), getScreenOffsetY()
						+ TAB_BUTTON_HEIGHT);
			}
		}
	}

	@Override
	public void setScreenOffsetY(int yScreenOffset) {
		super.setScreenOffsetY(yScreenOffset);
		updateTabButtons();
		updateTabComponents();
	}

}

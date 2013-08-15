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
import java.util.LinkedList;

/**
 * A vertically-scrolling panel of TGComponents.
 * 
 * @author BJ
 * 
 */
public class TGPanel extends TGComponent {
	private boolean showBorder = false;

	protected LinkedList<TGComponent> components = new LinkedList<TGComponent>();

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TGPanel(int x, int y, int width, int height, boolean showBorder) {
		super(x, y, width, height);
		setShowBorder(showBorder);
	}

	public TGPanel add(TGComponent... component) {
		for (TGComponent c : component) {
			components.add(c);
		}
		return this;
	}

	/**
	 * Removes components from any tab they are present upon
	 * 
	 * @param component
	 */
	public void remove(TGComponent... component) {
		components.removeAll(Arrays.asList(component));
	}

	/**
	 * Removes all components but leaves tabs
	 */
	public void removeAll() {
		components.clear();
	}

	/**
	 * Gets the tab components for a given tab index from the LinkedHashMap
	 * 
	 * @param index
	 * @return an empty list if not found
	 */
	protected LinkedList<TGComponent> getComponents() {
		return components;
	}

	@Override
	public void draw(int mx, int my) {
		super.draw(mx, my);

		updateComponentScreenOffsets();

		// Draw components on selected tab
		for (TGComponent c : components) {
			c.draw(mx - getX(), my - getY());
		}

		// Draw border
		if (showBorder) {
			drawBorder(0);
		}
	}

	@Override
	public void keyTyped(char keyChar, char keyCode) {
		super.keyTyped(keyChar, keyCode);

		// Draw components on selected tab
		for (TGComponent c : components) {
			c.keyTyped(keyChar, keyCode);
		}
	}

	@Override
	public void mouseClicked(int mx, int my, int button) {
		super.mouseClicked(mx, my, button);

		// Draw components on selected tab
		if (isMouseInside(mx, my)) {
			for (TGComponent c : components) {
				c.mouseClicked(mx - getX(), my - getY(), button);
			}
		}
	}

	@Override
	public void mouseMovedOrUp(int mx, int my, int button) {
		super.mouseMovedOrUp(mx, my, button);

		// Draw components on selected tab
		if (isMouseInside(mx, my)) {
			for (TGComponent c : components) {
				c.mouseMovedOrUp(mx - getX(), my - getY(), button);
			}
		}
	}

	public boolean isShowBorder() {
		return showBorder;
	}

	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}

	protected void updateComponentScreenOffsets() {
		// Update component render positions
		for (TGComponent c : components) {
			c.setScreenOffset(getScreenOffsetX() + getX(), getScreenOffsetY()
					+ getY());
		}
	}

	@Override
	public void setScreenOffsetX(int xScreenOffset) {
		super.setScreenOffsetX(xScreenOffset);

		updateComponentScreenOffsets();
	}

	@Override
	public void setScreenOffsetY(int yScreenOffset) {
		super.setScreenOffsetY(yScreenOffset);

		updateComponentScreenOffsets();
	}
}

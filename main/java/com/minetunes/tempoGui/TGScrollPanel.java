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

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * A vertically-scrolling panel of TGComponents.
 * 
 * @author BJ
 * 
 */
public class TGScrollPanel extends TGComponent {
	private boolean showBorder = false;
	protected TGScrollbar scrollbar;
	private int letterboxHeight = 0;
	/**
	 * This starts at 0 and goes down. SEE: Internet privacy.
	 */
	private int componentYOffset = 0;

	protected LinkedList<TGComponent> components = new LinkedList<TGComponent>();
	private LinkedList<TGComponent> visibleComponents = new LinkedList<TGComponent>();

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TGScrollPanel(int x, int y, int width, int height,
			boolean showBorder, int letterboxHeight) {
		super(x, y, width, height);
		setShowBorder(showBorder);
		setLetterboxHeight(letterboxHeight);

		int scrollbarWidth = 10;
		scrollbar = new TGScrollbar(x + width - scrollbarWidth, y + 0,
				scrollbarWidth, height);
		scrollbar.setScreenOffset(getScreenOffsetX(), getScreenOffsetY());
		
		setZLevel(0.5);
	}

	public TGScrollPanel add(TGComponent... component) {
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

	private int lastmx, lastmy;

	@Override
	public void draw(int mx, int my) {
		super.draw(mx, my);

		int wheelDelta = Mouse.getDWheel();
		if (isMouseInside(mx, my)) {
			if (wheelDelta != 0 && scrollbar.getSliderSizeRatio() < 1.0f) {
				int moveAmount = (int) ((float) wheelDelta * 0.1f);
				scrollInPixels(moveAmount);
			}
		}

		updateComponents();

		// Draw components on selected tab
		for (TGComponent c : visibleComponents) {
			c.draw(mx - getX(), my - componentYOffset - getY());
		}

		// Draw letterbox
		drawRect(getScreenOffsetX(), getY() - letterboxHeight
				+ getScreenOffsetY(), getScreenOffsetX() + getWidth(), getY()
				+ getScreenOffsetY(), bgColor);
		drawRect(getScreenOffsetX(), getY() + getHeight() + getScreenOffsetY(),
				getScreenOffsetX() + getWidth(), getY() + getScreenOffsetY()
						+ getHeight() + letterboxHeight, bgColor);

		// Draw border
		if (showBorder) {
			drawBorder(0);
		}

		scrollbar.draw(mx, my);

		lastmx = mx;
		lastmy = my;
	}

	private void scrollInPixels(int moveAmount) {
		int nonVisibleHeight = (int) Math.max(
				(getScrollPanelHeight() - (float) (getHeight())), 0);
		int currScrollPos = (int) (scrollbar.getPosition() * Math.max(
				getHeight() - getScrollPanelHeight(), 0));
		currScrollPos -= moveAmount;
		scrollbar.setPosition(scrollbar.getPosition() + (float) currScrollPos
				/ (float) nonVisibleHeight);
	}

	@Override
	public void keyTyped(char keyChar, char keyCode) {
		super.keyTyped(keyChar, keyCode);

		// Accept page up and page down keystrokes
		// if (isMouseInside(lastmx, lastmy)) {
		int scrollAmount = 0;
		if (keyCode == Keyboard.KEY_PRIOR) {
			scrollAmount += getHeight();
		} else if (keyCode == Keyboard.KEY_NEXT) {
			scrollAmount -= getHeight();
		}
		if (scrollAmount != 0) {
			scrollInPixels(scrollAmount);
		}
		// }

		// Draw components on selected tab
		for (TGComponent c : visibleComponents) {
			c.keyTyped(keyChar, keyCode);
		}
	}

	@Override
	public void mouseClicked(int mx, int my, int button) {
		super.mouseClicked(mx, my, button);

		// Draw components on selected tab
		if (isMouseInside(mx, my)) {
			for (TGComponent c : visibleComponents) {
				c.mouseClicked(mx - getX(), my - componentYOffset - getY(),
						button);
			}
		}

		scrollbar.mouseClicked(mx, my, button);
	}

	@Override
	public void mouseMovedOrUp(int mx, int my, int button) {
		super.mouseMovedOrUp(mx, my, button);

		// Draw components on selected tab
		if (isMouseInside(mx, my)) {
			for (TGComponent c : visibleComponents) {
				c.mouseMovedOrUp(mx - getX(), my - componentYOffset - getY(),
						button);
			}
		}

		scrollbar.mouseMovedOrUp(mx, my, button);
	}

	public boolean isShowBorder() {
		return showBorder;
	}

	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}

	protected void updateComponents() {
		if (getScrollPanelHeight() != 0) {
			scrollbar.setSliderSizeRatio((float) getHeight()
					/ (float) getScrollPanelHeight());
		} else {
			scrollbar.setSliderSizeRatio(1);
		}

		componentYOffset = -(int) ((float) (Math.max(getScrollPanelHeight()
				- getHeight(), 0)) * scrollbar.getPosition());

		// Update component render positions
		for (TGComponent c : components) {
			c.setScreenOffset(getScreenOffsetX() + getX(), getScreenOffsetY()
					+ componentYOffset + getY());
		}
		scrollbar.setScreenOffset(getScreenOffsetX(), getScreenOffsetY());

		// Calculate visible components
		Rectangle scrollPaneBounds = new Rectangle(0,
				(int) ((float) (Math.max(getScrollPanelHeight() - getHeight(),
						0)) * scrollbar.getPosition()), Integer.MAX_VALUE,
				getHeight());
		visibleComponents.clear();
		for (TGComponent c : components) {
			if (scrollPaneBounds.intersects(c.getBounds())) {
				visibleComponents.add(c);
			}
		}
	}

	@Override
	public void setScreenOffsetX(int xScreenOffset) {
		super.setScreenOffsetX(xScreenOffset);

		updateComponents();
	}

	@Override
	public void setScreenOffsetY(int yScreenOffset) {
		super.setScreenOffsetY(yScreenOffset);

		updateComponents();
	}

	/**
	 * Calculates the height of the panel "inside" the scoll panel from the
	 * components currently inside.
	 */
	public int getScrollPanelHeight() {
		int maxComponentYCoord = 0;
		for (TGComponent c : components) {
			if (c.getY() + c.getHeight() > maxComponentYCoord) {
				maxComponentYCoord = c.getY() + c.getHeight();
			}
		}
		return maxComponentYCoord;
	}

	public int getLetterboxHeight() {
		return letterboxHeight;
	}

	public void setLetterboxHeight(int letterboxHeight) {
		this.letterboxHeight = letterboxHeight;
	}
}

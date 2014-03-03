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
 * A vertical scrollbar. Call "getSliderSizeRatio()" with the ratio between
 * window size / content size to adjust the size of the scroll handle.
 * 
 * @author BJ
 * 
 */
public class TGScrollbar extends TGComponent {
	private boolean pressing;
	private int mouseLastY;
	private float position = 0;
	private float sliderSizeRatio = 0;

	/**
	 * Makes a new TGButton, determining the width from the label
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param label
	 */
	public TGScrollbar(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void draw(int mx, int my) {
		super.draw(mx, my);

		if (getSliderSizeRatio() < 1.0f) {

			int sliderHeight = Math.max(
					(int) ((float) getHeight() * sliderSizeRatio), 8);
			float sliderMovementRange = getHeight() - sliderHeight;

			if (pressing) {
				float mouseDelta = my - mouseLastY;
				if (sliderMovementRange != 0) {
					float positionDelta = mouseDelta / sliderMovementRange;
					setPosition(getPosition() + positionDelta);
				}
			}

			int sliderY = (int) (sliderMovementRange * getPosition());

			int sliderColor = borderColor;
			if (isMouseInside(mx, my) || pressing) {
				sliderColor = bgColorRollover;
			}
			// The + / - 1 are to not draw over the already-drawn border
			drawRect(getX() + getScreenOffsetX() + 1, sliderY
					+ getScreenOffsetY() + getY(), getX() + getScreenOffsetX()
					+ getWidth() - 1, sliderY + sliderHeight - 1
					+ getScreenOffsetY() + getY(), sliderColor);

			drawBorder(0, borderColor);

			mouseLastY = my;
		}
	}

	@Override
	public void mouseClicked(int mx, int my, int button) {
		super.mouseClicked(mx, my, button);

		if (button == 0 && isMouseInside(mx, my) && !pressing) {
			// Start pressing
			pressing = true;
			mouseLastY = my;
			playSelectSound();
		}
	}

	@Override
	public void mouseMovedOrUp(int mx, int my, int button) {
		super.mouseMovedOrUp(mx, my, button);

		if (button == 0 && pressing) {
			// Raised mouse button
			if (isMouseInside(mx, my)) {
				fireTGEvent(new TGEvent(this));
			}
			pressing = false;
		}
	}

	public float getPosition() {
		return Math.max(Math.min(position, 1), 0);
	}

	public void setPosition(float position) {
		this.position = position;
	}

	public float getSliderSizeRatio() {
		return sliderSizeRatio;
	}

	public void setSliderSizeRatio(float sliderSizeRatio) {
		this.sliderSizeRatio = sliderSizeRatio;
	}

}

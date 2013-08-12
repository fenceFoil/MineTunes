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

import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Minecraft;

import org.lwjgl.input.Keyboard;

/**
 * @author BJ
 * 
 */
public class TGTextField extends TGComponent {

	protected String text = "";

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TGTextField(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public TGTextField(int x, int y, int width, int height, String text) {
		this(x, y, width, height);
		setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void draw(int mx, int my) {
		// TODO Auto-generated method stub
		super.draw(mx, my);

		drawBorder();

		// Draw text
		int color = labelColor;
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int labelWidth = fr.getStringWidth(text);
		// int textX = getScreenX() + (width - labelWidth) / 2;
		int textX = 4;
		int textY = getScreenY() + (height / 2) - 4;
		fr.drawString(text
				+ ((System.currentTimeMillis() % 1000 > 500) ? "" : "_"), textX
				+ getScreenOffsetX(), textY, color);
	}

	@Override
	public void keyTyped(char keyChar, char keyCode) {
		super.keyTyped(keyChar, keyCode);

		if (ChatAllowedCharacters.isAllowedCharacter(keyChar)) {
			text += keyChar;
		}

		if (keyCode == Keyboard.KEY_BACK) {
			if (text.length() >= 1) {
				text = text.substring(0, text.length() - 1);
			}
		}
	}

	@Override
	public void mouseClicked(int mx, int my, int button) {
		// TODO Auto-generated method stub
		super.mouseClicked(mx, my, button);
	}

	@Override
	public void mouseMovedOrUp(int mx, int my, int button) {
		// TODO Auto-generated method stub
		super.mouseMovedOrUp(mx, my, button);
	}

}

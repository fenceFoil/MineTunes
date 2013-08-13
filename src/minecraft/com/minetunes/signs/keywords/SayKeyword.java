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
package com.minetunes.signs.keywords;

import net.minecraft.src.TileEntitySign;
import net.minecraft.src.World;

import com.minetunes.CueScheduler;
import com.minetunes.Minetunes;
import com.minetunes.Point3D;
import com.minetunes.config.MinetunesConfig;
import com.minetunes.ditty.Ditty;
import com.minetunes.signs.ParsedSign;
import com.minetunes.signs.SignTuneParser;
import com.minetunes.signs.keywords.argparser.ArgParser;

/**
 * @author William
 * 
 */
public class SayKeyword extends SignTuneKeyword {

	private String label = generateLabel();
	private String lyricText = "";

	private static int sayLyricCount = 0;

	private String generateLabel() {
		return "say" + (sayLyricCount++);
	}

	/**
	 * @param wholeKeyword
	 */
	public SayKeyword(String wholeKeyword) {
		super(wholeKeyword);

		argParser = new ArgParser();
	}

	@Override
	public void parse() {
		super.parse();
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public <T extends SignTuneKeyword> void parseWithMultiline(
			ParsedSign parsedSign, int keywordLine, T k) {
		super.parseWithMultiline(parsedSign, keywordLine, k);

		// Read lyric text (but not if there's no line after the keyword)
		if (keywordLine < 3) {
			setLyricText(SignTuneParser.readLyricFromSign(keywordLine + 1,
					parsedSign.getSignText(), ""));
		}
	}

	@Override
	public boolean isMultiline() {
		return true;
	}

	@Override
	public Point3D execute(Ditty ditty, Point3D location,
			TileEntitySign signTilEntity, Point3D nextSign, World world,
			StringBuilder b) {

		boolean trailing = false;
		// Check for trailing lyric
		// Get last token; if it's a say token, just tack onto it
		try {
			String[] bTokens = b.toString().trim().split(" ");
			String lastToken = bTokens[bTokens.length - 1];
			int indexFromEnd = 1;
			while (lastToken.startsWith("~A") || lastToken.startsWith("~B")) {
				indexFromEnd++;
				lastToken = bTokens[bTokens.length - indexFromEnd];
			}
			//System.out.println(lastToken);
			if (lastToken.startsWith("Ysay")) {
				trailing = true;
				label = lastToken.substring(1);
				lyricText = " " + lyricText;
			}
		} catch (Exception e) {
		}

		// Adding to existing lyric?
		if (MinetunesConfig.getBoolean("lyrics.enabled")) {
			CueScheduler lyrics = ditty.getLyricsStorage();

			lyrics.addLyricText(getLabel(), lyricText, 1);
		}

		// Insert lyric token
		if (!trailing) {
			ditty.addMusicStringTokens(b, "Y" + getLabel(), false);
		}

		return null;
	}

	public String getLyricText() {
		return lyricText;
	}

	public void setLyricText(String lyricText) {
		this.lyricText = lyricText;
	}

	@Override
	public boolean isAllBelow() {
		return true;
	}

}

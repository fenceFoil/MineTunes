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

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

import org.jfugue.JFugueDefinitions;

import com.minetunes.Point3D;
import com.minetunes.ditty.Ditty;
import com.minetunes.signs.SignTuneParser;

/**
 * Keyword format:
 * 
 * Accel [bpm] [duration]
 * 
 * [bpm]: -300 to 300 Int [duration]: JFugue duration
 */
public class AccelerateKeyword extends SignTuneKeyword {

	private int bpm = 2;

	// NOT infinity in this case
	private int duration = 0;

	private String durationString = "";

	public AccelerateKeyword(String wholeKeyword) {
		super(wholeKeyword);
	}

	@Override
	public void parse() {
		String[] args = getWholeKeyword().split(" ");
		int numArgs = getWholeKeyword().split(" ").length;

		int argsLeft = 0;
		if (numArgs <= 1) {
			return;
		} else {
			argsLeft = numArgs - 1;
		}

		boolean tonesRead = false;
		boolean durationRead = false;
		int currArg = 1;
		while (argsLeft > 0 && !(tonesRead && durationRead)) {

			String argument = args[currArg];

			if (argument.trim().matches("[-\\+]?\\d+") && !tonesRead) {
				Integer arg = Integer.parseInt(argument);
				if (arg > 300 || arg < -300) {
					// Out of range
					setGoodKeyword(false);
					setErrorMessageType(ERROR);
					setErrorMessage("Follow Accel with bpm to increase (-300 to +300).");
					return;
				}
				setBPM(arg);

				tonesRead = true;
			} else {
				double decimalDuration = parseLetterDuration(
						argument.toUpperCase(), argument.length(), 0);
				setDuration((int) (decimalDuration * JFugueDefinitions.SEQUENCE_RESOLUTION));
				setDurationString(argument);

				// System.out.println (decimalDuration + ":"+argument);

				durationRead = true;
			}

			argsLeft--;
			currArg++;
		}

		// Ensure that tones are specified
		if (!tonesRead) {
			setGoodKeyword(false);
			setErrorMessageType(ERROR);
			setErrorMessage("Follow Accel with bpm to increase (-300 to +300).");
			return;
		}

		return;
	}

	public int getBPM() {
		return bpm;
	}

	public void setBPM(int semitones) {
		this.bpm = semitones;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDurationString() {
		return durationString;
	}

	public void setDurationString(String durationString) {
		this.durationString = durationString;
	}

	// Copied from JFugue's MusicStringParser (I know, bad form); modified
	private static double parseLetterDuration(String s, int slen, int index) {
		return StaccatoKeyword.parseLetterDuration(s, slen, index);
	}

	@Override
	public Point3D execute(Ditty ditty, Point3D location,
			TileEntitySign signTileEntity, Point3D nextSign, World world,
			StringBuilder readMusicString) {
		String token = SignTuneParser.createNoteEffectToken(false,
				SignTuneParser.NOTE_EFFECT_ACCELERATE, getBPM(),
				getDuration());
		ditty.addMusicStringTokens(readMusicString, token, false);

		return null;
	}

}

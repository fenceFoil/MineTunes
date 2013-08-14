/**
 * Copyright (c) 2012 William Karnavas 
 * All Rights Reserved
 */

/**
 * 
 * This file is part of MineTunes.
 * 
 * MineTunes is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * MineTunes is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MineTunes. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.minetunes.ditty.event;

import java.util.LinkedList;

import org.jfugue.elements.Note;

import com.minetunes.DOMUtil;
import com.minetunes.bot.action.BotAction;

/**
 * Represents lyric text and bot instructions to be shown/executed during a
 * ditty.
 */
public class CueEvent extends TimedDittyEvent {

	// Lyric text, if any
	private String lyricText = "";

	// BotActions to be executed
	private LinkedList<BotAction> botActions = new LinkedList<BotAction>();

	private Integer markedPitch;
	private Integer markedWPM;

	public CueEvent() {
		super();
	}

	public CueEvent(String text) {
		super();
		setLyricText(text);
	}

	public void addBotAction(BotAction action) {
		botActions.add(action);
	}

	/**
	 * @return the text
	 */
	public String getLyricText() {
		return lyricText;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setLyricText(String text) {
		if (text != null) {
			StringBuilder newText = new StringBuilder();
			try {
				// TODO: Search for markup
				for (String s : text.split(" ")) {
					if (s.startsWith("~")) {
						// Candidate markup found
						if (s.matches("~\\d+")) {
							markedWPM = DOMUtil.parseIntString(s.substring(1));
							// System.out.println("Found rate markup " + s);
						} else if (s
								.matches("~[abcdefgABCDEFG][#bB\\d]?[\\d]]?")) {
							String markedPitchNote = s.substring(1);
							Note note = Note.createNote(markedPitchNote + "q");
							markedPitch = (int) note.getFrequencyForNote(note
									.getValue());
							// System.out.println("Found pitch markup " + s);
						}
					} else {
						newText.append(s + " ");
					}
				}
				text = newText.toString().trim();
			} catch (Exception e) {
			}
		}

		// Strip markup and save
		if (text != null) {
			this.lyricText = text.replaceAll("~.+", "");
		} else {
			this.lyricText = text;
		}
	}

	public LinkedList<BotAction> getBotActions() {
		return botActions;
	}

	public void setBotActions(LinkedList<BotAction> botActions) {
		this.botActions = botActions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public CueEvent clone() {
		CueEvent l = new CueEvent();
		l.lyricText = lyricText;
		l.timeToPlay = timeToPlay;
		l.timesPlayed = timesPlayed;
		l.botActions.addAll(botActions);
		l.markedPitch = markedPitch;
		l.markedWPM = markedWPM;
		return l;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + ":" + lyricText + ":time:"
				+ timeToPlay + ":timesPlayed:" + timesPlayed;
	}

	public Integer getMarkedWPM() {
		return markedWPM;
	}

	public void setMarkedWPM(Integer markedWPM) {
		this.markedWPM = markedWPM;
	}

	public Integer getMarkedPitch() {
		return markedPitch;
	}

	public void setMarkedPitch(Integer markedPitch) {
		this.markedPitch = markedPitch;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((botActions == null) ? 0 : botActions.hashCode());
		result = prime * result
				+ ((lyricText == null) ? 0 : lyricText.hashCode());
		result = prime * result
				+ ((markedPitch == null) ? 0 : markedPitch.hashCode());
		result = prime * result
				+ ((markedWPM == null) ? 0 : markedWPM.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CueEvent other = (CueEvent) obj;
		if (botActions == null) {
			if (other.botActions != null)
				return false;
		} else if (!botActions.equals(other.botActions))
			return false;
		if (lyricText == null) {
			if (other.lyricText != null)
				return false;
		} else if (!lyricText.equals(other.lyricText))
			return false;
		if (markedPitch == null) {
			if (other.markedPitch != null)
				return false;
		} else if (!markedPitch.equals(other.markedPitch))
			return false;
		if (markedWPM == null) {
			if (other.markedWPM != null)
				return false;
		} else if (!markedWPM.equals(other.markedWPM))
			return false;
		return true;
	}
}

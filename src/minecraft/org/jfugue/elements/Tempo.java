/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2008  David Koelle
 *
 * http://www.jfugue.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

package org.jfugue.elements;

import java.io.IOException;
import java.util.Map;

import org.jfugue.JFugueException;
import org.jfugue.factories.JFugueElementFactory;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;
import org.jfugue.util.MapUtils;
import org.jfugue.visitors.ElementVisitor;

/**
 * Represents tempo changes. Tempo is kept for the whole song, and is
 * independent of tracks. You may change the tempo during a song.
 * 
 * As of JFugue 4.0, Tempo represents the Beats Per Minute (BPM). In previous
 * versions, Tempo was measured in microseconds per beat, which is how MIDI
 * maintains this information. (tempo = 60000 / BPM, and BPM = 60000 / tempo)
 * 
 * @author David Koelle
 * @version 2.0
 * @version 4.0
 */
public final class Tempo implements JFugueElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int tempo;

	/**
	 * Creates a new Tempo object, with the specified tempo value (in BPM).
	 * 
	 * @param tempoInBPM
	 *            the tempo for this object, in Beats Per Minute
	 */
	public Tempo(int tempoInBPM) {
		setTempo(tempoInBPM);
	}

	public Tempo(String tempoName) {
		String tempo = DICT_MAP.get(tempoName);
		if (tempo == null)
			setTempo(Integer.parseInt(tempo));
		else {
			throw new IllegalArgumentException("Named tempo not found: "
					+ tempoName);
		}
	}

	/**
	 * Sets the value of the tempo for this object.
	 * 
	 * @param tempoInBPM
	 *            the tempo for this object
	 */
	public void setTempo(int tempoInBPM) {
		this.tempo = tempoInBPM;
	}

	/**
	 * Returns the value of the tempo for this object.
	 * 
	 * @return the value of the tempo for this object
	 */
	public int getTempo() {
		return tempo;
	}

	/**
	 * Returns the Music String representing this element and all of its
	 * settings. For a Tempo object, the Music String is <code>T</code>
	 * <i>tempo</i>
	 * 
	 * @return the Music String for this element
	 */
	public String getMusicString() {
		StringBuffer buffy = new StringBuffer();
		buffy.append("T");
		buffy.append(getTempo());
		return buffy.toString();
	}

	/**
	 * Returns verification string in this format: Tempo: tempo={#}
	 * 
	 */
	public String getVerifyString() {
		StringBuffer buffy = new StringBuffer();
		buffy.append("Tempo: tempo=");
		buffy.append(getTempo());
		return buffy.toString();
	}

	public void acceptVisitor(ElementVisitor visitor) {
		visitor.visit(this);
	}

	public static final int GRAVE = 40;
	public static final int LARGO = 45;
	public static final int LARGHETTO = 50;
	public static final int LENTO = 55;
	public static final int ADAGIO = 60;
	public static final int ADAGIETTO = 65;

	public static final int ANDANTE = 70;
	public static final int ANDANTINO = 80;
	public static final int MODERATO = 95;
	public static final int ALLEGRETTO = 110;

	public static final int ALLEGRO = 120;
	public static final int VIVACE = 145;
	public static final int PRESTO = 180;
	public static final int PRETISSIMO = 220;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Tempo other = (Tempo) obj;
		if (this.tempo != other.tempo) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + this.tempo;
		return hash;
	}

	public static final Map<String, String> DICT_MAP;
	static {
		DICT_MAP = MapUtils.convertArrayToImutableMap(new String[][] {
				//
				// Tempo values
				// {NEW for JFugue 4.0)
				{ "GRAVE", "40" }, { "LARGO", "45" }, { "LARGHETTO", "50" },
				{ "LENTO", "55" }, { "ADAGIO", "60" }, { "ADAGIETTO", "65" },

				{ "ANDANTE", "70" }, { "ANDANTINO", "80" },
				{ "MODERATO", "95" }, { "ALLEGRETTO", "110" },

				{ "ALLEGRO", "120" }, { "VIVACE", "145" }, { "PRESTO", "180" },
				{ "PRETISSIMO", "220" }, });
	}

	public static class Factory extends JFugueElementFactory<Tempo> {

		private static Tempo.Factory instance;

		private Factory() {
		}

		public static Tempo.Factory getInstance() {
			if (instance == null)
				instance = new Tempo.Factory();
			return instance;
		}


		@Override
		public Class<Tempo> type() {
			return Tempo.class;
		}

		@Override
		public Tempo createElement(ParserContext context) throws IOException,
				IllegalArgumentException, JFugueException, ParserError {
			context.readPastWhitespace();
			context.readChar('T', 't');
			return context.fireTempoEvent(new Tempo(context.readInt()));
		}
	}

}

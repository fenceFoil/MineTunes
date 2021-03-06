/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2007  David Koelle
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

package org.jfugue;

import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;


import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Controller;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Lyric;
import org.jfugue.elements.MCDittyEvent;
import org.jfugue.elements.Measure;
import org.jfugue.elements.Note;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.SystemExclusive;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;
import org.jfugue.parsers.MusicStringParser;

import com.minetunes.config.MinetunesConfig;

/**
 * Assists the StreamingPlayer in converting Patterns to MIDI.
 * 
 * @see StreamingPlayer
 * @author David Koelle
 * @version 3.2
 */
public class StreamingMidiRenderer implements ParserListener {
	private StreamingMidiEventManager eventManager;
	private MusicStringParser parser;
	private Synthesizer synthesizer;
	private long initialNoteTime = 0;
	private long lastParallelNoteDuration = 0;
	private int currentTempo = Tempo.ALLEGRO;

	/**
	 * Instantiates a Renderer
	 */
	public StreamingMidiRenderer(Synthesizer synthesizer) {
		this.parser = new MusicStringParser();
		this.parser.addParserListener(this);
		this.synthesizer = synthesizer;
		reset();
	}

	/**
	 * Creates a new MidiEventManager. If this isn't called, events from
	 * multiple calls to render() will be added to the same eventManager, which
	 * means that the second time render() is called, it will contain music left
	 * over from the first time it was called. (This wasn't a problem with Java
	 * 1.4)
	 * 
	 * @since 3.0
	 */
	public void reset() {
		this.eventManager = new StreamingMidiEventManager(synthesizer);
	}

	// ParserListener methods
	// //////////////////////////

	public void voiceEvent(Voice voice) {
		this.eventManager.setCurrentTrack(voice.getVoice());
	}

	public void tempoEvent(Tempo tempo) {
		this.currentTempo = tempo.getTempo();
	}

	public void instrumentEvent(Instrument instrument) {
		this.eventManager.addEvent(ShortMessage.PROGRAM_CHANGE, instrument.getInstrument(), 0);
	}

	public void layerEvent(Layer layer) {
		this.eventManager.setCurrentLayer(layer.getLayer());
	}

	public void timeEvent(Time time) {
		this.eventManager.setTrackTimer(time.getTime());
	}

	public void measureEvent(Measure measure) {
		// No MIDI is generated when a measure indicator is identified.
	}

	public void keySignatureEvent(KeySignature keySig) {
		this.eventManager.addMetaMessage(0x59, new byte[] { keySig.getKeySig(), keySig.getScale() });
	}

	public void systemExclusiveEvent(SystemExclusive sysex) {
		this.eventManager.addSystemExclusiveEvent(sysex.getBytes());
	}

	public void controllerEvent(Controller controller) {
		this.eventManager.addEvent(ShortMessage.CONTROL_CHANGE, controller.getIndex(), controller.getValue());
	}

	public void channelPressureEvent(ChannelPressure channelPressure) {
		this.eventManager.addEvent(ShortMessage.CHANNEL_PRESSURE, channelPressure.getPressure());
	}

	public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure) {
		this.eventManager.addEvent(ShortMessage.POLY_PRESSURE, polyphonicPressure.getKey(), polyphonicPressure.getPressure());
	}

	public void pitchBendEvent(PitchBend pitchBend) {
		this.eventManager.addEvent(ShortMessage.PITCH_BEND, pitchBend.getBend()[0], pitchBend.getBend()[1]);
	}

	public void noteEvent(Note note) {
		if (lastParallelNoteDuration != 0) {
			this.eventManager.advanceTrackTimer(lastParallelNoteDuration);
			lastParallelNoteDuration = 0;
		}

		// Remember the current track time, so we can flip back to it
		// if there are other notes to play in parallel
		this.initialNoteTime = this.eventManager.getTrackTimer();

		final long durationInMillis = getNoteDurationInMillis(note);

		boolean noteOn = !note.isEndOfTie();
		boolean noteOff = !note.isStartOfTie();

		// Add messages to the track
		if (note.isRest()) {
			this.eventManager.advanceTrackTimer(durationInMillis);
		} else {
			initialNoteTime = eventManager.getTrackTimer();
			byte attackVelocity = note.getAttackVelocity();
			byte decayVelocity = note.getDecayVelocity();
			this.eventManager.addNoteEvents(note.getValue(), attackVelocity, decayVelocity, durationInMillis, noteOn, noteOff);
			if (!note.hasAccompanyingNotes()) {
				this.eventManager.advanceTrackTimer(durationInMillis);
			}
		}
	}

	public void sequentialNoteEvent(Note note) {
		final long durationInMillis = getNoteDurationInMillis(note);
		if (note.isRest()) {
			this.eventManager.advanceTrackTimer(durationInMillis);
		} else {
			byte attackVelocity = note.getAttackVelocity();
			byte decayVelocity = note.getDecayVelocity();
			this.eventManager.addNoteEvents(note.getValue(), attackVelocity, decayVelocity, durationInMillis, !note.isEndOfTie(), !note.isStartOfTie());
		}
	}

	public void parallelNoteEvent(Note note) {
		final long durationInMillis = getNoteDurationInMillis(note);
		lastParallelNoteDuration = durationInMillis;
		this.eventManager.setTrackTimer(this.initialNoteTime);
		if (note.isRest()) {
			this.eventManager.advanceTrackTimer(durationInMillis);
		} else {
			byte attackVelocity = note.getAttackVelocity();
			byte decayVelocity = note.getDecayVelocity();
			this.eventManager.addNoteEvents(note.getValue(), attackVelocity, decayVelocity, durationInMillis, !note.isEndOfTie(), !note.isStartOfTie());
		}
	}

	public void close() {
		this.eventManager.close();
	}

	private long getNoteDurationInMillis(Note note) {
		// Here we have to calculate the correct duration in milliseconds
		// based on the current tempo.
		final double secondsPerWholeNote = 4 * (1.0 / currentTempo) * 60.0;
		final long durationInMillis = note.getMillisDuration() * (long) (1000.0 * secondsPerWholeNote / 128.0);
		return durationInMillis;
	}

	/**
	 * MCDitty: TODO
	 */
	@Override
	public void lyricEvent(Lyric lyric) {
		long now = eventManager.getTrackTimer();
		if (MinetunesConfig.DEBUG) {
			System.out.println("Lyric at " + now + ": " + lyric.getVerifyString());
			System.err.println("This method is still unfinished: StreamingMidiRenderer.lyricEvent");
		}
	}

	/**
	 * MCDitty: TODO
	 */
	@Override
	public void mcDittyEvent(MCDittyEvent event) {
		long now = eventManager.getTrackTimer();
		if (MinetunesConfig.DEBUG) {
			System.out.println("MCDittyEvent at " + now + ": " + event.getVerifyString());
		}
		System.err.println("This method is still unfinished: StreamingMidiRenderer.mcDittyEvent");
	}

}

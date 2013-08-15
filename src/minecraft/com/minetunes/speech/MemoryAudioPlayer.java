/**
 * 
 * Copyright (c) 2013 William Karnavas All Rights Reserved
 * 
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
package com.minetunes.speech;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;

import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.util.Utilities;

/**
 * Takes output from a voice synthesizer and stores it in memory.
 */
public class MemoryAudioPlayer implements AudioPlayer {

	private AudioFormat audioFormat;
	private float volume;
	private BufferedOutputStream os;
	private ByteArrayOutputStream byteArrayOut;

	public MemoryAudioPlayer() {
		os = new BufferedOutputStream(
				byteArrayOut = new ByteArrayOutputStream());
	}

	/**
	 * Sets the audio format for this player
	 * 
	 * @param format
	 *            the audio format
	 */
	public void setAudioFormat(AudioFormat format) {
		this.audioFormat = format;
	}

	/**
	 * Retrieves the audio format for this player
	 * 
	 * @return the current audio format.
	 */
	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	/**
	 * Cancels all queued output. Current 'write' call will return false
	 * 
	 */
	public void cancel() {
	}

	/**
	 * Pauses the audio output
	 */
	public void pause() {
	}

	/**
	 * Prepares for another batch of output. Larger groups of output (such as
	 * all output associated with a single FreeTTSSpeakable) should be grouped
	 * between a reset/drain pair.
	 */
	public void reset() {
	}

	/**
	 * Resumes audio output
	 */
	public void resume() {
	}

	/**
	 * Waits for all audio playback to stop, and closes this AudioPlayer.
	 */
	public void close() {
		try {
			os.flush();
			os.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Returns the current volume.
	 * 
	 * @return the current volume (between 0 and 1)
	 */
	public float getVolume() {
		return volume;
	}

	/**
	 * Sets the current volume.
	 * 
	 * @param volume
	 *            the current volume (between 0 and 1)
	 */
	public void setVolume(float volume) {
		this.volume = volume;
	}

	/**
	 * Writes the given bytes to the audio stream
	 * 
	 * @param audioData
	 *            array of audio data
	 * 
	 * @return <code>true</code> of the write completed successfully,
	 *         <code> false </code>if the write was cancelled.
	 */
	public boolean write(byte[] audioData) {
		return write(audioData, 0, audioData.length);
	}

	/**
	 * Starts the output of a set of data
	 * 
	 * @param size
	 *            the size of data between now and the end
	 * 
	 */
	public void begin(int size) {
	}

	/**
	 * Marks the end of a set of data
	 * 
	 */
	public boolean end() {
		return true;
	}

	/**
	 * Writes the given bytes to the audio stream
	 * 
	 * @param bytes
	 *            audio data to write to the device
	 * @param offset
	 *            the offset into the buffer
	 * @param size
	 *            the size into the buffer
	 * 
	 * @return <code>true</code> of the write completed successfully,
	 *         <code> false </code>if the write was cancelled.
	 */
	public boolean write(byte[] bytes, int offset, int size) {
		try {
			os.write(bytes, offset, size);
		} catch (IOException ioe) {
			return false;
		}
		return true;
	}

	/**
	 * Starts the first sample timer
	 */
	public void startFirstSampleTimer() {
	}

	/**
	 * Waits for all queued audio to be played
	 * 
	 * @return <code>true</code> if the audio played to completion,
	 *         <code> false </code>if the audio was stopped
	 */
	public boolean drain() {
		return true;
	}

	/**
	 * Gets the amount of played since the last resetTime Currently not
	 * supported.
	 * 
	 * @return the amount of audio in milliseconds
	 */
	public long getTime() {
		return -1L;
	}

	/**
	 * Resets the audio clock
	 */
	public void resetTime() {
	}

	/**
	 * Shows metrics for this audio player
	 */
	public void showMetrics() {
	}
	
	/**
	 * Get the sound data written by the synthesizer.
	 * @return
	 */
	public byte[] getWrittenData () {
		byte[] data = byteArrayOut.toByteArray();
		byteArrayOut.reset();
		return data;
	}
}

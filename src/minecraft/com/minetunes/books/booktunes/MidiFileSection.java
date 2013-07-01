/**
 * Copyright (c) 2012-2013 William Karnavas 
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
package com.minetunes.books.booktunes;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Element;

import com.minetunes.DOMUtil;
import com.minetunes.base64.Base64;

/**
 *
 */
public class MidiFileSection extends BookSection {

	private String name = null;
	/**
	 * Uncompressed here
	 */
	private byte[] data = null;
	private boolean autoPlay = false;

	@Override
	public boolean load(Element element) throws IOException {
		name = DOMUtil.getAttributeValue(element, "name");
		// Autoplay defaults to false
		autoPlay = DOMUtil.parseBooleanStringWithDefault(
				DOMUtil.getAttributeValue(element, "autoPlay"), false);

		// Read data, if present
		Element dataElement = DOMUtil.findFirstElement("data",
				element.getChildNodes());
		if (dataElement != null) {
			String dataText = dataElement.getTextContent();
			if (dataText != null) {
				// GZipping automatically detected
				data = Base64.decode(dataText);
			}
		}

		// Success
		return true;
	}

	@Override
	public void save(XMLStreamWriter xmlOut) throws XMLStreamException,
			IOException {
		// Write the main element and attributes
		xmlOut.writeStartElement("midiFile");
		if (name != null) {
			xmlOut.writeAttribute("name", name);
		}
		if (autoPlay == true) {
			xmlOut.writeAttribute("autoPlay", "true");
		}

		if (data != null) {
			// Begin writing data element
			xmlOut.writeStartElement("data");

			// Write data to xml in base 64
			xmlOut.writeCharacters(Base64.encodeBytes(data, Base64.GZIP));

			// End data
			xmlOut.writeEndElement();
		}

		// Close main element
		xmlOut.writeEndElement();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public boolean isAutoPlay() {
		return autoPlay;
	}

	public void setAutoPlay(boolean autoPlay) {
		this.autoPlay = autoPlay;
	}

}

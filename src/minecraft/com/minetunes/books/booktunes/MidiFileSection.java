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
	// XXX: Part of the multibook midi hack of '13.
	private String base64Data = null;
	private boolean autoPlay = false;

	/**
	 * XXX: Part of the multibook midi hack of '13. Parts start at 0
	 */
	private Integer part = null;

	@Override
	public boolean load(Element element) throws IOException {
		name = DOMUtil.getAttributeValue(element, "name");
		// Autoplay defaults to false
		autoPlay = DOMUtil.parseBooleanStringWithDefault(
				DOMUtil.getAttributeValue(element, "autoPlay"), false);

		// XXX Part of the multibook midi hack of '13.
		part = DOMUtil.parseIntString(DOMUtil
				.getAttributeValue(element, "part"));

		// Read data, if present
		Element dataElement = DOMUtil.findFirstElement("data",
				element.getChildNodes());
		if (dataElement != null && part == null) {
			String dataText = dataElement.getTextContent();
			if (dataText != null) {
				// GZipping automatically detected
				data = Base64.decode(dataText);
			}
			setBase64Data(dataElement.getTextContent());
		} else if (part != null) {
			setBase64Data(dataElement.getTextContent());
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
		if (part != null) {
			xmlOut.writeAttribute("part", Integer.toString(part));
		}

		if (data != null || base64Data != null) {
			// Begin writing data element
			xmlOut.writeStartElement("data");

			// Write data to xml in base 64
			if (base64Data != null) {
				// XXX: part of multibook midi hack of '13
				xmlOut.writeCharacters(base64Data);
			} else {
				xmlOut.writeCharacters(Base64.encodeBytes(data, Base64.GZIP));
			}

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

	public int getPart() {
		if (part != null) {
			return part;
		} else {
			return 0;
		}
	}

	public void setPart(int part) {
		this.part = part;
	}

	public String getBase64Data() {
		return base64Data;
	}

	public void setBase64Data(String base64Data) {
		this.base64Data = base64Data;
	}

}

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
public class PartSection extends BookSection {

	private Integer part = null;
	private Integer of = null;
	private Integer ver = null;
	private String set = null;

	@Override
	public boolean load(Element element) throws IOException {
		part = DOMUtil.parseIntStringWithDefault(
				DOMUtil.getAttributeValue(element, "p"), 1);
		of = DOMUtil.parseIntStringWithDefault(
				DOMUtil.getAttributeValue(element, "of"), 1);
		ver = DOMUtil.parseIntStringWithDefault(
				DOMUtil.getAttributeValue(element, "ver"), 0);
		set = DOMUtil.getAttributeValueOrDefault(element, "set", "");

		// Success
		return true;
	}

	@Override
	public void save(XMLStreamWriter xmlOut) throws XMLStreamException,
			IOException {
		// Write the main element and attributes
		xmlOut.writeStartElement("part");
		if (part != null) {
			xmlOut.writeAttribute("p", part + "");
		}
		if (of != null) {
			xmlOut.writeAttribute("of", of + "");
		}
		if (ver != null) {
			xmlOut.writeAttribute("ver", ver + "");
		}
		if (set != null) {
			xmlOut.writeAttribute("set", set);
		}
		xmlOut.writeAttribute("part", Integer.toString(part));

		// Close main element
		xmlOut.writeEndElement();
	}

	
	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public Integer getPart() {
		return part;
	}

	public void setPart(Integer part) {
		this.part = part;
	}

	public Integer getOf() {
		return of;
	}

	public void setOf(Integer of) {
		this.of = of;
	}

	public Integer getVer() {
		return ver;
	}

	public void setVer(Integer ver) {
		this.ver = ver;
	}

}

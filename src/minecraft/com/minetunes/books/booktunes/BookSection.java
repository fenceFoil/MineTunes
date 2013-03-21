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

/**
 * An abstract representation of a base element in a BookTune written to XML. It
 * contains one type of content, usually one just item. Ex. A music Section with
 * one sub-pattern of music, or a bot skin file.
 * 
 */
public abstract class BookSection {

	/**
	 * 
	 * @param element
	 * @return false indicates errors
	 * @throws IOException 
	 */
	public abstract boolean load(Element element) throws IOException;

	public abstract void save(XMLStreamWriter xmlOut) throws XMLStreamException, IOException;
	
}

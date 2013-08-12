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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.soap.Node;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.minetunes.books.BookSource;
import com.minetunes.books.BookTuneParser;
import com.minetunes.books.BookWrapper;

/**
 * A representation of the sections of a BookTune. Can be read from XML, and be
 * rendered back to XML. A BookTune can be multiple WrappedBooks long.
 */
public class BookTune {

	private static HashMap<String, Class<? extends BookSection>> sectionTagNames = new HashMap<String, Class<? extends BookSection>>();
	static {
		sectionTagNames.put("midiFile", MidiFileSection.class);
		sectionTagNames.put("part", PartSection.class);
		// sectionTagNames.put("music", MusicSection.class);
		// sectionTagNames.put("lyrics", LyricsSection.class);
	}

	private LinkedList<BookSection> sections = new LinkedList<BookSection>();

	/**
	 * Currently only loads from the first book in the booksource
	 * 
	 * @param bookSource
	 * @return false if book failed to load
	 */
	public boolean loadFromBooks(BookSource bookSource) {
		if (bookSource.getBooksAvailable() <= 0) {
			return false;
		}

		// Get our first (and in this instance only) book to parse
		BookWrapper currBook = bookSource.getNextBook();

		// Get all the minetunes elements out of this son of a block
		LinkedList<Element> bookTuneElements = null;
		try {
			bookTuneElements = BookTuneParser.getDittyXMLContainers(currBook);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			// Since we're only reading one book, failing to read it means
			// failure
			return false;
		}

		// If there was a failure or the book didn't contain any book tune
		// elements, declare a failure.
		if (bookTuneElements == null) {
			return false;
		}

		// Now parse sections out of the book
		for (Element e : bookTuneElements) {
			NodeList childrenNodes = e.getChildNodes();
			for (int i = 0; i < childrenNodes.getLength(); i++) {
				if (childrenNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					for (String s : sectionTagNames.keySet()) {
						if (((Element) childrenNodes.item(i)).getTagName().equalsIgnoreCase(s)) {
							// Have a section parse itself
							BookSection section;
							try {
								section = sectionTagNames.get(s).newInstance();

								if (section.load((Element) childrenNodes.item(i))) {
									sections.add(section);
								}
							} catch (InstantiationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

					}
				}
			}
		}

		// Falling this far indicates success
		return true;
	}

	public LinkedList<BookSection> getSections() {
		return sections;
	}

	public String saveToXML() throws XMLStreamException,
			FactoryConfigurationError, IOException {
		ByteArrayOutputStream xmlBuffer = new ByteArrayOutputStream();
		XMLStreamWriter xmlOut = XMLOutputFactory.newFactory()
				.createXMLStreamWriter(xmlBuffer);

		// Start the bookTune element (the root element)
		xmlOut.writeStartElement("bookTune");

		// Write each section
		for (BookSection s : sections) {
			s.save(xmlOut);
		}

		// End the bookTune element
		xmlOut.writeEndElement();

		// Close everything and return the xml as a string
		xmlOut.flush();
		xmlOut.close();
		xmlBuffer.flush();
		xmlBuffer.close();
		return xmlBuffer.toString();
	}
}

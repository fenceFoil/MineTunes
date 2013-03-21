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
package com.minetunes.books;

import java.util.List;

/**
 * Draws books from a source and wraps them as needed. E.G. from a player's
 * inventory or the world.
 */
public interface BookSource {

	/**
	 * Returns the next book from this source.
	 * 
	 * @return null if no more books are available
	 */
	public BookWrapper getNextBook();

	/**
	 * the number of books left unread from this source
	 * 
	 * @return
	 */
	public int getBooksAvailable();

	/**
	 * The list of books already gotten by getNext() or getAll().
	 * 
	 * @return null if there is no cache yet, or no cache ever.
	 */
	public List<BookWrapper> getBookCache();

	/**
	 * Returns all books accessible from this source at once.
	 * 
	 * @return
	 */
	public List<BookWrapper> getAllBooks();

	/**
	 * Results in getNextBook returning the first book in the set when it is
	 * called again, and clearing the cache.
	 */
	public void resetBookSource();
}

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

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.ItemStack;

/**
 * Use over as short a time as possible; if the player has time to rearrange
 * books in his inventory, stuff might get nasty. Always call resetBookSource()
 * if an instance of this is used for longer than a couple moments.
 * 
 */
public class InventoryBookSource implements BookSource {

	private EntityClientPlayerMP player;
	private ItemStack[] inv;
	private LinkedList<ItemStack> bookStacks;

	private int pos = 0;

	public InventoryBookSource(EntityClientPlayerMP player) {
		this.player = player;
	}

	@Override
	public BookWrapper getNextBook() {
		if (pos < getBooks().size()) {
			BookWrapper book = BookWrapper.wrapBook(getBooks().get(pos));
			pos++;
			return book;
		} else {
			return null;
		}
	}

	@Override
	public int getBooksAvailable() {
		return getBooks().size();
	}

	@Override
	public List<BookWrapper> getBookCache() {
		return getAllBooks();
	}

	@Override
	public List<BookWrapper> getAllBooks() {
		LinkedList<BookWrapper> books = new LinkedList<BookWrapper>();
		for (ItemStack stack : getBooks()) {
			books.add(BookWrapper.wrapBook(stack));
		}
		return books;
	}

	@Override
	public void resetBookSource() {
		bookStacks = null;
		pos = 0;
	}

	private ItemStack[] getInv() {
		return player.inventory.mainInventory;
	}

	private LinkedList<ItemStack> getBooks() {
		if (bookStacks == null) {
			LinkedList<ItemStack> ll = new LinkedList<ItemStack>();
			for (ItemStack stack : getInv()) {
				if (BookWrapper.isItemStackBook(stack)) {
					ll.add(stack);
				}
			}
			bookStacks = ll;
		}
		return bookStacks;
	}

}

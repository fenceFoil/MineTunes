/**
 * Copyright (c) 2013 William Karnavas 
 * All Rights Reserved
 * 
 * This file is part of SignWatcher.
 * 
 * SignWatcher is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SignWatcher is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SignWatcher. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.fencefoil.signWatcher;

/**
 * 
 * 
 * @since 0.5
 * 
 */
public class SignChangedEvent {

	private Sign sign;
	private SignChangeSource changeSource;

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public SignChangedEvent(Sign sign, SignChangeSource changeSource) {
		setSign(sign);
		setChangeSource(changeSource);
	}

//	@Override
//	public String toString() {
//		return "SignChangedEvent:Sign:"
//				+ ((sign == null) ? "null" : sign.toString() + "Source:"
//						+ ((changeSource == null) ? "null" : changeSource));
//	}

	/**
	 * Any SignChangeSource that applies, not necessarily the most specific. For
	 * example, if a player finishes a sign in the sign editor, you may get a
	 * sign changed even that says it's from a 'packet' as opposed to 'sign
	 * editor closing'.
	 * 
	 * @return
	 */
	public SignChangeSource getChangeSource() {
		return changeSource;
	}

	public void setChangeSource(SignChangeSource changeSource) {
		this.changeSource = changeSource;
	}

	@Override
	public String toString() {
		return "SignChangedEvent [sign=" + sign + ", changeSource="
				+ changeSource + "]";
	}
}

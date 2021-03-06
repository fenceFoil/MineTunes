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
package com.minetunes.ditty.event;

import com.minetunes.particle.ParticleRequest;

/**
 * @author William
 * 
 */
public class ParticleEvent extends TimedDittyEvent {

	private ParticleRequest particle;

	/**
	 * 
	 * @param particle
	 * @param dittyID
	 */
	public ParticleEvent(ParticleRequest particle, int dittyID) {
		super(dittyID);
		setParticle(particle);
	}

	public ParticleRequest getParticle() {
		return particle;
	}

	public void setParticle(ParticleRequest particle) {
		this.particle = particle;
	}

}

/* ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 *
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 *
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 *
 * GTNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GTNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * ---------------------------------------
 * DeterministicRandom.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.util;

import java.util.Random;

/**
 * @author Tim
 *
 */
public class DeterministicRandom extends Random {

    private long seed;

    public DeterministicRandom() {
	super();
    }
    
    public DeterministicRandom(long seed) {
	super(seed);
	this.seed = seed;
    }
    
    public void resetSeed() {
	this.setSeed(seed);
    }
    
    public void setNewSeed(long seed) {
	this.setSeed(seed);
    }
    
    /**
     * Returns the seed of the RNG
     * @return	Seed or <b>null<b> of no seed is set
     */
    public long getSeed() {
	return seed;
    }
    
    
}

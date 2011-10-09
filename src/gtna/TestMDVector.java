package gtna;

import static org.junit.Assert.*;
import gtna.util.MDVector;

import org.junit.Test;
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
 * TestMDVector.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */

/**
 * @author Nico
 *
 */
public class TestMDVector {
	@Test public void testDotProduct() {
		MDVector x = new MDVector( new double[]{1,3,-5});
		MDVector y = new MDVector( new double[]{4,-2,-1});
		assertEquals ( 3, x.dotProduct(y), 0);
	}
	
	@Test public void testAngle() {
		MDVector a = new MDVector( new double[]{1,2,3});
		MDVector b = new MDVector(new double[]{-7,8,9});
		assertEquals( 46.3, a.angleTo(b), 1);
	}
}

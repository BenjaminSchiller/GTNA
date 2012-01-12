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
 * LMCAttackerConvergence.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.attackableEmbedding.lmc;

import gtna.graph.Graph;

import java.util.Random;


/**
 * attacker behaving randomly
 * @author stefanieroos
 *
 */
public class LMCAttackerConvergence extends LMCNode{
	
	 public LMCAttackerConvergence(int index, Graph g, LMC lmc) {
	 super(index, g, lmc);
	 }
	
	 /**
	 * select a random ID
	 */
	 public void turn(Random rand) {
	      this.lmc.getIds()[this.getIndex()].setPosition(rand.nextDouble());
	 }
	
	 /**
	 * return a random ID
	 */
	 protected double ask(LMCNode caller, Random rand) {
	     return rand.nextDouble();
	 }

}

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
 * CircularAbstract.java
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
package gtna.transformation.gd;

import java.util.Arrays;
import java.util.HashSet;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.IdentifierSpace;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace;
import gtna.id.ring.RingPartition;
import gtna.util.Util;

/**
 * @author Nico
 *
 */
public abstract class CircularAbstract extends GraphDrawingAbstract {

	protected RingIdentifierSpace idSpace;
	protected RingPartition[] partitions;
	protected int realities;
	protected double modulus;
	protected Boolean wrapAround;
	HashSet<String> handledEdges;
	
	public CircularAbstract(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}

	protected void initIDSpace( Graph g ) {
		if ( !generateIDSpace ) return;
		
		for (int r = 0; r < this.realities; r++) {
			partitions = new RingPartition[g.getNodes().length];
			idSpace = new RingIdentifierSpace(partitions, this.modulus,
					this.wrapAround);
			RingIdentifier[] ids = new RingIdentifier[g.getNodes().length];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = RingIdentifier.rand(rand, idSpace);
			}
			Arrays.sort(ids);
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new RingPartition(ids[i], ids[(i + 1)
						% ids.length]);
			}
			Util.randomize(partitions, rand);
		}
	}

	protected void writeIDSpace ( Graph g ) {
		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
	}
	
	public void setIDSpace(IdentifierSpace idSpace) {
		this.idSpace = (RingIdentifierSpace) idSpace.clone();
		this.partitions = (RingPartition[]) this.idSpace.getPartitions();
		this.modulus = this.idSpace.getModulus();
		this.generateIDSpace = false;
	}

	protected int getPredecessor(int i) {
		double predEnd = partitions[i].getStart().getPosition();
		
		for (int j = 0; j < partitions.length; j++) {
			if ( partitions[j].getEnd().getPosition() == predEnd ) return j;
		}
		
		throw new RuntimeException("There's a hole in the RingIdentifierSpace!");
	}
	
	protected int getSuccessor(int i) {
		double succStart = partitions[i].getEnd().getPosition();
		
		for (int j = 0; j < partitions.length; j++) {
			if ( partitions[j].getStart().getPosition() == succStart ) return j;
		}
		
		throw new RuntimeException("There's a hole in the RingIdentifierSpace!");
	}
	
	protected void swapPositions(int i, int j) {
		RingPartition temp = partitions[i];
		partitions[i] = partitions[j];
		partitions[j] = temp;
	}
}

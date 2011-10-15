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
 * WetherellShannon.java
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

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.plot.Gephi;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

/**
 * @author Nico
 *
 */
public class WetherellShannon extends TransformationImpl implements Transformation {
	private final int firstVisit = 1;
	
	private Gephi gephi;

	private int[] heightModifiers, nodeModifiers;
	private int[] nodePositionsX, nodePositionsY;
	private int[] nextPos;

	private int modifierSum;

	public WetherellShannon() {
		this("GDA_WETHERELL_SHANNON", new String[]{}, new String[]{});
	}
	
	public WetherellShannon(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}	
	
	public WetherellShannon(Gephi plotter) {
		this("GDA_WETHERELL_SHANNON", new String[]{}, new String[]{});
		this.gephi = plotter;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}
	
	@Override
	public Graph transform(Graph g) {
			/*
			 * Is there a better way to retrieve a good root node?
			 */
		Node root = g.getNode(0);
		Graph tree = g.getSpanningTree(root);
		int maxHeight = g.getNodes().length; 
		
		heightModifiers = new int[maxHeight];
		nodeModifiers = new int[maxHeight];
		nextPos = new int[maxHeight];
		nodePositionsX = new int[maxHeight];
		nodePositionsY = new int[maxHeight];
		for ( int i = 0; i < maxHeight; i++ ) {
			heightModifiers[i] = 0;
			nextPos[i] = 1;
		}
		firstWalk ( tree, root, 0 );
		
		modifierSum = 0;
		secondWalk ( tree, root, 0 );
		
		for ( Node i: tree.getNodes() ) {
			if ( i == null ) {
				System.out.println("Missing node");
				continue;
			}
			System.out.print("Node " + i.getIndex() + " resides at " + nodePositionsX[i.getIndex()] + "|" + nodePositionsY[i.getIndex()] + " and has edges to ");
			for ( int j: i.getOutgoingEdges() ) System.out.print(j + " ");
			System.out.println();
		}			
		
		return g;
	}
	
	private void firstWalk ( Graph g, Node n, int height ) {
		int[] sons = n.getOutgoingEdges();
		for ( int singleSon: sons ) {
			firstWalk(g, g.getNode(singleSon), height + 1 );
		}
		
			/*
			 * So, for now, we have traveled through all children
			 * and should either have gotten to a leaf or we're on
			 * the way back to the top of the tree
			 */
		int place = 0;
		if ( sons.length == 0 ) {
				/*
				 * Current node has no childs, so use the
				 * next free position
				 */
			place = nextPos[height];
		} else {
				/*
				 * Put the node centered over its children
				 */
			for ( int singleSon: sons ) {
				place += nodePositionsX[singleSon];
			}
			place = place / sons.length;
		}
		
		heightModifiers[height] = Math.max ( heightModifiers[height], nextPos[height] - place );
		if ( sons.length == 0 ) {
			nodePositionsX[n.getIndex()] = place;
		} else {
			nodePositionsX[n.getIndex()] = place + heightModifiers[height];
		}
			/*
			 * This might be a problematic point, as +2 results from binary trees
			 */
		nextPos[height] = nodePositionsX[n.getIndex()] + 2;
		nodeModifiers[n.getIndex()] = heightModifiers[height];
	}
	
	private void secondWalk ( Graph g, Node n, int height ) {
		nodePositionsX[n.getIndex()] = nodePositionsX[n.getIndex()] + modifierSum;
		modifierSum = modifierSum + nodeModifiers[n.getIndex()];
		nodePositionsY[n.getIndex()] = 2 * height + 1;
		
		int[] sons = n.getOutgoingEdges();
		for ( int singleSon: sons ) {
			secondWalk(g, g.getNode(singleSon), height + 1 );
		}
		
		modifierSum = modifierSum - heightModifiers[n.getIndex()];
	}
}

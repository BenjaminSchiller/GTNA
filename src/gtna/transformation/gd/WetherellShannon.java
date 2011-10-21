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
import gtna.graph.spanningTree.SpanningTree;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.plot.GraphPlotter;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

/**
 * @author Nico
 *
 */
public class WetherellShannon extends TransformationImpl implements Transformation {
	private GraphPlotter graphPlotter;

	private int[] heightModifiers, nodeModifiers;
	private int[] nodePositionsX, nodePositionsY;
	private int[] nextPos;
	
	private double modulusX, modulusY;

	private int modifierSum;
	private SpanningTree tree;

	public WetherellShannon() {
		this("GDA_WETHERELL_SHANNON", new String[]{}, new String[]{});
	}
	
	public WetherellShannon(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}	
	
	public WetherellShannon(double modulusX, double modulusY, GraphPlotter plotter) {
		this("GDA_WETHERELL_SHANNON", new String[] {}, new String[] {});
		this.modulusX = modulusX;
		this.modulusY = modulusY;
		this.graphPlotter = plotter;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("SPANNINGTREE");
	}
	
	@Override
	public Graph transform(Graph g) {
		tree = (SpanningTree) g.getProperty("SPANNINGTREE");
		int source = tree.getSrc();
		int maxHeight = g.getNodes().length; 
		
		heightModifiers = new int[maxHeight];
		nodeModifiers = new int[maxHeight];
		nextPos = new int[maxHeight];
		nodePositionsX = new int[maxHeight];
		nodePositionsY = new int[maxHeight];
		for ( int i = 0; i < maxHeight; i++ ) {
			heightModifiers[i] = 0;
			nextPos[i] = 1;
			nodePositionsX[i] = 0;
			nodePositionsY[i] = 0;
		}
		firstWalk ( tree, source, 0 );
		
		modifierSum = 0;
		secondWalk ( tree, source, 0 );
		
		for ( Node i: g.getNodes() ) {
			if ( i == null ) {
				System.out.println("Missing node");
				continue;
			}
			System.out.print("Node " + i.getIndex() + " resides at " + nodePositionsX[i.getIndex()] + "|" + nodePositionsY[i.getIndex()] + " and has edges to ");
			for ( int j: tree.getChildren(i.getIndex()) ) System.out.print(j + " ");
			System.out.println();
		}
		
		setCoordinates(g);
		graphPlotter.plotFinalGraph(g);
		graphPlotter.plotSpanningTree(g);
		
		return g;
	}
	
	private void firstWalk ( SpanningTree tree, int n, int height ) {
		int[] sons = tree.getChildren(n);
		for ( int singleSon: sons ) {
			firstWalk(tree, singleSon, height + 1 );
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
			nodePositionsX[n] = place;
		} else {
			nodePositionsX[n] = place + heightModifiers[height];
		}
			/*
			 * This might be a problematic point, as +2 results from binary trees
			 */
		nextPos[height] = nodePositionsX[n] + 2;
		nodeModifiers[n] = heightModifiers[height];
	}
	
	private void secondWalk ( SpanningTree tree, int n, int height ) {
		nodePositionsX[n] = nodePositionsX[n] + modifierSum;
		modifierSum = modifierSum + nodeModifiers[n];
		nodePositionsY[n] = 2 * height + 1;
		
		int[] sons = tree.getChildren(n);
		for ( int singleSon: sons ) {
			secondWalk(tree, singleSon, height + 1 );
		}
		
		modifierSum = modifierSum - nodeModifiers[n];
	}
	
	private void setCoordinates( Graph graph ) {
			/*
			 * As the current coordinates could exceed the given
			 * idSpace (or on the other hand use only a tiny pane),
			 * we need to calculate a scale factor for the coordinates
			 */
		double scaleX = 0, scaleY = 0;
		for ( int i= 0; i < nodePositionsX.length; i++ ) {
			scaleX = Math.max(scaleX, nodePositionsX[i]);
			scaleY = Math.max(scaleY, nodePositionsY[i]);
		}
			/*
			 * The current scale factor would also use values on the borders
			 * of the idSpace (which will be cut to 0 by the modulus). So: scale
			 * it a tiny bit smaller
			 */
		scaleX = scaleX * 1.1;
		scaleY = scaleY * 1.1;
		
		PlaneIdentifier pos;
		PlanePartitionSimple[] partitions = new PlanePartitionSimple[graph.getNodes().length];
		PlaneIdentifierSpaceSimple idSpace = new PlaneIdentifierSpaceSimple(partitions, this.modulusX, this.modulusY,
				false);
		for (int i = 0; i < nodePositionsX.length; i++) {
			pos = new PlaneIdentifier(( nodePositionsX[i] / scaleX ) * idSpace.getModulusX(), ( nodePositionsY[i] / scaleY ) * idSpace.getModulusY(), idSpace);
			partitions[i] = new PlanePartitionSimple(pos);
		}
		graph.addProperty(graph.getNextKey("ID_SPACE"), idSpace);
	}
}

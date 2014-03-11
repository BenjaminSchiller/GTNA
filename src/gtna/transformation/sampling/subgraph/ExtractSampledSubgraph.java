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
 * SubgraphTransformation.java
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
package gtna.transformation.sampling.subgraph;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.Sample;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;

/**
 * @author Tim
 * 
 */
public class ExtractSampledSubgraph extends Transformation {

	private int index = 0;

	/**
	 * @param key
	 */
	public ExtractSampledSubgraph() {
		super("SUBGRAPH", new Parameter[] { new StringParameter(
				"SUBGPRAPHFUNCTION", "extract") });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		
		Graph gi = new Graph(g.getName() + " (SAMPLED)");
		Sample sample = (Sample) g.getProperty("SAMPLE_" + this.index);

		HashSet<Integer> sampledIds = new HashSet<Integer>();
		sampledIds.addAll(sample.getSampledIds()); // old Ids
		
		Node[] ni = Node.init(sampledIds.size(), gi);
		Edges e = new Edges(ni, g.computeNumberOfEdges());

		gi.setNodes(ni);
		
		int[] map = new int[g.getNodeCount()];
		Arrays.fill(map, -1);
		
		for(Integer id : sampledIds){ // save at index i the new node index of node i
			map[id] = sample.getNewNodeId(id);
		}		
		
		Node[] nodes = g.getNodes();
		for(Integer id : sampledIds){
			Node src = nodes[id];
			for(int dst : src.getOutgoingEdges()){
				if(map[dst]>0){
					e.add(map[id], map[dst]);

				}
			}
		}
				
		e.fill();

		return gi;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("SAMPLE_" + this.index);
	}

	public void setIndex(int i) {
		this.index = i;
	}

}

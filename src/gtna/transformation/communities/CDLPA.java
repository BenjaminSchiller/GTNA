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
 * Communities.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors: florian;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.communities;

import gtna.communities.CommunityList;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorting;
import gtna.transformation.Transformation;
import gtna.util.Util;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class CDLPA extends Transformation {
	private int iterationLimitFactor;

	/**
	 * Standard constructor. Only argument is the factor that determines the
	 * maximum number of iterations that is being done. This maximum number is
	 * calculated by multiplying the number of nodes with this factor. With a
	 * value above 50, it should only stop infinite loopings while not affecting
	 * normal community detection. However, even if it stops infinite loops,
	 * depending on the number of nodes this might still take a very long time.
	 * 
	 * @param factor
	 *            The maximum number of iterations is determined by factor *
	 *            nodes.
	 */
	public CDLPA(int limitFactor) {
		super("CD_LPA", new Parameter[] { new IntParameter(
				"LIMIT_FACTOR", limitFactor) });
		this.iterationLimitFactor = limitFactor;
	}

	@Override
	public Graph transform(Graph g) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

		int[] labels = labelPropagationAlgorithm(g.getNodes());
		HashMap<Integer, Integer> labelCommunityMapping = Util
				.mapLabelsToCommunities(labels);

		for (Node n : g.getNodes()) {
			map.put(n.getIndex(),
					labelCommunityMapping.get(labels[n.getIndex()]));
		}

		g.addProperty(g.getNextKey("COMMUNITIES"),
				new CommunityList(map));
		return g;
	}

	/**
	 * Assigns labels to nodes using the label propagation algorithm.
	 * 
	 * @param nodes
	 * @return an array containing the label of each node
	 */
	private int[] labelPropagationAlgorithm(Node[] nodes) {
		Random rand = new Random();
		boolean finished = false;
		int[] labels = new int[nodes.length];
		for (int i = 0; i < labels.length; i++)
			labels[i] = i;

		int count = 0;

		while (!finished) {

			Node[] X = NodeSorting.random(nodes, rand);
			finished = true;
			for (Node x : X) {
				ArrayList<Integer> maxLabels = selectMaxLabels(
						x.getOutgoingEdges(), labels);
				if (!maxLabels.isEmpty()) {
					int maxLabel = maxLabels
							.get(rand.nextInt(maxLabels.size()));
					if (!maxLabels.contains(labels[x.getIndex()]))
						finished = false;
					labels[x.getIndex()] = maxLabel;
				}
			}
			count++;

			if (count > nodes.length * iterationLimitFactor)
				finished = true;
		}
		return labels;
	}

	/**
	 * Selects the labels occurring with the highest frequency among the
	 * neighboring nodes.
	 * 
	 * @param nodes
	 * @param labels
	 * @return list of labels occuring with the highest frequency
	 */
	private ArrayList<Integer> selectMaxLabels(int[] nodes, int[] labels) {
		HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>(); // <label,
																				// count>
		ArrayList<Integer> maxLabels = new ArrayList<Integer>();
		int maxCount = 0;

		for (int node : nodes) {
			int label = labels[node];
			int count = counter.containsKey(label) ? counter.get(label) + 1 : 1;
			counter.put(label, count);
			if (count > maxCount) {
				maxCount = count;
			}
		}

		for (int label : counter.keySet()) {
			if (counter.get(label) == maxCount) {
				maxLabels.add(label);
			}
		}

		return maxLabels;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}
}

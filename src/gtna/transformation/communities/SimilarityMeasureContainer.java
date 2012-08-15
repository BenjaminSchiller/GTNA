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
 * IHaveNoIdeaHowToNameThis.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.communities;

import java.util.ArrayList;
import java.util.HashMap;

import gtna.communities.Community;
import gtna.communities.CommunityList;
import gtna.graph.Node;
import gtna.util.Util;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

/**
 * The <code>SimilarityMeasureContainer</code> contains the logic to calculate
 * the similarity of all nodes to eachother and then create a community from
 * this similarities. The actualy similarity calculation is done by the
 * <code>SimilarityMeasure</code> that is supplied when creating this
 * <code>SimilarityMeasureContainer</code>.
 * 
 * 
 * @author Philipp Neubrand
 * 
 */
public class SimilarityMeasureContainer {

	int[][] data;
	private SimilarityMeasure simMeasure;
	private double threshold;

	/**
	 * Creates a new <code>SimilarityMeasureContainer</code> with the supplied
	 * similarity measure and the supplied threshold. The threshold is between 0
	 * and 1 and if the best similarity for a node is below (threshold *
	 * maxValue()) the node will remain solo.
	 * 
	 * @param simMeasure
	 *            The <code>SimilarityMeasure</code> that should be used to
	 *            calculate the similarities.
	 * @param threshold
	 *            The threshold.
	 */
	public SimilarityMeasureContainer(SimilarityMeasure simMeasure,
			double threshold) {

		this.simMeasure = simMeasure;
		this.threshold = threshold;
	}

	/**
	 * Setter for the size of the graph of this SMC.
	 * 
	 * @param size
	 *            The number of nodes of the graph.
	 */
	public void setSize(int size) {
		data = new int[size][size];
	}

	/**
	 * Adds the supplied community for the supplied node to the data.
	 * 
	 * @param node
	 *            The node for which the community was calculated.
	 * @param com
	 *            The community that is to be added.
	 */
	public void addCommunityOfNode(Node node, Community com) {
		for (int i : com.getNodes()) {
			data[node.getIndex()][i] = 1;
		}
	}
	

	/**
	 * Adds the supplied community for the supplied node to the data.
	 * 
	 * @param node
	 *            The node for which the community was calculated.
	 * @param com
	 *            The community that is to be added.
	 */
	public void addCommunityOfNode(Node node, HashMap<Integer, Integer> com) {
		for(int akt : com.keySet())
			data[node.getIndex()][akt] = 1;
		
	}

	/**
	 * Calculates and returns the community list.
	 * 
	 * @return The community list.
	 */
	public CommunityList getCommunityList() {
		HashMap<Integer, ArrayList<Integer>> coms = new HashMap<Integer, ArrayList<Integer>>();

		double[][] sims = new double[data.length][data.length];
		int[] combuffer = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			combuffer[i] = i;
			coms.put(i, new ArrayList<Integer>());
			coms.get(i).add(i);
			for (int j = i + 1; j < data.length; j++) {
				sims[i][j] = simMeasure.calcSimilarity(data[i], data[j]);
			}
		}

		double maxValue = simMeasure.minValue();
		int mostSimilarNode = -1;

		double val = 0;
		for (int i = 0; i < data.length; i++) {
			mostSimilarNode = -1;
			maxValue = simMeasure.minValue();
			for (int j = 0; j < data.length; j++) {
				if (i == j)
					continue;

				if (j < i) {
					val = sims[j][i];
				} else if (j > i) {
					val = sims[i][j];
				}

				if (val >= maxValue) {
					mostSimilarNode = j;
					maxValue = val;
				}
			}
			if (maxValue < threshold
					* simMeasure.getMaxValue(data[i], data[mostSimilarNode]))
				continue;

			for (int j = 0; j < i; j++) {
				if (coms.get(j) == coms.get(i)) {
					coms.get(mostSimilarNode).addAll(coms.get(j));
					coms.remove(j);
				}
			}

			if (combuffer[mostSimilarNode] == combuffer[i])
				continue;

			coms.get(combuffer[mostSimilarNode]).addAll(coms.get(i));

			coms.remove(i);
			for (int k = 0; k < data.length; k++)
				if (combuffer[k] == combuffer[i])
					combuffer[k] = combuffer[mostSimilarNode];

			combuffer[i] = combuffer[mostSimilarNode];

		}

		ArrayList<Community> ret = new ArrayList<Community>();
		int id = 0;
		for (ArrayList<Integer> akt : coms.values()) {
			ret.add(new Community(id, akt));
			id++;
		}

		return new CommunityList(ret);
	}

	/**
	 * Getter for the parameter[] of the configuration parameters.
	 * 
	 * @return An <code>Parameter[]</code> containing the configuration
	 *         parameters.
	 */
	public Parameter[] getParameterArray() {
		return Util.mergeArrays(new Parameter[] { new DoubleParameter(
				"THRESHOLD", threshold) }, simMeasure.getParameterArray());
	}
}

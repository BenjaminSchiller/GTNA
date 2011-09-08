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

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorting;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author benni
 * 
 */
public class CommunityGeneration extends TransformationImpl implements Transformation {

	public CommunityGeneration() {
		super("COMMUNITIES", new String[] {}, new String[] {});
	}
	
	@Override
	public Graph transform(Graph g) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        int[] labels = labelPropagationAlgorithm(g.getNodes());
        HashMap<Integer, Integer> labelCommunityMapping = mapLabelsToCommunities(labels);

		for (Node n : g.getNodes()) {
			map.put(n.getIndex(), labelCommunityMapping.get(labels[n.getIndex()]));
		}

		g.addProperty(g.getNextKey("COMMUNITIES"), new gtna.communities.Communities(map));
		return g;
	}


    /**
     * Assigns labels to nodes using the label propagation algorithm.
     * @param nodes
     * @return an array containing the label of each node
     */
    private int[] labelPropagationAlgorithm(Node[] nodes){
        Random rand = new Random();
        boolean finished = false;
        int[] labels = new int[nodes.length];
        for(int i = 0; i < labels.length; i++)
            labels[i] = i;

        while (!finished){
            Node[] X = NodeSorting.random(nodes, rand);
            for (Node x : X){
                ArrayList<Integer> maxLabels = selectMaxLabels(x.getOutgoingEdges(), labels);
                if (!maxLabels.isEmpty()){
                    int maxLabel = maxLabels.get(rand.nextInt(maxLabels.size()));
                    labels[x.getIndex()] = maxLabel;
                }
            }

            finished = true;
            for (Node x : X){
                ArrayList<Integer> maxLabels = selectMaxLabels(x.getOutgoingEdges(), labels);
                if (!maxLabels.isEmpty() && !maxLabels.contains(labels[x.getIndex()])){
                    finished = false;
                }
            }
        }
        return labels;
    }


    /**
     * Selects the labels occurring with the highest frequency among the neighboring nodes.
     * @param nodes
     * @param labels
     * @return list of labels occuring with the highest frequency
     */
    private ArrayList<Integer> selectMaxLabels(int[] nodes, int[] labels){
        HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>(); //<label, count>
        ArrayList<Integer> maxLabels = new ArrayList<Integer>();
        int maxCount = 0;

        for(int node : nodes){
            int label = labels[node];
            int count = counter.containsKey(label) ? counter.get(label) + 1 : 1;
            counter.put(label, count);
            if (count > maxCount){
                maxCount = count;
            }
        }

        for(int label : counter.keySet()){
            if (counter.get(label) == maxCount){
                maxLabels.add(label);
            }
        }

        return maxLabels;
    }

    /**
     * Maps labels to communities. Labels are mapped in ascending order, communities are
     * indexed from 0 to N, where N is the number of communities.
     * @param labels array of labels
     * @return mapping of labels to communities
     */
    private HashMap<Integer, Integer> mapLabelsToCommunities(int[] labels){
        SortedSet<Integer> labelSet = new TreeSet<Integer>();
        for(int label : labels){
            labelSet.add(label);
        }
        HashMap<Integer, Integer> labelCommunityMapping = new HashMap<Integer, Integer>();
        int communityIndex = 0;
        for(int label : labelSet){
            labelCommunityMapping.put(label, communityIndex++);
        }
        return labelCommunityMapping;
    }


	@Override
	public boolean applicable(Graph g) {
		return true;
	}
}

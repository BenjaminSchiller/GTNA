package gtna.transformation.communities;

import java.util.ArrayList;

import gtna.graph.sorting.NodeSorting;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;
import gtna.util.Config;
import gtna.util.Util;

public class CommunityDetectionLPAExtended extends TransformationImpl implements
		Transformation {
	
	public static final String key = "COMMUNITY_DETECTION_LPAEXTENDED";
	
	public CommunityDetectionLPAExtended() {
		super(key, Util.addPrefix(key, new String[] {"_W", "_F", "_D", "_M"}),
				new String[] {Config.get(key + "_W"), Config.get(key + "_F"), Config.get(key + "_D"), Config.get(key + "_M")});
	}

	public static interface EdgeWeight {
		public double getWeight(Node src, Node dst);
	}

	public static interface NodeCharacteristic {
		public double getCharacteristic(Node node);
	}

	public static class DefaultEdgeWeight implements EdgeWeight {
		public double getWeight(Node src, Node dst) {
			return 0.5;
		}
	}

	public static class DefaultNodeCharacteristic implements NodeCharacteristic {
		public double getCharacteristic(Node node) {
			return node.getOutDegree();
		}
	}
	
	public int[] labelPropagationAlgorithmExtended(Node[] nodes) {
		EdgeWeight w = null;
		NodeCharacteristic f = null;
		try {
			w = (EdgeWeight) Class.forName(Config.get(key() + "_W"))
					.newInstance();
			f = (NodeCharacteristic) Class.forName(Config.get(key() + "_F"))
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("invalid config - "
					+ e.getClass().getSimpleName() + ": " + e.getMessage());
		}
		double m = Double.parseDouble(Config.get(key() + "_M"));
		double d = Double.parseDouble(Config.get(key() + "_D"));

		int[] labels = new int[nodes.length];
		double[] scores = new double[nodes.length];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = i;
			scores[i] = 1.0;
		}
		Random rand = new Random(System.currentTimeMillis());

		// label propagation loop
		boolean finished = false;
		while (!finished) {
			finished = true;
			Node[] X = NodeSorting.random(nodes, rand);
			for (Node x : X) {
				ArrayList<Integer> maxLabels = selectMaxLabelsExtended(x,
						nodes, labels, scores, w, f, m);
				if (!maxLabels.isEmpty()) {
					int maxLabel = maxLabels
							.get(rand.nextInt(maxLabels.size()));
					if(!maxLabels.contains(labels[x.getIndex()])){
						finished = false;
						scores[x.getIndex()] = scores[x.getIndex()] - d;
					}
					labels[x.getIndex()] = maxLabel;
				}
			}
		}
		return labels;
	}

	private ArrayList<Integer> selectMaxLabelsExtended(Node n, Node[] nodes,
			int[] l, double[] s, EdgeWeight w, NodeCharacteristic f, double m) {
		HashMap<Integer, Double> sums = new HashMap<Integer, Double>();
		Node dst = null;
		for (int akt : n.getOutgoingEdges()) {
			dst = nodes[akt];
			int label = l[dst.getIndex()];
			double weight = w.getWeight(n, dst) + w.getWeight(dst, n);
			double psum = s[dst.getIndex()]
					* Math.pow(f.getCharacteristic(dst), m) * weight;
			Double sum = sums.get(label);
			if (sum != null) {
				psum += sum;
			}
			sums.put(label, psum);
		}

		ArrayList<Integer> ret = new ArrayList<Integer>();

		double maxSum = 0;
		for (int label : sums.keySet()) {
			if (sums.get(label) > maxSum) {
				maxSum = sums.get(label);
				ret.clear();
				ret.add(label);
			} else if (sums.get(label) == maxSum)
				ret.add(label);
		}
		return ret;
	}
	

	@Override
	public boolean applicable(Graph g) {
		return true;
	}


	@Override
	public Graph transform(Graph g) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        int[] labels = labelPropagationAlgorithmExtended(g.getNodes());
        HashMap<Integer, Integer> labelCommunityMapping = mapLabelsToCommunities(labels);

		for (Node n : g.getNodes()) {
			map.put(n.getIndex(), labelCommunityMapping.get(labels[n.getIndex()]));
		}

		g.addProperty(g.getNextKey("COMMUNITIES"), new gtna.communities.Communities(map));
		return g;
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
}

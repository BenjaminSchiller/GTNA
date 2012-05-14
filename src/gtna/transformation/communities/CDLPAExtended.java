package gtna.transformation.communities;

import gtna.communities.CommunityList;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorting;
import gtna.transformation.Transformation;
import gtna.util.Util;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CDLPAExtended extends Transformation {

	public static final String key = "CD_LPAEXTENDED";
	private double d;
	private double m;
	private NodeCharacteristic nc;
	private EdgeWeight ew;

	public CDLPAExtended(double d, double m, NodeCharacteristic nc, EdgeWeight ew) {
		super(key, new Parameter[] {
				new DoubleParameter("D", d),
				new DoubleParameter("M", m),
				new StringParameter("NODE_CHARACTERISTIC", nc.getKey()),
				new StringParameter("EDGE_WEIGHT", ew.getKey())});

		this.d = d;
		this.m = m;
		this.nc = nc;
		this.ew = ew;
	}

	public static abstract class EdgeWeight {
		private String key;
		public abstract double getWeight(Node src, Node dst);
		
		public EdgeWeight(String key){
			this.key = key;
		}

		public String getKey(){
			return key;
		}
	}

	public static abstract class NodeCharacteristic {
		private String key;
		public abstract double getCharacteristic(Node node);

		public String getKey(){
			return key;
		}
		
		public NodeCharacteristic(String key){
			this.key = key;
		}
	}

	public static class DefaultEdgeWeight extends EdgeWeight {
		public DefaultEdgeWeight(){
			super("DEFAULT");
		}
		
		public double getWeight(Node src, Node dst) {
			return 0.5;
		}
	}

	public static class DefaultNodeCharacteristic extends NodeCharacteristic {
		public DefaultNodeCharacteristic(){
			super("DEFAULT");
		}
		
		public double getCharacteristic(Node node) {
			return node.getOutDegree();
		}
	}

	public int[] labelPropagationAlgorithmExtended(Node[] nodes) {

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
						nodes, labels, scores, ew, nc, m);
				if (!maxLabels.isEmpty()) {
					int maxLabel = maxLabels
							.get(rand.nextInt(maxLabels.size()));
					if (!maxLabels.contains(labels[x.getIndex()])) {
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
}

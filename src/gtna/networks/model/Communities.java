package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;
import gtna.util.Util;

import java.util.ArrayList;
import java.util.Random;

public class Communities extends NetworkImpl implements Network {
	private int[] sizes;

	private double[] avgInLinks;

	private double[] avgOutLinks;

	private boolean bidirectional;

	public Communities(int n, double avgCommunitySize,
			double avgInLinks, double avgOutLinks, boolean bidirectional,
			RoutingAlgorithm r, Transformation[] t) {
		super("COMMUNITIES", n, new String[] { "AVG_COMMUNITY_SIZE",
				"AVG_IN_LINKS", "AVG_OUT_LINKS" }, new String[] {
				"" + avgCommunitySize, "" + avgInLinks, "" + avgOutLinks }, r,
				t);
		Random rand = new Random(System.currentTimeMillis());
		this.bidirectional = bidirectional;
		if (n <= avgCommunitySize) {
			this.sizes = new int[] { n };
			this.avgInLinks = new double[] { avgInLinks };
			this.avgOutLinks = new double[] { avgOutLinks };
		} else {
			ArrayList<Integer> sizes = new ArrayList<Integer>();
			int total = n;
			while (total > 0) {
				if (total <= avgCommunitySize) {
					sizes.add(total);
					total = 0;
				} else {
					int current = (int) (avgCommunitySize + ((double) avgCommunitySize * rand
							.nextGaussian()));
					if (current > total) {
						sizes.add(total);
						total = 0;
					} else if (current > 0) {
						sizes.add(current);
						total -= current;
					}
				}
			}
			this.sizes = Util.toIntegerArray(sizes);
			// this.sizes = new int[(int) Math.ceil((double) n /
			// avgCommunitySize)];
			// for (int i = 0; i < this.sizes.length - 1; i++) {
			// this.sizes[i] = (int) (avgCommunitySize + ((double)
			// avgCommunitySize * rand
			// .nextGaussian()));
			// }
			// this.sizes[this.sizes.length - 1] = n - Util.sum(this.sizes);
			this.avgInLinks = new double[this.sizes.length];
			for (int i = 0; i < this.avgInLinks.length; i++) {
				this.avgInLinks[i] = avgInLinks + avgInLinks
						* rand.nextGaussian();
			}
			this.avgOutLinks = new double[this.sizes.length];
			for (int i = 0; i < this.avgOutLinks.length; i++) {
				this.avgOutLinks[i] = avgOutLinks + avgOutLinks
						* rand.nextGaussian();
			}
		}
	}

	public Communities(int[] sizes, double[] avgInLinks,
			double[] avgOutLinks, boolean bidirectional, RoutingAlgorithm r,
			Transformation[] t) {
		super("COMMUNITIES_NETWORK", Util.sum(sizes), new String[] {
				"AVG_COMMUNITY_SIZE", "AVG_IN_LINKS", "AVG_OUT_LINKS" },
				new String[] { Util.toFolderString(sizes), Util.toFolderString(avgInLinks),
						Util.toFolderString(avgOutLinks) }, r, t);
		this.sizes = sizes;
		this.avgInLinks = avgInLinks;
		this.avgOutLinks = avgOutLinks;
		this.bidirectional = bidirectional;
	}

	public Graph generate() {
		Timer timer = new Timer();
		NodeImpl[] nodes = NodeImpl.init(this.nodes());
		Edges edges = new Edges(nodes, 0);
		Random rand = new Random(System.currentTimeMillis());
		NodeImpl[][] communities = new NodeImpl[this.sizes.length][];
		// fill communities
		int index = 0;
		for (int i = 0; i < communities.length; i++) {
			communities[i] = new NodeImpl[this.sizes[i]];
			for (int j = 0; j < communities[i].length; j++) {
				communities[i][j] = nodes[index++];
			}
		}
		// create internal links
		for (int i = 0; i < communities.length; i++) {
			double prob = this.avgInLinks[i]
					/ (double) (communities[i].length - 1);
			for (int j = 0; j < communities[i].length; j++) {
				for (int k = 0; k < communities[i].length; k++) {
					if (j != k && rand.nextDouble() <= prob) {
						edges.add(nodes[communities[i][j].index()],
								nodes[communities[i][k].index()]);
						if (this.bidirectional) {
							edges.add(nodes[communities[i][k].index()],
									nodes[communities[i][j].index()]);
						}
					}
				}
			}
		}
		// create external links
		for (int i = 0; i < communities.length; i++) {
			double prob = this.avgOutLinks[i]
					/ (double) (this.nodes() - communities[i].length);
			int start = communities[i][0].index();
			int end = communities[i][communities[i].length - 1].index();
			for (int j = 0; j < communities[i].length; j++) {
				for (int k = 0; k < nodes.length; k++) {
					if ((k < start || k > end) && rand.nextDouble() <= prob) {
						edges.add(nodes[communities[i][j].index()], nodes[k]);
						if (this.bidirectional) {
							edges.add(nodes[k],
									nodes[communities[i][j].index()]);
						}
					}
				}
			}
		}
		edges.fill();
		timer.end();
		return new Graph(this.description(), nodes, timer);
	}
}

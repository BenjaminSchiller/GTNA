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

public class Communities2 extends NetworkImpl implements Network {
	private int[] sizes;

	private double[] avgInLinks;

	private int[][] interCommunityLinks;

	private boolean bidirectional;

	public Communities2(int[] sizes, double[] avgInLinks,
			int[][] interCommunityLinks, boolean bidirectional,
			RoutingAlgorithm r, Transformation[] t) {
		super("COMMUNITIES2", Util.sum(sizes), new String[] {
				"COMMUNITY_SIZES", "AVG_IN_LINKS", "INTER_COMMUNITY_LINKS" },
				new String[] { Util.toFolderString(sizes),
						Util.toFolderString(avgInLinks),
						Util.toFolderString(interCommunityLinks) }, r, t);
		this.sizes = sizes;
		this.avgInLinks = avgInLinks;
		this.interCommunityLinks = interCommunityLinks;
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
		// create inter-community links
		for (int i = 0; i < this.interCommunityLinks.length; i++) {
			for (int j = 0; j < this.interCommunityLinks[i].length; j++) {
				if (i == j) {
					continue;
				}
				NodeImpl[] f = communities[i];
				NodeImpl[] t = communities[j];
				ArrayList<NodeImpl> from = this.fill(f, t.length);
				ArrayList<NodeImpl> to = this.fill(t, f.length);
				this.addInterCommunityEdges(from, to,
						this.interCommunityLinks[i][j], edges, rand);
			}
		}

		edges.fill();
		timer.end();
		return new Graph(this.description(), nodes, timer);
	}

	private ArrayList<NodeImpl> fill(NodeImpl[] community, int copies) {
		ArrayList<NodeImpl> set = new ArrayList<NodeImpl>(community.length
				* copies);
		for(int i=0; i<community.length; i++){
			for(int j=0; j<copies; j++){
				set.add(community[i]);
			}
		}
		return set;
	}

	private void addInterCommunityEdges(ArrayList<NodeImpl> from,
			ArrayList<NodeImpl> to, int links, Edges edges, Random rand) {
		int added = 0;
		while (added < links) {
			NodeImpl FROM = from.get(rand.nextInt(from.size()));
			NodeImpl TO = to.get(rand.nextInt(to.size()));
			from.remove(FROM);
			to.remove(TO);
			if (!edges.contains(FROM.index(), TO.index())) {
				added++;
				edges.add(FROM, TO);
				if (this.bidirectional) {
					edges.add(TO, FROM);
				}
			}
		}
	}
}

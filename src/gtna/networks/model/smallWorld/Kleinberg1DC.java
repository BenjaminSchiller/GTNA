package gtna.networks.model.smallWorld;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.Random;

/**
 * implement the Kleinberg model in one dimension, chosen neighbors within
 * distance C as local contacts instead of ring neighbors 'The Small-World
 * Phenomenon: An Algorithmic Perspective'
 * 
 * @author stefanie
 * 
 */
public class Kleinberg1DC extends Network {

	private int SHORT_RANGE_CONTACTS;
	private int LONG_RANGE_CONTACTS;

	private double CLUSTERING_EXPONENT;

	private boolean BIDIRECTIONAL;
	private boolean RANDOM;
	private int C;

	/**
	 * 
	 * @param nodes
	 *            : # of nodes
	 * @param SHORT_RANGE_CONTACTS
	 *            : #
	 * @param LONG_RANGE_CONTACTS
	 *            : #
	 * @param CLUSTERING_EXPONENT
	 *            : exponent in distance distribution
	 * @param BIDIRECTIONAL
	 * @param RANDOM
	 *            : true = IDs chosen randomly in [0,1), else equally
	 *            distributed i/nodes
	 * @param ra
	 * @param t
	 */
	public Kleinberg1DC(int nodes, int SHORT_RANGE_CONTACTS,
			int LONG_RANGE_CONTACTS, double CLUSTERING_EXPONENT,
			boolean BIDIRECTIONAL, boolean RANDOM, int C, Transformation[] t) {
		super("KLEINBERG_1DC", nodes,
				new Parameter[] {
						new IntParameter("SHORT_RANGE_CONTACTS",
								SHORT_RANGE_CONTACTS),
						new IntParameter("LONG_RANGE_CONTACTS",
								LONG_RANGE_CONTACTS),
						new DoubleParameter("CLUSTERING_EXPONENT",
								CLUSTERING_EXPONENT),
						new BooleanParameter("BIDIRECTIONAL", BIDIRECTIONAL),
						new BooleanParameter("RANDOM", RANDOM),
						new IntParameter("C", C) }, t);
		this.SHORT_RANGE_CONTACTS = SHORT_RANGE_CONTACTS;
		this.LONG_RANGE_CONTACTS = LONG_RANGE_CONTACTS;
		this.CLUSTERING_EXPONENT = CLUSTERING_EXPONENT;
		this.BIDIRECTIONAL = BIDIRECTIONAL;
		this.RANDOM = RANDOM;
		this.C = C;
	}

	public Graph generate() {
		Graph g = new Graph(this.getDescription());
		Random rand = new Random();
		Node[] nodes = new Node[this.getNodes()];
		RingPartitionSimple[] parts = new RingPartitionSimple[this.getNodes()];
		RingIdentifierSpaceSimple idSpace = new RingIdentifierSpaceSimple(
				parts, true);

		double[] pos = new double[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			if (this.RANDOM) {
				pos[i] = rand.nextDouble();
			} else {
				pos[i] = (double) i / nodes.length;
			}
			nodes[i] = new Node(i, g);
		}
		Arrays.sort(pos);
		for (int j = 0; j < pos.length; j++) {
			parts[j] = new RingPartitionSimple(new RingIdentifier(pos[j], true));
		}

		Edges edges = new Edges(nodes, this.getNodes()
				* (this.SHORT_RANGE_CONTACTS * 2 + this.LONG_RANGE_CONTACTS));
		// short-distance links
		for (int i = 0; i < nodes.length; i++) {
			int src = i;
			for (int j = 1; j <= this.SHORT_RANGE_CONTACTS; j++) {
				int l1 = (i + rand.nextInt(C) + 1) % nodes.length;
				edges.add(src, l1);
				edges.add(l1, src);
				int l2 = (nodes.length + i - rand.nextInt(C) - 1)
						% nodes.length;
				edges.add(src, l2);
				edges.add(l2, src);
			}
		}
		// long-distance links
		double sum = 0;
		for (int j = 1; j < nodes.length; j++) {
			sum += Math.pow(parts[0].distance(parts[j].getIdentifier()),
					-this.CLUSTERING_EXPONENT);
		}

		for (int i = 0; i < nodes.length; i++) {
			this.generateLongRangeContacts(sum, parts, i, rand, edges);
			if (this.RANDOM && i < nodes.length - 1) {
				sum = 0;
				for (int j = 0; j < nodes.length; j++) {
					if (i + 1 != j) {
						sum += Math
								.pow(parts[i + 1].distance(parts[j]
										.getIdentifier()),
										-this.CLUSTERING_EXPONENT);
					}
				}
			}
		}
		edges.fill();
		g.setNodes(nodes);
		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
		return g;
	}

	private void generateLongRangeContacts(double sum,
			RingPartitionSimple[] part, int nr, Random rand, Edges edges) {
		double[] rands = new double[this.LONG_RANGE_CONTACTS];
		for (int i = 0; i < rands.length; i++) {
			rands[i] = rand.nextDouble() * sum;
		}
		Arrays.sort(rands);

		double sum2 = 0;
		int current = 0;
		int found = 0;
		while (found < rands.length && current < part.length) {
			if (current == nr) {
				current++;
				continue;
			}
			sum2 = sum2
					+ Math.pow(
							part[nr].distance(part[current].getIdentifier()),
							-this.CLUSTERING_EXPONENT);
			;
			if (sum2 >= rands[found]) {
				edges.add(nr, current);
				found++;
				if (this.BIDIRECTIONAL) {
					edges.add(current, nr);
				}
			}
			current++;
		}
	}

}

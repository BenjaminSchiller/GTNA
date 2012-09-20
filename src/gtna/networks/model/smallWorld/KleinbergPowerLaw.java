package gtna.networks.model.smallWorld;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.Random;

/**
 * implement the Kleinberg model in one dimension 'The Small-World Phenomenon:
 * An Algorithmic Perspective'
 * 
 * @author stefanie
 * 
 */
public class KleinbergPowerLaw extends Network {

	private int SHORT_RANGE_CONTACTS;

	private double EXPONENT;

	private boolean BIDIRECTIONAL;
	private boolean RANDOM;
	private int CUTOFF;

	public double getAlpha() {
		return this.EXPONENT;
	}

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
	public KleinbergPowerLaw(int nodes, int SHORT_RANGE_CONTACTS,
			double EXPONENT, int CUTOFF, boolean BIDIRECTIONAL, boolean RANDOM,
			RoutingAlgorithm ra, Transformation[] t) {
		super("KLEINBERG_POWER_LAW", nodes, new Parameter[] {
				new IntParameter("SHORT_RANGE_CONTACTS", SHORT_RANGE_CONTACTS),
				new DoubleParameter("EXPONENT", EXPONENT),
				new IntParameter("CUTOFF", CUTOFF),
				new BooleanParameter("BIDIRECTIONAL", BIDIRECTIONAL),
				new BooleanParameter("RANDOM", RANDOM) }, t);
		this.SHORT_RANGE_CONTACTS = SHORT_RANGE_CONTACTS;

		this.EXPONENT = EXPONENT;
		this.BIDIRECTIONAL = BIDIRECTIONAL;
		this.RANDOM = RANDOM;
		this.CUTOFF = CUTOFF;
	}

	public Graph generate() {
		Graph g = new Graph(this.getDescription());
		Random rand = new Random();
		Node[] nodes = new Node[this.getNodes()];
		RingPartitionSimple[] parts = new RingPartitionSimple[this.getNodes()];

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
		RingIdentifierSpaceSimple idSpace = new RingIdentifierSpaceSimple(
				parts, true);

		double norm = 0;
		for (int j = 1; j < this.CUTOFF; j++) {
			norm = norm + Math.pow(j, -this.EXPONENT);
		}
		Edges edges = new Edges(
				nodes,
				(int) (this.getNodes() * (this.SHORT_RANGE_CONTACTS * 2 + norm)));
		// short-distance links
		for (int i = 0; i < nodes.length; i++) {
			int src = i;
			for (int j = 1; j <= this.SHORT_RANGE_CONTACTS; j++) {
				int dst = (i + j) % nodes.length;
				edges.add(src, dst);
				edges.add(dst, src);
			}
		}

		// long-distance links
		double sum = 0;
		for (int j = 1; j < nodes.length; j++) {
			sum += 1 / parts[0].distance(parts[j].getIdentifier());
		}

		for (int i = 0; i < nodes.length; i++) {
			int k = 1;
			double r = rand.nextDouble();
			double s = 1;
			while (r * norm > s) {
				k++;
				s = s + Math.pow(k, -this.EXPONENT);
			}
			this.generateLongRangeContacts(sum, parts, i, rand, edges, k);
			if (this.RANDOM && i < nodes.length - 1) {
				sum = 0;
				for (int j = 0; j < nodes.length; j++) {
					if (i + 1 != j) {
						sum += 1 / (parts[i + 1].distance(parts[j]
								.getIdentifier()));
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
			RingPartitionSimple[] part, int nr, Random rand, Edges edges,
			int degree) {
		double[] rands = new double[degree];
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
			sum2 = sum2 + 1 / part[nr].distance(part[current].getIdentifier());
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

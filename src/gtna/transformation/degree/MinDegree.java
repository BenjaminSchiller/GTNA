package gtna.transformation.degree;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.ArrayList;
import java.util.Random;

/**
 * Transforms the given graph so that every node has the given minimum in- and
 * out-degree by adding random from and to nodes with a smaller in- or
 * out-degree. Note that these links are created at random so the degree and
 * connectivity of other nodes is affected as well.
 * 
 * @author benni
 * 
 */
public class MinDegree extends TransformationImpl implements Transformation {
	private int minIn;

	private int minOut;

	private boolean bidirectional;

	public MinDegree(int minIn, int minOut, boolean bidirectional) {
		super("MIN_DEGREE",
				new String[] { "MIN_IN", "MIN_OUT", "BIDIRECTIONAL" },
				new String[] { "" + minIn, "" + minOut, "" + bidirectional });
		this.minIn = minIn;
		this.minOut = minOut;
		this.bidirectional = bidirectional;
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		ArrayList<NodeImpl> mins = new ArrayList<NodeImpl>();
		for (int i = 0; i < g.nodes.length; i++) {
			if (g.nodes[i].in().length < this.minIn
					|| g.nodes[i].out().length < this.minOut) {
				mins.add(g.nodes[i]);
			}
		}
		Random rand = new Random(System.currentTimeMillis());
		while (mins.size() > 0) {
			int index = rand.nextInt(mins.size());
			NodeImpl current = mins.get(index);
			if (current.in().length >= this.minIn
					&& current.out().length >= this.minOut) {
				mins.remove(index);
				continue;
			}
			if (this.bidirectional) {
				NodeImpl randLink = g.nodes[rand.nextInt(g.nodes.length)];
				while (current.hasIn(randLink) || current.hasOut(randLink)
						|| current.index() == randLink.index()) {
					randLink = g.nodes[rand.nextInt(g.nodes.length)];
				}
				current.addIn(randLink);
				current.addOut(randLink);
				randLink.addIn(current);
				randLink.addOut(current);
			} else if (current.in().length < this.minIn) {
				NodeImpl randLink = g.nodes[rand.nextInt(g.nodes.length)];
				while (current.hasIn(randLink)
						|| current.index() == randLink.index()) {
					randLink = g.nodes[rand.nextInt(g.nodes.length)];
				}
				current.addIn(randLink);
				randLink.addOut(current);
			} else if (current.out().length < this.minOut) {
				NodeImpl randLink = g.nodes[rand.nextInt(g.nodes.length)];
				while (current.hasOut(randLink)
						|| current.index() == randLink.index()) {
					randLink = g.nodes[rand.nextInt(g.nodes.length)];
				}
				current.addOut(randLink);
				randLink.addIn(current);
			}
		}
		return new Graph(g.name, g.nodes, g.timer);
	}

}

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
 * DirectedMotifs3.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Dirk;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.motifs;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.Metric;
import gtna.networks.Network;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Counter for directed 3 node motifs
 * 
 * @author Dirk
 *
 */
public class DirectedMotifs3 extends MotifCounter {

	/**
	 * @param key
	 */
	public DirectedMotifs3() {
		super("DIRECTED_MOTIFS_3");
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.counts = new double[13];
		
		
		for (Node nodeA : g.getNodes()) {
		
			int a = nodeA.getIndex();
			
			HashSet<Node> connectedNodes = this.getConnectedNodes(g, nodeA);
			
			for (Node nodeB : connectedNodes) {
				
				int b = nodeB.getIndex();
				
				boolean ab = g.getEdges().contains(a, b);
				boolean ba = g.getEdges().contains(b, a);

				for (Node nodeC : connectedNodes) {
					int c = nodeC.getIndex();
					
					boolean ac = g.getEdges().contains(a, c);
					boolean ca = g.getEdges().contains(c, a);
					boolean bc = g.getEdges().contains(b, c);
					boolean cb = g.getEdges().contains(c, b);

					if (!bc && !cb) {
						if (b < c) {
							this.counts[this.getType(ab, ba, ac, ca)-1]++;
						}
					} else {
						if (a < b
								&& b < c) {
							this.counts[this.getType(ab, ba, ac, ca, bc, cb)-1]++;
						}
					}
				}
			}
		}		
	}
	
	protected HashSet<Node> getConnectedNodes(Graph g, Node node) {
		HashSet<Node> set = new HashSet<Node>();
		
		for (int i : node.getOutgoingEdges())
			set.add(g.getNode(i));
		
		for (int i : node.getIncomingEdges())
			set.add(g.getNode(i));
		
		return set;
	}
	
	
	protected int getType(boolean ab, boolean ba, boolean ac,
			boolean ca) {
		if (ab && ba && ac && ca) {
			return 11;
		} else if (!ab && ba && ac && ca) {
			return 06;
		} else if (ab && !ba && ac && ca) {
			return 05;
		} else if (ab && ba && !ac && ca) {
			return 06;
		} else if (ab && ba && ac && !ca) {
			return 05;
		} else if (!ab && ba && !ac && ca) {
			return 02;
		} else if (!ab && ba && ac && !ca) {
			return 03;
		} else if (ab && !ba && !ac && ca) {
			return 03;
		} else if (ab && !ba && ac && !ca) {
			return 01;
		}
		return -1;
	}

	protected int getType(boolean ab, boolean ba, boolean ac,
			boolean ca, boolean bc, boolean cb) {
		// 1
		if (!ab && !ac && ba && bc && !ca && !cb) {
			return 01;
		}
		if (!ab && !ac && !ba && !bc && ca && cb) {
			return 01;
		}

		// 2
		if (ab && !ac && !ba && !bc && !ca && cb) {
			return 02;
		}
		if (!ab && ac && !ba && bc && !ca && !cb) {
			return 02;
		}

		// 3
		if (ab && !ac && !ba && bc && !ca && !cb) {
			return 03;
		}
		if (!ab && ac && !ba && !bc && !ca && cb) {
			return 03;
		}
		if (!ab && !ac && !ba && bc && ca && !cb) {
			return 03;
		}
		if (!ab && !ac && ba && !bc && !ca && cb) {
			return 03;
		}

		// 4
		if (ab && ac && !ba && bc && !ca && !cb) {
			return 04;
		}
		if (ab && ac && !ba && !bc && !ca && cb) {
			return 04;
		}
		if (!ab && ac && ba && bc && !ca && !cb) {
			return 04;
		}
		if (!ab && !ac && ba && bc && ca && !cb) {
			return 04;
		}
		if (ab && !ac && !ba && !bc && ca && cb) {
			return 04;
		}
		if (!ab && !ac && ba && !bc && ca && cb) {
			return 04;
		}

		// 5
		if (ab && !ac && ba && bc && !ca && !cb) {
			return 05;
		}
		if (!ab && !ac && ba && bc && !ca && cb) {
			return 05;
		}
		if (!ab && ac && !ba && !bc && ca && cb) {
			return 05;
		}
		if (!ab && !ac && !ba && bc && ca && cb) {
			return 05;
		}

		// 6
		if (ab && !ac && ba && !bc && !ca && cb) {
			return 06;
		}
		if (!ab && ac && !ba && bc && ca && !cb) {
			return 06;
		}
		if (ab && !ac && !ba && bc && !ca && cb) {
			return 06;
		}
		if (!ab && ac && !ba && bc && !ca && cb) {
			return 06;
		}

		// 7
		if (ab && !ac && !ba && bc && ca && !cb) {
			return 07;
		}
		if (!ab && ac && ba && !bc && !ca && cb) {
			return 07;
		}

		// 8
		if (ab && ac && !ba && bc && !ca && cb) {
			return 8;
		}
		if (!ab && ac && ba && bc && ca && !cb) {
			return 8;
		}
		if (ab && !ac && ba && !bc && ca && cb) {
			return 8;
		}

		// 9
		if (!ab && !ac && ba && bc && ca && cb) {
			return 9;
		}
		if (ab && ac && !ba && !bc && ca && cb) {
			return 9;
		}
		if (ab && ac && ba && bc && !ca && !cb) {
			return 9;
		}

		// 10
		if (ab && !ac && !ba && bc && ca && cb) {
			return 10;
		}
		if (!ab && ac && ba && bc && !ca && cb) {
			return 10;
		}
		if (!ab && ac && ba && !bc && ca && cb) {
			return 10;
		}
		if (ab && ac && !ba && bc && ca && !cb) {
			return 10;
		}
		if (ab && !ac && ba && bc && ca && !cb) {
			return 10;
		}
		if (ab && ac && ba && !bc && !ca && cb) {
			return 10;
		}

		// 11
		if (!ab && ac && !ba && bc && ca && cb) {
			return 11;
		}
		if (ab && !ac && ba && bc && !ca && cb) {
			return 11;
		}

		int sum = (ab ? 1 : 0) + (ac ? 1 : 0) + (ba ? 1 : 0) + (bc ? 1 : 0)
				+ (ca ? 1 : 0) + (cb ? 1 : 0);

		// 12
		if (sum == 5) {
			return 12;
		}
		// 13
		if (sum == 6) {
			return 13;
		}

		// 1
		if (ab && ac && !ba && !bc && !ca && !cb) {
			return 01;
		}
		// 2
		if (!ab && !ac && ba && !bc && ca && !cb) {
			return 02;
		}
		// 3
		if (!ab && ac && ba && !bc && !ca && !cb) {
			return 03;
		}
		if (ab && !ac && !ba && !bc && ca && !cb) {
			return 03;
		}
		// 5
		if (ab && ac && ba && !bc && !ca && !cb) {
			return 05;
		}
		if (ab && ac && !ba && !bc && ca && !cb) {
			return 05;
		}
		// 6
		if (ab && !ac && ba && !bc && ca && !cb) {
			return 06;
		}
		if (!ab && ac && ba && !bc && ca && !cb) {
			return 06;
		}
		// 11
		if (ab && ac && ba && !bc && ca && !cb) {
			return 11;
		}

		return -1;

	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

}

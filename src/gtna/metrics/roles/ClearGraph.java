package gtna.metrics.roles;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.NodeImpl;

/**
 * @deprecated
 */
public class ClearGraph {
	private int deletedLoops;
	private int deletedParallelEdges;
	private int deletedDirectedParallelEdges;

	public ClearGraph() {

	}

	// delete all loops, parallel edges and antipodal edges
	public Graph clearGraph(Graph g) {
		g = graphDeleteLoops(g);
		g = graphDeleteParallelEdges(g);
		g = graphDeleteDirectedParallelEdges(g);
		return g;
	}

	// public Graph downsizeGraph(Graph g, int maxsize){
	// for(int i=0;i<g.nodes.length;i++){
	// for(int j=0;j<g.nodes[i].in.length;j++){
	// if(g.nodes[i].in[j].index>maxsize){
	// g.nodes[i].removeIn(g.nodes[i].in[j]);
	// }
	// }
	// for(int j=0;j<g.nodes[i].out.length;j++){
	// if(g.nodes[i].out[j].index>maxsize){
	// g.nodes[i].removeOut(g.nodes[i].out[j]);
	// }
	// }
	// }
	// Node[] nodes = new Node[maxsize];
	// for(int i=0; i<maxsize; i++){
	// nodes[i] = g.nodes[i];
	// }
	// Graph graph = new Graph("graph", nodes);
	// return graph;
	// }

	// delete all parallel edges
	public Graph graphDeleteDirectedParallelEdges(Graph g) {
		int counter = 0;
		for (int i = 0; i < g.nodes.length; i++) {
			NodeImpl node_i = g.nodes[i];
			int index = node_i.index();
			Node[] in = node_i.in();
			for (int j = 0; j < in.length; j++) {
				for (int k = j+1; k < in.length; k++) {
						if (in[j] == in[k]) {
							g.nodes[in[k].index()].removeOut(node_i);
							g.nodes[index]
									.removeIn((NodeImpl) in[k]);
							counter++;
						}
				}
			}
		}
		System.out
				.println("Directed Parralel Edges Deleted: " + counter + "\n");
		this.setDeletedDirectedParallelEdges(counter);
		return g;
	}

	// delete all loops
	public Graph graphDeleteLoops(Graph g) {
		int counter = 0;
		for (int i = 0; i < g.nodes.length; i++) {
			Node[] in = g.nodes[i].in();
			for (int j = 0; j < in.length; j++) {
				if (in[j].index() == g.nodes[i].index()) {
					g.nodes[i].removeIn(g.nodes[i]);
					g.nodes[i].removeOut(g.nodes[i]);
					counter++;
				}
			}
		}
		System.out.println("\n\nLoops Deleted: " + counter);
		this.setDeletedLoops(counter);
		return g;
	}

	// delete all antipodal edges
	public Graph graphDeleteParallelEdges(Graph g) {
		int counter = 0;
		for (int i = 0; i < g.nodes.length; i++) {
			Node[] in = g.nodes[i].in();
			Node[] out = g.nodes[i].out();
			for (int j = 0; j < in.length; j++) {
				for (int k = 0; k < out.length; k++) {
					if (in[j].index() == out[k].index()) {
						g.nodes[out[k].index()].removeIn(g.nodes[i]);
						g.nodes[g.nodes[i].index()]
								.removeOut((NodeImpl) out[k]);
						counter++;
					}
				}
			}
		}
		System.out.println("Parralel Edges Deleted: " + counter);
		this.setDeletedParallelEdges(counter);
		return g;
	}

	// getter and setter methods

	private void setDeletedLoops(int deletedLoops) {
		this.deletedLoops = deletedLoops;
	}

	public int getDeletedLoops() {
		return deletedLoops;
	}

	private void setDeletedParallelEdges(int deletedParallelEdges) {
		this.deletedParallelEdges = deletedParallelEdges;
	}

	public int getDeletedParallelEdges() {
		return deletedParallelEdges;
	}

	private void setDeletedDirectedParallelEdges(
			int deletedDirectedParallelEdges) {
		this.deletedDirectedParallelEdges = deletedDirectedParallelEdges;
	}

	public int getDeletedDirectedParallelEdges() {
		return deletedDirectedParallelEdges;
	}
}

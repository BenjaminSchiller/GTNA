package gtna.io.networks;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.util.Timer;

public class WOTReader {
	public static Graph read(String filename) throws Exception {
		Timer timer = new Timer();
		
		timer.end();

		NodeImpl[] nodes = {};
		Graph g = new Graph("NAME", nodes, timer);
		return g;
	}

	private static class WOTNode {
		public int index;

		public String name;

		public int key;

		public WOTNode[] signatures;

		public WOTNode(int index, String name) {
			this.index = index;
			this.name = name;
			this.key = -1;
			this.signatures = null;
		}

		public String toString() {
			return this.name + " [" + this.key + "]";
		}
	}
}

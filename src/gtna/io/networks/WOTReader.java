package gtna.io.networks;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.util.Timer;
import gtna.util.Util;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class WOTReader {
	public static Graph read(String filename) throws Exception {
		Timer timer = new Timer();
		System.out.println();
		String line = null;
		RandomAccessFile raf = new RandomAccessFile(filename, "r");
		while (!raf.readLine().startsWith("names/")) {
		}
		ArrayList<WOTNode> nodes = new ArrayList<WOTNode>();
		HashSet<String> nodesSet = new HashSet<String>();
		int index = 0;
		while (!(line = raf.readLine()).startsWith("keys/")) {
			nodes.add(new WOTNode(index++, line));
			nodesSet.add(line);
		}
		Hashtable<WOTNode, Integer> keysHT = new Hashtable<WOTNode, Integer>();
		Hashtable<Integer, WOTNode> nodesHT = new Hashtable<Integer, WOTNode>();
		HashSet<Integer> keySet = new HashSet<Integer>();
		for (int i = 0; i < nodes.size(); i++) {
			byte[] keyB = new byte[4];
			raf.read(keyB);
			int key = Util.toInt(keyB);
			nodes.get(i).key = key;
			if (keySet.contains(key)) {
				System.out.println(nodes.get(i) + " => " + nodesHT.get(key));
			}
			keysHT.put(nodes.get(i), key);
			nodesHT.put(key, nodes.get(i));
			keySet.add(key);
		}
		System.out.println("nodes: " + nodes.size());
		System.out.println("names: " + nodesSet.size());
		System.out.println("keys1: " + keysHT.size());
		System.out.println("keys2: " + keySet.size());
		System.out.println("  " + raf.readLine());
		for (int i = 0; i < nodes.size(); i++) {
			byte[] numberB = new byte[4];
			raf.read(numberB);
			int number = Util.toInt(numberB);
			WOTNode node = nodes.get(i);
			node.signatures = new WOTNode[number];
			for (int j = 0; j < number; j++) {
				byte[] sb = new byte[4];
				raf.read(sb);
				// byte type = sb[0];
				byte[] indexB = new byte[] { 0, sb[1], sb[2], sb[3] };
				int sig = Util.toInt(indexB);
				node.signatures[j] = nodes.get(sig);
			}
		}
		System.out.println("  " + raf.readLine());

		NodeImpl[] n = NodeImpl.init(nodes.size());
		Edges edges = new Edges(n, n.length);
		for (int i = 0; i < nodes.size(); i++) {
			WOTNode node = nodes.get(i);
			for (int j = 0; j < node.signatures.length; j++) {
				int from = i;
				int to = node.signatures[j].index;
				edges.add(n[from], n[to]);
			}
		}
		edges.fill();

		raf.close();
		timer.end();
		Graph g = new Graph("NAME", n, timer);
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

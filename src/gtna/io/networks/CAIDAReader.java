package gtna.io.networks;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.io.Filereader;
import gtna.util.Timer;

import java.util.Hashtable;

public class CAIDAReader extends Filereader {
	private static final String caidaSeparator = "	";

	public CAIDAReader(String filename) {
		super(filename);
	}
	
	public static Graph read(String filename){
		Timer timer = new Timer();
		
		Hashtable<String, Integer> ids = new Hashtable<String, Integer>();
		int index = 0;
		int edgeCounter = 0;
		CAIDAReader reader1 = new CAIDAReader(filename);
		String line1 = null;
		while ((line1 = reader1.readLine()) != null) {
			String[] parts = line1.split(caidaSeparator);
			if ("D".equals(parts[0])) {
				String from = parts[1];
				String to = parts[2];
				if (!ids.containsKey(from)) {
					ids.put(from, index++);
				}
				if (!ids.containsKey(to)) {
					ids.put(to, index++);
				}
			}
			edgeCounter++;
		}
		reader1.close();

		NodeImpl[] nodes = NodeImpl.init(index);
		Edges edges = new Edges(nodes, edgeCounter);
		CAIDAReader reader = new CAIDAReader(filename);
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(caidaSeparator);
			if ("D".equals(parts[0])) {
				int fromID = ids.get(parts[1]);
				int toID = ids.get(parts[2]);
				edges.add(nodes[fromID], nodes[toID]);
			}
		}
		reader.close();
		edges.fill();

		timer.end();
		Graph g = new Graph("CAIDA", nodes, timer);
		return g;
	}
}

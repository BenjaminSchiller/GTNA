package gtna.io.networks;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.io.Filereader;
import gtna.util.Timer;

import java.util.Hashtable;

public class SPIReader extends Filereader {
	public static final String buddySeparator = ";";
	
	public static final String buddyFileStart = "\"user_active_rid\"";
	
	private SPIReader(String filename) {
		super(filename);
	}

	public static Graph read(String filename) {
		Timer timer = new Timer();

		Hashtable<String, Integer> ids = new Hashtable<String, Integer>();
		int index = 0;
		int edgeCounter = 0;
		SPIReader reader1 = new SPIReader(filename);
		String line1 = null;
		while ((line1 = reader1.readLine()) != null) {
			if (line1.startsWith(buddyFileStart)) {
				continue;
			}
			String[] parts = line1.split(buddySeparator);
			String from = parts[0].replace("\"", "");
			String to = parts[1].replace("\"", "");
			if (!ids.containsKey(from)) {
				ids.put(from, index++);
			}
			if (!ids.containsKey(to)) {
				ids.put(to, index++);
			}
			edgeCounter++;
		}
		reader1.close();

		NodeImpl[] nodes = NodeImpl.init(index);
		Edges edges = new Edges(nodes, edgeCounter);
		SPIReader reader = new SPIReader(filename);
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith(buddyFileStart)) {
				continue;
			}
			String[] parts = line.split(buddySeparator);
			String from = parts[0].replace("\"", "");
			String to = parts[1].replace("\"", "");
			int fromID = ids.get(from);
			int toID = ids.get(to);
			edges.add(nodes[fromID], nodes[toID]);
		}
		reader.close();
		edges.fill();

		timer.end();
		Graph g = new Graph("NAME", nodes, timer);
		return g;
	}
}

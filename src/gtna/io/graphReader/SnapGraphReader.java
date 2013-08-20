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
 * SnapGraphReader.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.io.graphReader;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

=======
>>>>>>> SNAP standard separator (tab)
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Filereader;
import gtna.util.Config;

/**
 * @author Tim
 *
 */
public class SnapGraphReader extends GraphReader {

    /**
     * @param key
     */
    public SnapGraphReader() {
	super("SNAP_standard");
    }

    /* (non-Javadoc)
     * @see gtna.io.graphReader.GraphReader#read(java.lang.String)
     */
    @Override
    public Graph read(String filename) {
<<<<<<< HEAD
	String sep1 = Config.get("SNAP_SEPARATOR_1");
	String ffd = Config.get("FILESYSTEM_FOLDER_DELIMITER");
	Filereader fr = new Filereader(filename);
	try {
		String line = null;
		String name = filename.substring(filename.lastIndexOf(ffd)+1);
//		int V = Integer.parseInt(fr.readLine());
//		int E = Integer.parseInt(fr.readLine());
		Graph graph = new Graph(name);
		
		Map<Integer, List<Integer>> outgoing = new HashMap<Integer, List<Integer>>();
		Map<Integer, List<Integer>> incoming = new HashMap<Integer, List<Integer>>();
		
		while ((line = fr.readLine()) != null) {
		    	line.trim();
=======
	String sep1 = Config.get("GRAPH_WRITER_SEPARATOR_1");
	String sep2 = Config.get("GRAPH_WRITER_SEPARATOR_2");
	Filereader fr = new Filereader(filename);
	try {
		String line = null;
		String name = fr.readLine();
		int V = Integer.parseInt(fr.readLine());
		int E = Integer.parseInt(fr.readLine());
		Graph graph = new Graph(name);
		Node[] nodes = Node.init(V, graph);
		Edges edges = new Edges(nodes, E);
		while ((line = fr.readLine()) != null) {
>>>>>>> SNAP standard separator (tab)
			String[] temp = line.split(sep1);
			if (temp.length < 2 || temp[1].length() == 0) {
				continue;
			}
			int src = Integer.parseInt(temp[0]);
<<<<<<< HEAD
			int dst = Integer.parseInt(temp[1]);
			
			if(outgoing.containsKey(src)){
				List<Integer> oe = outgoing.get(src);
				oe.add(dst);
				outgoing.put(src, oe);
			} else {
				List<Integer> oe = new ArrayList<Integer>();
				oe.add(dst);
				outgoing.put(src, oe);
			}
			if(incoming.containsKey(dst)){
				List<Integer> ie = incoming.get(dst);
				ie.add(src);
				incoming.put(src, ie);
			} else {
				List<Integer> ie = new ArrayList<Integer>();
				ie.add(src);
				incoming.put(src, ie);
			}
		}
		
		Map<Integer, Integer> idMapping = new HashMap<Integer, Integer>();
		idMapping = calculateIdMapping(outgoing, incoming);
		outgoing = mapIds(idMapping, outgoing);
		incoming = mapIds(idMapping, incoming);
		Node[] nodes = Node.init(idMapping.size(), graph);
		Edges edges = new Edges(nodes, outgoing.size());
		addEdges(edges, outgoing);
=======
			String[] temp2 = temp[1].split(sep2);
			for (String dst : temp2) {
				edges.add(src, Integer.parseInt(dst));
			}
		}
>>>>>>> SNAP standard separator (tab)
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	} catch (NumberFormatException nf) {
		return null;
	} catch (Exception e) {
		return null;
	} finally {
		fr.close();
	}
    }

<<<<<<< HEAD
    /**
	 * @param edges
	 * @param outgoing
	 */
	private void addEdges(Edges edges, Map<Integer, List<Integer>> outgoing) {
		for(Entry<Integer, List<Integer>> e : outgoing.entrySet()){
			int src = e.getKey();
			for(Integer dst : e.getValue()){
				edges.add(src, dst);
			}
		}
		
	}

	/**
	 * @param idMapping
	 * @param incoming
	 * @return
	 */
	private Map<Integer, List<Integer>> mapIds(Map<Integer, Integer> idMapping,
			Map<Integer, List<Integer>> toMap) {
		Map<Integer, List<Integer>> mapped = new HashMap<Integer, List<Integer>>();
		
		for(Entry<Integer, List<Integer>> e : toMap.entrySet()){
			List<Integer> ml = new ArrayList<Integer>();
			for(Integer li : e.getValue()){
				ml.add(idMapping.get(li));
			}
			int nk = idMapping.get(e.getKey());
			
			mapped.put(nk, ml);
		}
		
		return mapped;
	}

	/**
	 * @param outgoing
	 * @param incoming
	 * @return
	 */
	private Map<Integer, Integer> calculateIdMapping(
			Map<Integer, List<Integer>> outgoing,
			Map<Integer, List<Integer>> incoming) {
		Map<Integer, Integer> ids = new HashMap<Integer, Integer>();
		
		for(Entry<Integer, List<Integer>> e : outgoing.entrySet()){
			if(!ids.containsKey(e.getKey())){
				ids.put(e.getKey(), ids.size());
			}
			for(Integer i : e.getValue()){
				if(!ids.containsKey(i)){
					ids.put(i, ids.size());
				}
			}
		}
		// should never set any condition true as all nodes in the incoming set should be added in the outgoing destination list
		for(Entry<Integer, List<Integer>> e : incoming.entrySet()){
			if(!ids.containsKey(e.getKey())){
				ids.put(e.getKey(), ids.size());
			}
			for(Integer i : e.getValue()){
				if(!ids.containsKey(i)){
					ids.put(i, ids.size());
				}
			}
		}
		
		return ids;
	}

	/* (non-Javadoc)
=======
    /* (non-Javadoc)
>>>>>>> SNAP standard separator (tab)
     * @see gtna.io.graphReader.GraphReader#nodes(java.lang.String)
     */
    @Override
    public int nodes(String filename) {
	// TODO Auto-generated method stub
	return 0;
    }

}

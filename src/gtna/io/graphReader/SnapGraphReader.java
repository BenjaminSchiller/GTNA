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

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Filewriter;
import gtna.util.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author Tim
 * 
 */
public class SnapGraphReader extends GraphReader {

	public enum graphtype {
		DIRECTED, UNDIRECTED
	}

	/**
	 * @param key
	 */
	public SnapGraphReader() {
		super("SNAP_standard");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.io.graphReader.GraphReader#read(java.lang.String)
	 */
	@Override
	public Graph read(String filename) {
		String sep1 = Config.get("SNAP_SEPARATOR_1");
		String ffd = Config.get("FILESYSTEM_FOLDER_DELIMITER");
		SNAPFileReader fr = new SNAPFileReader(filename);
		try {
			String line = null;
			String name = filename.substring(filename.lastIndexOf(ffd) + 1);

			Graph graph = new Graph(name);

			Map<Integer, Integer> idMapping = new HashMap<Integer, Integer>();
			int nodecounter = 0;
			
			LinkedList<String> edges = new LinkedList<String>();
			
			graphtype type=null;
			
			line = fr.readLine(); 
			type = (line.contains("Directed graph")) ? graphtype.DIRECTED
						: graphtype.UNDIRECTED;
			
			while(!line.contains("FromNodeId") && !line.contains("ToNodeId")){
				line = fr.readLine();
			}
			nodecounter = parseEdges(sep1, fr, idMapping, nodecounter, edges,
					type);

			Node[] nodes = Node.init(idMapping.size(), graph);
			Edges e = new Edges(nodes, edges.size());
			ListIterator<String> edgeIterator = edges.listIterator();
			while(edgeIterator.hasNext()){
				String[] pair = edgeIterator.next().split("-");
				e.add(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
			}
				
			e.fill();
			graph.setNodes(nodes);
			return graph;
		} catch (Exception e) {
			return null;
		} finally {
			fr.close();
		}
	}

	/**
	 * @param sep1
	 * @param fr
	 * @param idMapping
	 * @param nodecounter
	 * @param edges
	 * @param type
	 * @return
	 */
	private int parseEdges(String sep1, SNAPFileReader fr,
			Map<Integer, Integer> idMapping, int nodecounter,
			LinkedList<String> edges, graphtype type) {
		String line;
		while ((line = fr.readLine()) != null) {
			line.trim();

			
		
			String[] temp = line.split(sep1);
			if (temp.length < 2 || temp[1].length() == 0) {
				continue;
			}
			int src = Integer.parseInt(temp[0]);
			int dst = Integer.parseInt(temp[1]);

			int gtnaSrc;
			int gtnaDst;
			
			if(idMapping.containsKey(src)){
				gtnaSrc = idMapping.get(src);
			} else {
				idMapping.put(src, nodecounter);
				gtnaSrc = nodecounter;
				nodecounter++;
			}
			
			if(idMapping.containsKey(dst)){
				gtnaDst = idMapping.get(dst);
			} else {
				idMapping.put(dst, nodecounter);
				gtnaDst = nodecounter;
				nodecounter++;
			}
			
			
			// directed
			edges.add(gtnaSrc + "-" + gtnaDst);
			if (type == graphtype.UNDIRECTED)
				edges.add(gtnaDst + "-" + gtnaSrc);
		}
		return nodecounter;
	}

	
	/**
	 * Returns number of nodes in the graph, expects a line:
	 * # Nodes: nodecount Edges: edgecount
	 */
	@Override
	public int nodes(String filename) {
		String sep1 = Config.get("SNAP_SEPARATOR_1");
		SNAPFileReader fr = new SNAPFileReader(filename);
		
		String line = fr.readLine();
		while(!line.contains("Nodes:")){
			line = fr.readLine();
		}
		

		String[] sl = line.split(sep1);		
		return Integer.parseInt(sl[1]);
	}
	
	
	
	
	private class SNAPFileReader{
		private String filename;

		private java.io.BufferedReader br;

		public SNAPFileReader(String filename) {
			this.filename = filename;
			try {
				this.br = new java.io.BufferedReader(new java.io.FileReader(
						this.filename));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public String readLine() {
			try {
				String line = this.br.readLine();
				if (line != null) {
					line = line.trim();
				}
				while (line != null
						&&  line.length() == 0) {
					line = this.br.readLine();
					if (line != null) {
						line = line.trim();
					}
					if(line != null && line.startsWith(Filewriter.COMMENT)){
						line = line.replace(Filewriter.COMMENT + " ", "");
					}
				}
				
				
				return line;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		public boolean close() {
			try {
				this.br.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

}



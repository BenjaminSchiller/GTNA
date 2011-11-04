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
 * GooglePlusReader.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.io.networks.googlePlus;

import gtna.graph.Edges;
import gtna.graph.Graph;

import java.io.File;

/**
 * @author benni
 * 
 */
public class GooglePlusReader {

	public static Graph generateGraph(Crawl crawl, Mapping mapping) {
		System.out.println("GENERATING GRAPH FOR " + crawl.toString());
		Graph graph = new Graph("G+ crawl " + mapping.getCid());
		gtna.graph.Node[] nodes = gtna.graph.Node.init(mapping.getMap().size(),
				graph);
		Edges edges = new Edges(nodes, 0);
		int counter = 0;
		for (File node : crawl.getNodeList()) {
			counter++;
			if ((counter % 1000) == 0) {
				System.out.println("  " + crawl.getCid() + " : " + counter
						+ " of " + crawl.getNodeList().size());
			}
			String[] temp = node.getName().split("-");
			int tid = Integer.parseInt(temp[0]);
			int tlid = Integer.parseInt((new File(node.getParent()).getName()));
			String u_id = temp[1];
			if (!mapping.getMap().containsKey(u_id)) {
				continue;
			}
			
			Task task = new Task(tid, mapping.getCid(), tlid, u_id, 0, 0);
			Node n = null;
			try {
				n = Node.read(node.getAbsolutePath(), task);
			} catch (Exception e) {
				continue;
			}
			if (n.getTask().getU_id().length() != 21) {
				continue;
			}
			int index = mapping.getMap().get(n.getTask().getU_id());
			for (User out : n.getOut()) {
				if (out.getId().length() == 21
						&& mapping.getMap().containsKey(out.getId())) {
					int outIndex = mapping.getMap().get(out.getId());
					edges.add(index, outIndex);
				}
			}
			for (User in : n.getIn()) {
				if (in.getId().length() == 21
						&& mapping.getMap().containsKey(in.getId())) {
					int inIndex = mapping.getMap().get(in.getId());
					edges.add(inIndex, index);
				}
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}

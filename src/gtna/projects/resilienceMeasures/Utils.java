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
 * Utils.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: truong;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.resilienceMeasures;

import java.io.File;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import gtna.graph.Graph;

/**
 * @author truong
 * 
 */
public class Utils {
	public Graph importGraphFromFile(String s) {
		Graph gtna = new Graph(s);

		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// import file
		ImportController importController = Lookup.getDefault().lookup(
				ImportController.class);
		Container container;
		try {
			File file = new File(getClass().getResource(s).toURI());
			container = importController.importFile(file);
			container.getLoader().setEdgeDefault(EdgeDefault.UNDIRECTED); // force
																			// UNDIRECTED
			container.setAllowAutoNode(true); // create missing nodes
		} catch (Exception e) {
			System.out.println("Cannot import file: " + s);
			return null;
		}

		// append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		// get a graph model
		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();

		// append as a undirected graph
		UndirectedGraph gephi = graphModel.getUndirectedGraph();

		gtna.graph.Node[] nodes = new gtna.graph.Node[gephi.getNodeCount()];
		// import nodes
		for (Node n : gephi.getNodes()) {
			gtna.graph.Node node = new gtna.graph.Node(n.getId(), gtna);
			nodes[n.getId() - 1] = node;
		}

		// import edges
		int importedEdges = 0;
		for (Edge e : gephi.getEdges()) {
			// gtna.graph.Edge edge = new
			// gtna.graph.Edge(nodes[e.getSource().getId() - 1],
			// nodes[e.getTarget().getId() - 1]);
			importedEdges++;
			System.out.println("--> import edge :" + importedEdges
					+ " gephi:  " + e.toString());
			int src = e.getSource().getId() - 1;
			int dst = e.getTarget().getId() - 1;
			System.out.println("src = " + src + "  dst = " + dst);
			nodes[src].addOut(dst);
			nodes[dst].addIn(src);
		}

		gtna.setNodes(nodes);
		gtna.generateEdges();

		System.out.println("Imported: " + gtna.getNodes().length + " nodes + "
				+ gtna.getEdges().size() + " edges.");
		for (gtna.graph.Node n : gtna.getNodes()) {
			n.generateAllEdges();
			System.out
					.println(""
							+ n.toString()
							+ " -- gephi: "
							+ gephi.getNeighbors(gephi.getNode(n.getIndex()))
									.toArray().length);
		}

		return gtna;
	}
}

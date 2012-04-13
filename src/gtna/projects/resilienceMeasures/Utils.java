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

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
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
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;
import org.w3c.css.sac.AttributeCondition;

import gtna.graph.Edges;
import gtna.graph.Graph;

/**
 * @author truong
 * 
 */
public class Utils {
	private UndirectedGraph gephi;

	public Graph importGraphFromFile(String s) {
		Graph gtnaGraph = new Graph(s);

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
		gephi = graphModel.getUndirectedGraph();

		gtna.graph.Node[] nodes = gtna.graph.Node.init(gephi.getNodeCount(),
				gtnaGraph);
		// import nodes
		/*
		 * for (Node n : gephi.getNodes()) { gtna.graph.Node node = new
		 * gtna.graph.Node(n.getId(), gtnaGraph); nodes[n.getId() - 1] = node;
		 * System.out.println("Node " + n.getId() + " = " +
		 * n.getNodeData().getLabel()); }
		 */

		// import edges
		int importedEdges = 0;
		Edges edges = new Edges(nodes, gephi.getEdgeCount() * 2);
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
			edges.add(src, dst);
			edges.add(dst, src);
		}
		edges.fill();

		gtnaGraph.setNodes(nodes);
		// gtnaGraph.generateEdges();

		System.out.println("Imported: " + gtnaGraph.getNodes().length
				+ " nodes + " + gtnaGraph.getEdges().size() + " edges.");

		/*
		 * for (gtna.graph.Node n : gtnaGraph.getNodes()) {
		 * n.generateAllEdges(); }
		 */

		return gtnaGraph;
	}

	public void printCentrality(String centralityString) {
		// get graph model and attribute model of current workspace
		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();
		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();

		// get centrality
		GraphDistance distance = new GraphDistance();
		distance.setDirected(false);
		distance.execute(graphModel, attributeModel);

		// get centrality column created
		AttributeColumn col = attributeModel.getNodeTable().getColumn(
				centralityString);

		// iterate over values
		System.out.println("Gephi centrality:");
		for (Node n : gephi.getNodes()) {
			Double c = (Double) n.getNodeData().getAttributes()
					.getValue(col.getId());
			System.out.println("" + n.getId() + " = " + c);
		}
	}
}

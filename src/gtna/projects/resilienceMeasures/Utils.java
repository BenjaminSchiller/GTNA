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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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
import gtna.graph.Edges;
import gtna.graph.Graph;

/**
 * @author truong
 * 
 */
public class Utils {
	private UndirectedGraph gephi;

	public static void exportToGML(Graph g, String fileName) throws IOException {
		FileWriter outFile = new FileWriter(fileName + ".gml");
		PrintWriter out = new PrintWriter(outFile);

		out.println("graph [");

		for (gtna.graph.Node n : g.getNodes()) {
			// write node
			out.print("node ");
			out.println("[");
			out.println("id " + n.getIndex());
			out.println("]");

		}
		for (gtna.graph.Edge e : g.getEdges().getEdges()) {
			// write edge
			int src = e.getSrc();
			int dst = e.getDst();
			if (src < dst) {
				out.print("edge ");
				out.println("[");
				out.println("source " + src);
				out.println("target " + dst);
				out.println("]");
			}
		}

		out.println("]");

		out.close();
		outFile.close();
	}

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

		// import edges
		Edges edges = new Edges(nodes, gephi.getEdgeCount() * 2);
		for (Edge e : gephi.getEdges()) {

			int src = e.getSource().getId() - 1;
			int dst = e.getTarget().getId() - 1;

			edges.add(src, dst);
			edges.add(dst, src);
		}
		edges.fill();

		gtnaGraph.setNodes(nodes);

		System.out.println("Imported: " + gtnaGraph.getNodes().length
				+ " nodes + " + gtnaGraph.getEdges().size() + " edges.");

		return gtnaGraph;
	}

	public HashMap<Integer, Double> gephiCentrality(String centralityString) {
		HashMap<Integer, Double> result = new HashMap<Integer, Double>();
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
		for (Node n : gephi.getNodes()) {
			Double c = (Double) n.getNodeData().getAttributes()
					.getValue(col.getId());
			result.put(n.getId() - 1, c);
		}
		return result;
	}
}

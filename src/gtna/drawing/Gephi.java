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
 * Gephi.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.drawing;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.spanningTree.SpanningTree;
import gtna.id.IdentifierSpace;
import gtna.id.Partition;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDPartitionSimple;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlanePartitionSimple;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingPartition;
import gtna.id.ring.RingPartitionSimple;
import gtna.util.Config;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.layout.plugin.force.ForceVector;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.ProjectController;
import org.openide.util.Lookup;

/**
 * @author Nico
 * 
 */
public class Gephi {
	private GraphModel graphModel;
	private org.gephi.graph.api.Graph gephiGraph;
	private org.gephi.graph.api.Node[] gephiNodes;
	private Boolean useSpanningTreeOnNextPlot = false;
	private GephiDecorator[] decorators;

	private float ringRadius;

	public void plot(Graph g, IdentifierSpace idSpace, String fileName) {
		plot(g, idSpace, null, fileName);
	}

	public void plot(Graph g, IdentifierSpace idSpace, NodeColors colors,
			String fileName) {
		plot(g, null, idSpace, colors, fileName);
	}

	public void plot(Graph g, GephiDecorator[] decorators,
			IdentifierSpace idSpace, String fileName) {
		plot(g, decorators, idSpace, null, fileName);
	}

	public void plot(Graph g, GephiDecorator[] decorators,
			IdentifierSpace idSpace, NodeColors colors, String fileName) {
		ringRadius = Config.getInt("GEPHI_RING_RADIUS");
		boolean curvedFlag = Config.getBoolean("GEPHI_DRAW_CURVED_EDGES");
		float edgeScale = Config.getFloat("GEPHI_EDGE_SCALE");
		float nodeBorderWidth = Config.getFloat("GEPHI_NODE_BORDER_WIDTH");

		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();

		graphModel = Lookup.getDefault().lookup(GraphController.class)
				.getModel();

		// A generic graph might be okay, as we do not really care about
		// directions
		gephiGraph = graphModel.getGraph();

		// Next lines: do *never* draw curved lines!
		PreviewModel model = Lookup.getDefault()
				.lookup(PreviewController.class).getModel();
		PreviewProperties props = model.getProperties();
		props.putValue(PreviewProperty.EDGE_CURVED, curvedFlag);
		props.putValue(PreviewProperty.EDGE_THICKNESS, edgeScale);
		props.putValue(PreviewProperty.ARROW_SIZE, 0);
		props.putValue(PreviewProperty.NODE_BORDER_WIDTH, nodeBorderWidth);
		// props.putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);

		if (decorators == null) {
			decorators = new GephiDecorator[0];
		}
		this.decorators = decorators;
		for (GephiDecorator sD : decorators) {
			sD.init(g);
		}
		gephiNodes = new org.gephi.graph.api.Node[g.getNodes().length];
		this.plotGraph(g, idSpace, colors);

		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {
			ec.exportFile(new File(fileName));
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}

	public void useSpanningTreeOnNextPlot() {
		this.useSpanningTreeOnNextPlot = true;
	}

	private void plotGraph(Graph g, IdentifierSpace idSpace, NodeColors colors) {
		Partition[] p = idSpace.getPartitions();
		boolean showNode;

		if (g.hasProperty("NODE_COLORS_0") && colors == null) {
			colors = (NodeColors) g.getProperty("NODE_COLORS_0");
		}

		// First run: add all nodes
		for (Node n : g.getNodes()) {
			if (n == null) {
				continue;
			}
			ForceVector position = getPosition(p[n.getIndex()]);
			if (Double.isNaN(position.getNorm())) {
				throw new RuntimeException(
						"Cannot plot graph as it contains nodes with non-existing coordinates");
			}
			showNode = true;
			for (GephiDecorator sD : decorators) {
				showNode = showNode && sD.showNode(n);
			}
			if (!showNode)
				continue;
			org.gephi.graph.api.Node temp = addNode(graphModel, gephiGraph, "N"
					+ n.getIndex(), "Node " + n.getIndex(), position);
			for (GephiDecorator sD : decorators) {
				sD.decorateNode(temp, n);
			}
			if (colors != null) {
				Color c = colors.getColors()[n.getIndex()];
				float c1 = (float) c.getRed() / (float) 255;
				float c2 = (float) c.getGreen() / (float) 255;
				float c3 = (float) c.getBlue() / (float) 255;
				temp.getNodeData().setColor(c1, c2, c3);
			}
			temp.getNodeData().setSize(Config.getFloat("GEPHI_NODE_SIZE"));
			gephiNodes[n.getIndex()] = temp;
		}

		// Second run: add the edges
		if (useSpanningTreeOnNextPlot) {
			useSpanningTreeOnNextPlot = false;
			addSpanningTreeEdges(g);
		} else {
			addAllEdges(g);
		}
	}

	private void addAllEdges(Graph g) {
		Edge temp;
		boolean showEdge;

		for (Node n : g.getNodes()) {
			if (n == null) {
				continue;
			}

			for (int dest : n.getOutgoingEdges()) {
				showEdge = true;
				for (GephiDecorator sD : decorators) {
					showEdge = showEdge && sD.showEdge(n.getIndex(), dest);
				}
				if (!showEdge)
					continue;
				temp = addEdge(graphModel, gephiGraph,
						gephiNodes[n.getIndex()], gephiNodes[dest]);
				for (GephiDecorator sD : decorators) {
					sD.decorateEdge(temp, n.getIndex(), dest);
				}
			}
			for (int src : n.getIncomingEdges()) {
				showEdge = true;
				for (GephiDecorator sD : decorators) {
					showEdge = showEdge && sD.showEdge(src, n.getIndex());
				}
				if (!showEdge)
					continue;
				temp = addEdge(graphModel, gephiGraph, gephiNodes[src],
						gephiNodes[n.getIndex()]);
				for (GephiDecorator sD : decorators) {
					sD.decorateEdge(temp, src, n.getIndex());
				}
			}
		}
	}

	private void addSpanningTreeEdges(Graph g) {
		Edge temp;
		boolean showEdge;

		if (!g.hasProperty("SPANNINGTREE")) {
			throw new RuntimeException(
					"Should plot a spanning tree, but given graph misses property");
		}
		SpanningTree tree = (SpanningTree) g.getProperty("SPANNINGTREE");
		gtna.graph.Edge[] edges = tree.generateEdgesUnidirectional();
		for (gtna.graph.Edge e : edges) {
			if (e == null)
				continue;
			if (e.getSrc() == -1)
				continue;
			showEdge = true;
			for (GephiDecorator sD : decorators) {
				showEdge = showEdge && sD.showEdge(e.getSrc(), e.getDst());
			}
			if (!showEdge)
				continue;
			temp = addEdge(graphModel, gephiGraph, gephiNodes[e.getSrc()],
					gephiNodes[e.getDst()]);
			for (GephiDecorator sD : decorators) {
				sD.decorateEdge(temp, e.getSrc(), e.getDst());
			}
		}
	}

	private ForceVector getPosition(Partition p) {
		if (p instanceof PlanePartitionSimple) {
			PlaneIdentifier temp = (PlaneIdentifier) p
					.getRepresentativeIdentifier();
			return new ForceVector((float) temp.getX(), (float) temp.getY());
		} else if (p instanceof RingPartition) {
			// get the modulus for the ring
			double modulus = 1.0;

			double positionOnRing = ((RingIdentifier) ((RingPartition) p)
					.getRepresentativeIdentifier()).getPosition();
			double angle = (positionOnRing / modulus) * 360;

			ForceVector pos = new ForceVector((float) Math.sin(Math
					.toRadians(angle)) * ringRadius, (float) Math.cos(Math
					.toRadians(angle)) * ringRadius);
			return pos;
		} else if (p instanceof RingPartitionSimple) {
			RingPartitionSimple temp = (RingPartitionSimple) p;

			double modulus = 1.0;

			double posisitonOnRing = temp.getIdentifier().getPosition();
			double angle = (posisitonOnRing / modulus) * 360;

			ForceVector pos = new ForceVector((float) Math.sin(Math
					.toRadians(angle)) * ringRadius, (float) Math.cos(Math
					.toRadians(angle)) * ringRadius);

			return pos;
		} else if (p instanceof MDPartitionSimple) {
			MDIdentifier temp = (MDIdentifier) p.getRepresentativeIdentifier();
			if (temp.getCoordinates().length == 2) {
				return new ForceVector((float) temp.getCoordinates()[0],
						(float) temp.getCoordinates()[1]);
			} else
				throw new RuntimeException(
						"Cannot yet calculate a responsing coordinate for "
								+ temp.toString());
		} else
			throw new RuntimeException("Cannot calculate a position in "
					+ p.getClass());
	}

	private org.gephi.graph.api.Node addNode(GraphModel graphModel,
			org.gephi.graph.api.Graph graph, String name, String label,
			ForceVector position) {
		org.gephi.graph.api.Node temp = graphModel.factory().newNode(name);
		temp.getNodeData().setLabel(label);
		// Important: we need *both* setLayoutData and setX / setY to position a
		// node!
		temp.getNodeData().setLayoutData(position);
		temp.getNodeData().setX(position.x());
		temp.getNodeData().setY(position.y());
		graph.addNode(temp);
		return temp;
	}

	private org.gephi.graph.api.Edge addEdge(GraphModel graphModel,
			org.gephi.graph.api.Graph graph, org.gephi.graph.api.Node start,
			org.gephi.graph.api.Node end) {
		Edge temp = graphModel.factory().newEdge(start, end);
		graph.addEdge(temp);
		return temp;
	}
}
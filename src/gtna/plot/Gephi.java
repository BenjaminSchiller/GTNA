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
package gtna.plot;

import java.io.File;
import java.io.IOException;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.IdentifierSpace;
import gtna.id.Partition;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.id.ring.RingIdentifierSpace;
import gtna.id.ring.RingPartition;
import gtna.util.Config;

import org.gephi.graph.api.*;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.layout.plugin.force.ForceVector;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
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
	
	public Gephi() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		
		graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		
			// A generic graph might be okay, as we do not really care about directions
		gephiGraph = graphModel.getGraph();
		
			// Next three lines: do *never* draw curved lines!
        PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
        model.getUndirectedEdgeSupervisor().setCurvedFlag(false);
        model.getBiEdgeSupervisor().setCurvedFlag(false);		
	}
	
	public void Plot( Graph g, String fileName ) {
		gephiNodes = new org.gephi.graph.api.Node[g.getNodes().length];
		this.plotGraph(g);
      
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		try {
		   ec.exportFile(new File( Config.get("MAIN_PLOT_FOLDER") + fileName ));
		} catch (IOException ex) {
		   ex.printStackTrace();
		   return;
		}		
	}
	
	private void plotGraph(Graph g) {
		IdentifierSpace idSpace = (IdentifierSpace) g.getProperty("ID_SPACE_0");
		Partition[] p = idSpace.getPartitions();
		
		// First run: add all nodes
		for ( Node n: g.getNodes() ) {
			ForceVector position = getPosition( p[n.getIndex()]);
			org.gephi.graph.api.Node temp = addNode ( graphModel, gephiGraph, "N" + n.getIndex(), "Node " + n.getIndex(), position);
			gephiNodes[n.getIndex()] = temp;
		}
		
			// Second run: add the edges - only use outgoing edges to avoid chaos
		for ( Node n: g.getNodes() ) {
			for (int dest: n.getOutgoingEdges()) {
				addEdge(graphModel, gephiGraph, gephiNodes[n.getIndex()], gephiNodes[dest]);
			}
				
		}
	}
	
	private ForceVector getPosition ( Partition p ) {
		if ( p instanceof PlanePartitionSimple ) {
				PlaneIdentifier temp = (PlaneIdentifier) p.getRepresentativeID();
				return new ForceVector((float)temp.getX(), (float)temp.getY());
		} else throw new RuntimeException("Cannot calculate a position in " + p.getClass());
	}

	private org.gephi.graph.api.Node addNode ( GraphModel graphModel, org.gephi.graph.api.Graph graph, String name, String label, ForceVector position ) {
		org.gephi.graph.api.Node temp = graphModel.factory().newNode( name );
		temp.getNodeData().setLabel(label);
			// Important: we need *both* setLayoutData and setX / setY to position a node!
		temp.getNodeData().setLayoutData(position);
		temp.getNodeData().setX(position.x());
		temp.getNodeData().setY(position.y());
		graph.addNode(temp);
		return temp;
	}
	
	private org.gephi.graph.api.Edge addEdge ( GraphModel graphModel, org.gephi.graph.api.Graph graph, org.gephi.graph.api.Node start, org.gephi.graph.api.Node end ) {
		Edge temp = graphModel.factory().newEdge( start, end );
		graph.addEdge(temp);
		return temp;
	}	
}

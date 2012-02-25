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
 * Roles.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors: florian;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.communities;

import gtna.communities.Communities;
import gtna.communities.Community;
import gtna.communities.Roles2;
import gtna.communities.Roles2.Role2;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.HashSet;

public class Roles2Generation extends TransformationImpl implements
		Transformation {

	private boolean withHubs;

	/**
	 * @param withHubs
	 *            if true hubs are detected based on the nodes relative within
	 *            module degree, else all nodes are declared non-hubs
	 */
	public Roles2Generation(boolean withHubs) {
		super("ROLES2_GENERATION", new String[] { "WITH_HUBS" },
				new String[] { "" + withHubs });
		this.withHubs = withHubs;
	}

	@Override
	public Graph transform(Graph g) {
		GraphProperty[] properties = g.getProperties("COMMUNITIES");
		for (GraphProperty gp : properties) {
			Communities communities = (Communities) gp;
			Role2[] roleOfNode = new Role2[g.getNodes().length];

			for (Node node : g.getNodes()) {
				double z;
				if (withHubs) {
					z = getRelativeWithinModuleDegree(node.getIndex(), g,
							communities);
				} else {
					z = 0; // nodes with z < 0.5 are non-hubs
				}
				int c = getNrOfAdjacentCommunities(node, communities);
				roleOfNode[node.getIndex()] = this.getRole2(c, z);
			}
			g.addProperty(g.getNextKey("ROLES2"), new Roles2(roleOfNode));
		}
		return g;
	}

	/**
	 * @param nodeIndex
	 * @param graph
	 * @param communities
	 * @return nr of links to nodes in the same community
	 */
	private int getWithinModuleDegree(int nodeIndex, Graph graph,
			Communities communities) {
		int k = 0;
		for (int neighbor : graph.getNodes()[nodeIndex].getOutgoingEdges()) {
			if (communities.getCommunityOfNode(nodeIndex).equals(
					communities.getCommunityOfNode(neighbor))) {
				k++;
			}
		}
		return k;
	}

	/**
	 * @param nodeIndex
	 * @param graph
	 * @param communities
	 * @return relative within module degree P
	 */
	private double getRelativeWithinModuleDegree(int nodeIndex, Graph graph,
			Communities communities) {
		double k = getWithinModuleDegree(nodeIndex, graph, communities);
		double avgk = 0;
		double avgkquad = 0;
		Community community = communities.getCommunityOfNode(nodeIndex);

		for (int node : community.getNodes()) {
			int withinDegree = getWithinModuleDegree(node, graph, communities);
			avgk += withinDegree;
			avgkquad += Math.pow(withinDegree, 2);
		}
		avgk /= community.size();
		avgkquad /= community.size();
		double a = (k - avgk);
		double b = Math.sqrt(avgkquad - Math.pow(avgk, 2));
		return (a == 0.0d || b == 0.0d) ? 0.0d : a / b;
	}

	/**
	 * @param node
	 * @param communities
	 * @return number of communities adjacent to the node
	 */
	private int getNrOfAdjacentCommunities(Node node, Communities communities) {
		HashSet<Integer> adjacentCommunities = new HashSet<Integer>();
		for (int neighbor : node.getOutgoingEdges()) {
			adjacentCommunities.add(communities.getCommunityOfNode(neighbor)
					.getIndex());
		}
		return adjacentCommunities.size();
	}

	/**
	 * @param c
	 *            numberOfAdjacentCommunities
	 * @param z
	 *            relativeWithinModuleDegree
	 * @return role2
	 */
	private Role2 getRole2(int c, double z) {
		if (z < 0.5) {
			if (c <= 1) {
				return Role2.COMMON;
			} else if (c == 2) {
				return Role2.BRIDGE;
			} else {
				return Role2.STAR;
			}
		} else {
			if (c <= 1) {
				return Role2.HUB_COMMON;
			} else if (c == 2) {
				return Role2.HUB_BRIDGE;
			} else {
				return Role2.HUB_STAR;
			}
		}
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("COMMUNITIES_0");
	}

}

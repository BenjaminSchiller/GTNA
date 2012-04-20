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
 * GuimeraRolesTransformation.java
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
package gtna.transformation.communities;

import gtna.communities.CommunityList;
import gtna.communities.Community;
import gtna.communities.GuimeraRole;
import gtna.communities.Role;
import gtna.communities.RoleList;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.transformation.Transformation;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class GuimeraRolesTransformation extends Transformation {
	public GuimeraRolesTransformation() {
		super("ROLES_GUIMERA");
	}

	@Override
	public Graph transform(Graph g) {
		GraphProperty[] properties = g.getProperties("COMMUNITIES");
		for (GraphProperty gp : properties) {
			CommunityList communities = (CommunityList) gp;
			Role[] roles = new Role[g.getNodes().length];

			for (Node node : g.getNodes()) {
				double z = this.getRelativeWithinModuleDegree(node.getIndex(),
						g, communities);
				double p = this.getParticipationCoefficient(node, communities);
				roles[node.getIndex()] = this.getRole(z, p);
			}
			g.addProperty(g.getNextKey("ROLES_GUIMERA"),
					new RoleList(roles));
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
			CommunityList communities) {
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
			CommunityList communities) {
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
	 * @return participation coefficient z
	 */
	private double getParticipationCoefficient(Node node,
			CommunityList communities) {
		HashMap<Community, Integer> counter = new HashMap<Community, Integer>();

		for (int dst : node.getOutgoingEdges()) {
			Community community = communities.getCommunityOfNode(dst);
			int count = counter.containsKey(community) ? counter.get(community) + 1
					: 1;
			counter.put(community, count);
		}

		double psum = 0;
		for (Community community : counter.keySet()) {
			psum += Math.pow(
					(double) counter.get(community)
							/ (double) node.getOutDegree(), 2);
		}
		return 1.0 - psum;
	}

	/**
	 * @param z
	 *            relativeWithinModuleDegree
	 * @param p
	 *            participationCoefficient
	 * @return role
	 */
	private Role getRole(double z, double p) {
		if (z < 2.5) {
			if (p <= 0.05) {
				return new GuimeraRole(
						GuimeraRole.GuimeraRoleType.ULTRA_PERIPHERAL);
			} else if (p <= 0.62) {
				return new GuimeraRole(GuimeraRole.GuimeraRoleType.PERIPHERAL);
			} else if (p <= 0.8) {
				return new GuimeraRole(
						GuimeraRole.GuimeraRoleType.SATELLITE_CONNECTOR);
			} else {
				return new GuimeraRole(GuimeraRole.GuimeraRoleType.KINLESS_NODE);
			}
		} else {
			if (p <= 0.3) {
				return new GuimeraRole(
						GuimeraRole.GuimeraRoleType.PROVINCIAL_HUB);
			} else if (p <= 0.75) {
				return new GuimeraRole(
						GuimeraRole.GuimeraRoleType.CONNECTOR_HUB);
			} else {
				return new GuimeraRole(GuimeraRole.GuimeraRoleType.GLOBAL_HUB);
			}
		}
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("COMMUNITIES_0");
	}
}

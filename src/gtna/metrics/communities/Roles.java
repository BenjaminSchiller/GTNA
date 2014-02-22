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
 * RolesCorrelation.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2014-02-03 : readData (Tim Grube)
 *
 */
package gtna.metrics.communities;

import gtna.communities.Role;
import gtna.communities.Role.RoleType;
import gtna.communities.RoleList;
import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.ArrayUtils;
import gtna.util.Config;
import gtna.util.Distribution;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class Roles extends Metric {
	private RoleType type;

	private Role[] roles;

	private double[][] rolesMaxPerNode;

	private double[][] rolesMaxFraction;

	private double[] rolesMax;

	public Roles(RoleType type) {
		super("ROLES", new Parameter[] { new StringParameter("TYPE",
				type.toString()) });

		this.type = type;

		this.roles = Role.getRoleTypes(type);

		this.initConfig();

		Config.overwrite("ROLES_SINGLES_KEYS", "");
		Config.overwrite("ROLES_SINGLES_PLOTS", "");
		Config.overwrite("ROLES_TABLE_KEYS", "");

		this.rolesMaxPerNode = new double[this.roles.length][];
		this.rolesMaxFraction = new double[this.roles.length][];
		this.rolesMax = new double[this.roles.length];
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ROLES_" + this.type.toString() + "_0");
	}

	private void initConfig() {
		this.dataKeys = new ArrayList<String>();
		this.singleKeys = new ArrayList<String>();

		for (Role role : this.roles) {
			String key = "ROLES_" + this.type.toString()
					+ "_ROLES_MAX_PER_NODE_" + role.getKey();
			String name = "Occurrences of most frequent role per node ("
					+ role.getName() + ")";
			String filename = "max-per-node-" + role.toIndex() + "-"
					+ role.getKey();
			String title = name;
			String x = "Nodes sorted by descending frequency";
			String y = "Fraction of occurrences of most frequent role ("
					+ role.getName() + ")";
			this.addMulti(key, name, filename, title, x, y, false);
		}

		for (Role role : this.roles) {
			String key = "ROLES_" + this.type.toString()
					+ "_ROLES_MAX_FRACTION_" + role.getKey();
			String name = "Fraction of role occurrences for max "
					+ role.getName();
			String filename = "max-fraction-" + role.toIndex() + "-"
					+ role.getKey();
			String title = name;
			String x = "Role";
			String y = "Fraction";
			this.addMulti(key, name, filename, title, x, y, true);
		}

		String key = "ROLES_" + this.type.toString() + "_ROLES_MAX";
		String name = "Occurrence of role as most frequent role";
		String filename = "max";
		String title = name;
		String x = "Roles";
		String y = "Fraction of occurrence of role as most frequent role";
		this.addMulti(key, name, filename, title, x, y, true);
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		GraphProperty[] gp = g.getProperties("ROLES_" + this.type.toString());
		RoleList[] roleLists = new RoleList[gp.length];
		for (int i = 0; i < gp.length; i++) {
			roleLists[i] = (RoleList) gp[i];
		}

		for (Role role : this.roles) {
			this.rolesMaxPerNode[role.toIndex()] = this.computeMaxPerNode(g,
					roleLists, role);
		}
		for (Role role : this.roles) {
			this.rolesMaxFraction[role.toIndex()] = this.computeMaxFraction(g,
					roleLists, role);
		}
		this.rolesMax = this.computeMax(g, this.rolesMaxPerNode);
	}

	private double[] computeMax(Graph g, double[][] rolesMaxPerNode) {
		double[] max = new double[this.roles.length];
		for (Role role : this.roles) {
			max[role.toIndex()] = rolesMaxPerNode[role.toIndex()].length;
		}

		ArrayUtils.divide(max, (double) g.getNodes().length);
		return max;
	}

	private double[] computeMaxFraction(Graph g, RoleList[] roleLists, Role role) {
		double[] fraction = new double[this.roles.length];

		int counter = 0;
		for (Node node : g.getNodes()) {
			if (!this.getMaxRole(node, roleLists).equals(role)) {
				continue;
			}
			counter++;

			for (Role r : this.roles) {
				fraction[r.toIndex()] += this.getCount(node, roleLists, r);
			}
		}

		ArrayUtils.divide(fraction, (double) (counter * roleLists.length));
		return fraction;
	}

	private double[] computeMaxPerNode(Graph g, RoleList[] roleLists, Role role) {
		ArrayList<Double> list = new ArrayList<Double>();
		for (Node node : g.getNodes()) {
			Role maxRole = this.getMaxRole(node, roleLists);
			if (maxRole.equals(role)) {
				int count = this.getCount(node, roleLists, maxRole);
				list.add((double) count / (double) roleLists.length);
			}
		}

		double[] fraction = ArrayUtils.toDoubleArray(list);
		Arrays.sort(fraction);
		ArrayUtils.reverse(fraction);
		return fraction;
	}

	private Role getMaxRole(Node node, RoleList[] roleLists) {
		int[] count = new int[this.roles.length];
		for (RoleList roleList : roleLists) {
			Role role = roleList.getRole(node.getIndex());
			count[role.toIndex()]++;
		}

		int maxCount = count[0];
		Role maxRole = this.roles[0];
		for (int i = 1; i < count.length; i++) {
			if (count[i] > maxCount) {
				maxCount = count[i];
				maxRole = this.roles[i];
			}
		}

		return maxRole;
	}

	private int getCount(Node node, RoleList[] roleLists, Role role) {
		int count = 0;
		for (RoleList roleList : roleLists) {
			if (roleList.getRole(node.getIndex()).equals(role)) {
				count++;
			}
		}

		return count;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;

		for (Role role : this.roles) {
			success &= DataWriter.writeWithIndex(
					this.rolesMaxPerNode[role.toIndex()],
					"ROLES_" + this.type.toString() + "_ROLES_MAX_PER_NODE_"
							+ role.getKey(), folder);
			success &= DataWriter.writeWithIndex(
					this.rolesMaxFraction[role.toIndex()],
					"ROLES_" + this.type.toString() + "_ROLES_MAX_FRACTION_"
							+ role.getKey(), folder);
		}
		success &= DataWriter.writeWithIndex(this.rolesMax, "ROLES_"
				+ this.type.toString() + "_ROLES_MAX", folder);

		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[] {};
	}
	
	@Override
	public Distribution[] getDistributions(){
		return new Distribution[] {};
	}
	
	@Override
	public NodeValueList[] getNodeValueLists(){
		return new NodeValueList[] {};
	}
	
	@Override
	public boolean readData(String folder){
		
		/* 2D Values */
		for (Role role : this.roles) {
			this.rolesMaxPerNode[role.toIndex()] = readDistribution(folder, 
					"ROLES_" + this.type.toString() + "_ROLES_MAX_PER_NODE_"
							+ role.getKey());
			this.rolesMaxFraction[role.toIndex()] = readDistribution(folder, "ROLES_" + this.type.toString() + "_ROLES_MAX_FRACTION_"
							+ role.getKey());
		}
		return true;
	}
	
	

	private ArrayList<String> dataKeys;

	public String[] getDataKeys() {
		return ArrayUtils.toStringArray(this.dataKeys);
	}

	private ArrayList<String> singleKeys;

	public String[] getSingleKeys() {
		return ArrayUtils.toStringArray(this.singleKeys);
	}

	public String[] getDataPlotKeys() {
		return this.getDataKeys();
	}

	public String[] getSinglePlotKeys() {
		return this.getSingleKeys();
	}

	private void addMulti(String key, String name, String filename,
			String title, String x, String y, boolean xtics) {
		this.dataKeys.add(key);

		Config.overwrite(key + "_DATA_NAME", name);
		Config.overwrite(key + "_DATA_FILENAME", filename);

		Config.overwrite(key + "_PLOT_DATA", key);
		Config.overwrite(key + "_PLOT_FILENAME", filename);
		Config.overwrite(key + "_PLOT_TITLE", title);
		Config.overwrite(key + "_PLOT_X", x);
		Config.overwrite(key + "_PLOT_Y", y);

		if (xtics) {
			StringBuffer buff = new StringBuffer();
			for (Role role : this.roles) {
				if (buff.length() > 0) {
					buff.append(", ");
				}
				buff.append("\"" + role.getName() + "\" " + role.toIndex());
			}
			Config.overwrite(key + "_PLOT_XTICS", buff.toString());
		}
	}

}

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
 * RolesComparison.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.communities;

import gtna.communities.Roles;
import gtna.communities.Roles.Role;
import gtna.communities.Roles2;
import gtna.communities.Roles2.Role2;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * @author Flipp
 * 
 */
public class RolesComparison extends Metric {

	private int[][] results;
	private int[][] resultsR2;
	private int countRoles;
	private int countRoles2;
	private TreeSet<MaxRolePercRole> erg;
	private TreeSet<MaxRolePercRole2> erg2;

	/**
	 * @param key
	 */
	public RolesComparison() {
		super("ROLES_COMPARISON");
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if (!applicable(g, n, m))
			return;

		results = new int[n.getNodes()][Roles.Role.values().length];
		resultsR2 = new int[n.getNodes()][Roles2.Role2.values().length];

		GraphProperty[] p = g.getProperties("ROLES");
		countRoles = p.length;
		Roles r;

		for (GraphProperty akt : p) {
			r = (Roles) akt;
			for (int i = 0; i < g.getNodes().length; i++) {
				results[i][r.getRoleOfNode(i).ordinal()]++;
			}
		}

		p = g.getProperties("ROLES2");
		countRoles2 = p.length;
		Roles2 r2;
		for (GraphProperty akt : p) {
			r2 = (Roles2) akt;
			for (int i = 0; i < g.getNodes().length; i++) {
				resultsR2[i][r2.getRoleOfNode(i).ordinal()]++;
			}
		}

		erg = new TreeSet<MaxRolePercRole>();

		for (int i = 0; i < results.length; i++) {
			erg.add(new MaxRolePercRole(results[i], countRoles, i));
		}

		erg2 = new TreeSet<MaxRolePercRole2>();

		for (int i = 0; i < results.length; i++) {

			erg2.add(new MaxRolePercRole2(resultsR2[i], countRoles2, i));
		}

	}

	@Override
	public boolean writeData(String folder) {
		boolean ret = true;
		ret &= DataWriter.writeWithIndex(toValueArray(erg, null), getKey()
				+ "_RMAX_ROLES", folder);

		Role akt;
		Role subRole;
		for (int i = 0; i < Roles.Role.values().length; i++){
			akt = Roles.Role.values()[i];
			for (int j = 0; j < Roles.Role.values().length; j++){
				subRole = Roles.Role.values()[j];
				ret &= DataWriter.writeWithIndex(
						toValueArray(filter(erg, akt), subRole),
						getKey() + "_RMAX_ROLES_R" + (i+1) + "_R"
								+ (j+1), folder);
			}
		}

		ret &= DataWriter.writeWithIndex(calcAverages(erg),
				getKey() + "_RPERC", folder);

		ret &= DataWriter.writeWithIndex(toValueArray2(erg2, null), getKey()
				+ "_RMAX_ROLES2", folder);

		Role2 akt2;
		Role2 subRole2;
		for (int i = 0; i < Roles2.Role2.values().length; i++){
			akt2 = Roles2.Role2.values()[i];
			for (int j = 0; j < Roles2.Role2.values().length; j++){
				subRole2 = Roles2.Role2.values()[j];
				ret &= DataWriter.writeWithIndex(
						toValueArray2(filter2(erg2, akt2), subRole2),
						getKey() + "_RMAX_ROLES2_R" + (i+1) + "_R"
								+ (j+1), folder);
			}
		}

		ret &= DataWriter.writeWithIndex(calcAverages2(erg2), getKey()
				+ "_R2PERC", folder);

		return ret;
	}

	/**
	 * @param erg22
	 * @return
	 */
	private double[] calcAverages2(TreeSet<MaxRolePercRole2> erg3) {
		double[] ret = new double[Role2.values().length];
		int[] c = new int[Role2.values().length];

		for (MaxRolePercRole2 akt : erg3) {
			ret[akt.getRole().ordinal()] += akt.getValue();
			c[akt.getRole().ordinal()]++;
		}

		for (int i = 0; i < ret.length; i++)
			ret[i] = (double) (ret[i] / c[i]);

		return ret;
	}

	/**
	 * @param erg3
	 * @return
	 */
	private double[] calcAverages(TreeSet<MaxRolePercRole> erg3) {
		double[] ret = new double[Role.values().length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = 0;
		double[] c = new double[Role.values().length];

		for (MaxRolePercRole akt : erg3) {
			ret[akt.getRole().ordinal()] += akt.getValue();
			c[akt.getRole().ordinal()]++;
		}

		for (int i = 0; i < ret.length; i++) {
			if (ret[i] != 0)
				ret[i] = (double) (ret[i] / c[i]);
		}

		return ret;

	}

	/**
	 * @param erg22
	 * @param akt
	 * @return
	 */
	private TreeSet<MaxRolePercRole2> filter2(TreeSet<MaxRolePercRole2> erg3,
			Role2 akt) {
		TreeSet<MaxRolePercRole2> r = new TreeSet<MaxRolePercRole2>();
		for (MaxRolePercRole2 aktp : erg3)
			if (aktp.getRole().equals(akt))
				r.add(aktp);

		return r;
	}

	/**
	 * @param erg3
	 * @param akt
	 * @return
	 */
	private TreeSet<MaxRolePercRole> filter(TreeSet<MaxRolePercRole> erg3,
			Role akt) {
		TreeSet<MaxRolePercRole> r = new TreeSet<MaxRolePercRole>();
		for (MaxRolePercRole aktp : erg3)
			if (aktp.getRole().equals(akt)) {
				r.add(aktp);
			}

		return r;

	}

	/**
	 * @param erg3
	 * @return
	 */
	private double[] toValueArray(TreeSet<MaxRolePercRole> erg3, Role aktRole) {
		if (erg3.size() == 0)
			return new double[] { 0 };

		double[] ret = new double[erg3.size()];
		int i = 0;

		for (MaxRolePercRole akt : erg3) {
			if (aktRole == null)
				ret[i] = akt.getValue();
			else
				ret[i] = akt.getValueOfRole(aktRole);
			i++;
		}

		return ret;

	}

	/**
	 * @param erg22
	 * @return
	 */
	private double[] toValueArray2(TreeSet<MaxRolePercRole2> erg3, Role2 aktRole) {
		double[] ret = new double[erg3.size()];
		int i = 0;

		for (MaxRolePercRole2 akt : erg3) {
			if (aktRole == null)
				ret[i] = akt.getValue();
			else
				ret[i] = akt.getValueOfRole(aktRole);
			i++;
		}

		return ret;

	}

	@Override
	public Single[] getSingles() {
		return new Single[] {};
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ROLES_0") || g.hasProperty("ROLES2_0");
	}

}

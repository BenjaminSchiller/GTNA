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
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.communities;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class Roles implements GraphProperty {
	private Role[] roles;

	private byte[] roleOfNode;

	public Roles() {
		this.roles = new Role[] {};
		this.roleOfNode = new byte[] {};
	}

	public Roles(Role[] roles) {
		this.roles = roles;
		this.computeRoleOfNodes();
	}

	private void computeRoleOfNodes() {
		int sum = 0;
		for (Role r : this.roles) {
			sum += r.getNodes().length;
		}
		this.roleOfNode = new byte[sum];
		for (Role r : this.roles) {
			for (int n : r.getNodes()) {
				this.roleOfNode[n] = r.getType();
			}
		}
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		// CLASS
		fw.writeComment(Config.get("GRAPH_PROPERTY_CLASS"));
		fw.writeln(this.getClass().toString());

		// KEYS
		fw.writeComment(Config.get("GRAPH_PROPERTY_KEY"));
		fw.writeln(key);

		fw.writeln();

		// LIST OF ROLES
		for (Role role : this.roles) {
			fw.writeln(role.getStringRepresentation());
		}

		return fw.close();
	}

	@Override
	public void read(String filename, Graph graph) {
		Filereader fr = new Filereader(filename);

		// CLASS
		fr.readLine();

		// KEYS
		String key = fr.readLine();

		// ROLES
		this.roles = new Role[7];
		String line = null;
		while ((line = fr.readLine()) != null) {
			Role r = new Role(line);
			this.roles[r.getType() - 1] = r;
		}
		for (int i = 0; i < this.roles.length; i++) {
			if (this.roles[i] == null) {
				this.roles[i] = new Role((byte) (i + 1));
			}
		}

		fr.close();

		this.computeRoleOfNodes();

		graph.addProperty(key, this);
	}

	public Role getRole(byte type) {
		return this.roles[type - 1];
	}

	/**
	 * @return the roles
	 */
	public Role[] getRoles() {
		return this.roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Role[] roles) {
		this.roles = roles;
	}

	/**
	 * @return the roleOfNode
	 */
	public byte[] getRoleOfNode() {
		return this.roleOfNode;
	}

	/**
	 * @param roleOfNode
	 *            the roleOfNode to set
	 */
	public void setRoleOfNode(byte[] roleOfNode) {
		this.roleOfNode = roleOfNode;
	}

}

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
 * Roles2.java
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
package gtna.communities;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

import java.util.ArrayList;
import java.util.HashMap;

public class Roles2 implements GraphProperty {
	private Role2[] roles;

	private byte[] roleOfNode;

	public Roles2() {
		this.roles = new Role2[] {};
		this.roleOfNode = new byte[] {};
	}

	public Roles2(HashMap<Integer, Byte> map) {
		HashMap<Byte, ArrayList<Integer>> r = new HashMap<Byte, ArrayList<Integer>>();
		for (byte b = 1; b <= 6; b++) {
			r.put(b, new ArrayList<Integer>());
		}
		for (int index : map.keySet()) {
			byte role = map.get(index);
			r.get(role).add(index);
		}
		this.initRoles();
		for (byte b = 1; b <= 6; b++) {
			this.roles[b - 1] = new Role2(b, r.get(b));
		}
	}

	public Roles2(ArrayList<Role2> roles) {
		this.initRoles();
		for (Role2 role : roles) {
			this.roles[role.getType() - 1] = role;
		}
		this.computeRoleOfNodes();
	}

	public Roles2(Role2[] roles) {
		this.initRoles();
		for (Role2 role : roles) {
			this.roles[role.getType() - 1] = role;
		}
		this.computeRoleOfNodes();
	}

	private void initRoles() {
		this.roles = new Role2[6];
		for (int i = 0; i < this.roles.length; i++) {
			this.roles[i] = new Role2((byte) (i + 1));
		}
	}

	private void computeRoleOfNodes() {
		int sum = 0;
		for (Role2 r : this.roles) {
			sum += r.getNodes().length;
		}
		this.roleOfNode = new byte[sum];
		for (Role2 r : this.roles) {
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
		fw.writeln(this.getClass().getCanonicalName().toString());

		// KEYS
		fw.writeComment(Config.get("GRAPH_PROPERTY_KEY"));
		fw.writeln(key);

		fw.writeln();

		// LIST OF ROLES
		for (Role2 role : this.roles) {
			fw.writeln(role.toString());
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
		this.roles = new Role2[6];
		String line = null;
		while ((line = fr.readLine()) != null) {
			Role2 r = new Role2(line);
			this.roles[r.getType() - 1] = r;
		}
		for (int i = 0; i < this.roles.length; i++) {
			if (this.roles[i] == null) {
				this.roles[i] = new Role2((byte) (i + 1));
			}
		}

		fr.close();

		this.computeRoleOfNodes();

		graph.addProperty(key, this);
	}

	public Role2 getRole(byte type) {
		return this.roles[type - 1];
	}

	/**
	 * @return the roles
	 */
	public Role2[] getRoles() {
		return this.roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Role2[] roles) {
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

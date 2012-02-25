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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class Roles implements GraphProperty {
	public static enum Role {
		ULTRA_PERIPHERAL, PERIPHERAL, SATELLITE_CONNECTOR, KINLESS_NODE, PROVINCIAL_HUB, CONNECTOR_HUB, GLOBAL_HUB
	};

	private HashMap<Role, ArrayList<Integer>> roles;
	private Role[] roleOfNode;

	public Roles() {
		this.roles = new HashMap<Role, ArrayList<Integer>>();
		this.roleOfNode = new Role[0];
	}

	public Roles(Role[] roleOfNode) {
		this.roles = new HashMap<Role, ArrayList<Integer>>();
		for (Role r : Role.values()) {
			this.roles.put(r, new ArrayList<Integer>());
		}
		for (int i = 0; i < roleOfNode.length; i++) {
			this.roles.get(roleOfNode[i]).add(i);
		}
		this.roleOfNode = roleOfNode;
	}

	public ArrayList<Integer> getNodesOfRole(Role r) {
		return this.roles.get(r);
	}

	public Role getRoleOfNode(int index) {
		return this.roleOfNode[index];
	}

	public static int toIndex(Role r) {
		if (r == Role.ULTRA_PERIPHERAL) {
			return 1;
		} else if (r == Role.PERIPHERAL) {
			return 2;
		} else if (r == Role.SATELLITE_CONNECTOR) {
			return 3;
		} else if (r == Role.KINLESS_NODE) {
			return 4;
		} else if (r == Role.PROVINCIAL_HUB) {
			return 5;
		} else if (r == Role.CONNECTOR_HUB) {
			return 6;
		} else if (r == Role.GLOBAL_HUB) {
			return 7;
		} else {
			return -1;
		}
	}

	public static String toString(Role r) {
		if (r == Role.ULTRA_PERIPHERAL) {
			return "UP";
		} else if (r == Role.PERIPHERAL) {
			return "P";
		} else if (r == Role.SATELLITE_CONNECTOR) {
			return "SC";
		} else if (r == Role.KINLESS_NODE) {
			return "KN";
		} else if (r == Role.PROVINCIAL_HUB) {
			return "PH";
		} else if (r == Role.CONNECTOR_HUB) {
			return "CH";
		} else if (r == Role.GLOBAL_HUB) {
			return "GH";
		} else {
			return null;
		}
	}

	public static Role toRole(int index) {
		if (index == 1) {
			return Role.ULTRA_PERIPHERAL;
		} else if (index == 2) {
			return Role.PERIPHERAL;
		} else if (index == 3) {
			return Role.SATELLITE_CONNECTOR;
		} else if (index == 4) {
			return Role.KINLESS_NODE;
		} else if (index == 5) {
			return Role.PROVINCIAL_HUB;
		} else if (index == 6) {
			return Role.CONNECTOR_HUB;
		} else if (index == 7) {
			return Role.GLOBAL_HUB;
		} else {
			return null;
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

		// NODES
		fw.writeComment("Nodes");
		fw.writeln(this.roleOfNode.length);

		fw.writeln();

		// LIST OF ROLES
		for (Role r : Role.values()) {
			StringBuffer buff = new StringBuffer();
			ArrayList<Integer> list = this.roles.get(r);
			for (int index : list) {
				if (buff.length() == 0) {
					buff.append(index);
				} else {
					buff.append(";" + index);
				}
			}
			fw.writeln(r + ":" + buff.toString());
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

		// NODES
		int nodes = Integer.parseInt(fr.readLine());

		// ROLES
		this.roles = new HashMap<Role, ArrayList<Integer>>();
		this.roleOfNode = new Role[nodes];
		for (Role r : Role.values()) {
			this.roles.put(r, new ArrayList<Integer>());
		}

		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp1 = line.split(":");
			if (temp1.length == 1 || temp1[1].length() == 0) {
				continue;
			}
			Role r = Role.valueOf(temp1[0]);
			String[] temp2 = temp1[1].split(";");
			ArrayList<Integer> list = this.roles.get(r);
			for (String node : temp2) {
				int index = Integer.parseInt(node);
				list.add(index);
				this.roleOfNode[index] = r;
			}
		}

		fr.close();
		graph.addProperty(key, this);
	}

}

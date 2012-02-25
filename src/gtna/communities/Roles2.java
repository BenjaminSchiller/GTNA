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
	public static enum Role2 {
		COMMON, BRIDGE, STAR, HUB_COMMON, HUB_BRIDGE, HUB_STAR
	};

	private HashMap<Role2, ArrayList<Integer>> roles;
	private Role2[] roleOfNode;

	public Roles2() {
		this.roles = new HashMap<Role2, ArrayList<Integer>>();
		this.roleOfNode = new Role2[0];
	}

	public Roles2(Role2[] roleOfNode) {
		this.roles = new HashMap<Role2, ArrayList<Integer>>();
		for (Role2 r : Role2.values()) {
			this.roles.put(r, new ArrayList<Integer>());
		}
		for (int i = 0; i < roleOfNode.length; i++) {
			this.roles.get(roleOfNode[i]).add(i);
		}
		this.roleOfNode = roleOfNode;
	}

	public ArrayList<Integer> getNodesOfRole(Role2 r) {
		return this.roles.get(r);
	}

	public Role2 getRoleOfNode(int index) {
		return this.roleOfNode[index];
	}

	public static int toIndex(Role2 r) {
		if (r == Role2.COMMON) {
			return 1;
		} else if (r == Role2.BRIDGE) {
			return 2;
		} else if (r == Role2.STAR) {
			return 3;
		} else if (r == Role2.HUB_COMMON) {
			return 4;
		} else if (r == Role2.HUB_BRIDGE) {
			return 5;
		} else if (r == Role2.HUB_STAR) {
			return 6;
		} else {
			return -1;
		}
	}

	public static String toString(Role2 r) {
		if (r == Role2.COMMON) {
			return "C";
		} else if (r == Role2.BRIDGE) {
			return "B";
		} else if (r == Role2.STAR) {
			return "S";
		} else if (r == Role2.HUB_COMMON) {
			return "HC";
		} else if (r == Role2.HUB_BRIDGE) {
			return "HB";
		} else if (r == Role2.HUB_STAR) {
			return "HS";
		} else {
			return null;
		}
	}

	public static Role2 toRole(int index) {
		if (index == 1) {
			return Role2.COMMON;
		} else if (index == 2) {
			return Role2.BRIDGE;
		} else if (index == 3) {
			return Role2.STAR;
		} else if (index == 4) {
			return Role2.HUB_COMMON;
		} else if (index == 5) {
			return Role2.HUB_BRIDGE;
		} else if (index == 6) {
			return Role2.HUB_STAR;
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
		for (Role2 r : Role2.values()) {
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
		this.roles = new HashMap<Role2, ArrayList<Integer>>();
		this.roleOfNode = new Role2[nodes];
		for (Role2 r : Role2.values()) {
			this.roles.put(r, new ArrayList<Integer>());
		}

		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] temp1 = line.split(":");
			if (temp1.length == 1 || temp1[1].length() == 0) {
				continue;
			}
			Role2 r = Role2.valueOf(temp1[0]);
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

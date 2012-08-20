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
 * RoleGuimera.java
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

import gtna.util.Config;

/**
 * @author benni
 * 
 *         Role based on the definitions from Guimera et al. 
 *         "Classes of complex networks defined by role-to-role connectivity profiles"
 *         (2007)
 * 
 */
public class GuimeraRole extends Role {
	private GuimeraRoleType type;

	public GuimeraRole() {
		this.type = null;
	}

	public GuimeraRole(GuimeraRoleType type) {
		this.type = type;
	}

	public static enum GuimeraRoleType {
		ULTRA_PERIPHERAL, PERIPHERAL, SATELLITE_CONNECTOR, KINLESS_NODE, PROVINCIAL_HUB, CONNECTOR_HUB, GLOBAL_HUB
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof GuimeraRole)) {
			return false;
		}
		return ((GuimeraRole) obj).type == this.type;
	}

	public int hashCode() {
		return this.type.toString().hashCode();
	}

	@Override
	public String toString() {
		return this.type.toString();
	}

	@Override
	public String getName() {
		return Config.get("ROLES_GUIMERA_" + this.type.toString() + "_NAME");
	}

	@Override
	public int toInt() {
		switch (this.type) {
		case ULTRA_PERIPHERAL:
			return 0;
		case PERIPHERAL:
			return 1;
		case SATELLITE_CONNECTOR:
			return 2;
		case KINLESS_NODE:
			return 3;
		case PROVINCIAL_HUB:
			return 4;
		case CONNECTOR_HUB:
			return 5;
		case GLOBAL_HUB:
			return 6;
		}
		return -1;
	}

	@Override
	public void setType(String typeStr) {
		this.type = GuimeraRoleType.valueOf(typeStr);
	}

	@Override
	public void setType(int typeInt) {
		switch (typeInt) {
		case 0:
			this.type = GuimeraRoleType.ULTRA_PERIPHERAL;
			break;
		case 1:
			this.type = GuimeraRoleType.PERIPHERAL;
			break;
		case 2:
			this.type = GuimeraRoleType.SATELLITE_CONNECTOR;
			break;
		case 3:
			this.type = GuimeraRoleType.KINLESS_NODE;
			break;
		case 4:
			this.type = GuimeraRoleType.PROVINCIAL_HUB;
			break;
		case 5:
			this.type = GuimeraRoleType.CONNECTOR_HUB;
			break;
		case 6:
			this.type = GuimeraRoleType.GLOBAL_HUB;
			break;
		default:
			this.type = null;
			break;
		}
	}

	@Override
	public Role[] getRoleTypes() {
		Role[] roles = new Role[GuimeraRoleType.values().length];
		int index = 0;
		for (GuimeraRoleType type : GuimeraRoleType.values()) {
			roles[index++] = new GuimeraRole(type);
		}
		return roles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.communities.Role#getKey()
	 */
	@Override
	public String getKey() {
		switch (this.type) {
		case ULTRA_PERIPHERAL:
			return "UP";
		case PERIPHERAL:
			return "P";
		case SATELLITE_CONNECTOR:
			return "SC";
		case KINLESS_NODE:
			return "KN";
		case PROVINCIAL_HUB:
			return "PH";
		case CONNECTOR_HUB:
			return "CH";
		case GLOBAL_HUB:
			return "GH";
		}
		return null;
	}

	@Override
	public int toIndex() {
		return this.toInt();
	}

	@Override
	public RoleType getRoleType() {
		return RoleType.GUIMERA;
	}
}

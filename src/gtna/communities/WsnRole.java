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
 * RoleWsn.java
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

import gtna.communities.Role.RoleType;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class WsnRole extends Role {
	private WsnRoleType type;

	public WsnRole() {
		this.type = null;
	}

	public WsnRole(WsnRoleType type) {
		this.type = type;
	}

	public static enum WsnRoleType {
		COMMON, BRIDGE, STAR, HUB_COMMON, HUB_BRIDGE, HUB_STAR
	};

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof WsnRole)) {
			return false;
		}
		return ((WsnRole) obj).type == this.type;
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
		return Config.get("ROLES_WSN_" + this.type.toString() + "_NAME");
	}

	@Override
	public int toInt() {
		switch (this.type) {
		case COMMON:
			return 0;
		case BRIDGE:
			return 1;
		case STAR:
			return 2;
		case HUB_COMMON:
			return 3;
		case HUB_BRIDGE:
			return 4;
		case HUB_STAR:
			return 5;
		}
		return -1;
	}

	@Override
	public void setType(String typeStr) {
		this.type = WsnRoleType.valueOf(typeStr);
	}

	@Override
	public void setType(int typeInt) {
		switch (typeInt) {
		case 0:
			this.type = WsnRoleType.COMMON;
			break;
		case 1:
			this.type = WsnRoleType.BRIDGE;
			break;
		case 2:
			this.type = WsnRoleType.STAR;
			break;
		case 3:
			this.type = WsnRoleType.HUB_COMMON;
			break;
		case 4:
			this.type = WsnRoleType.HUB_BRIDGE;
			break;
		case 5:
			this.type = WsnRoleType.HUB_STAR;
			break;
		default:
			this.type = null;
			break;
		}
	}

	@Override
	public Role[] getRoleTypes() {
		Role[] roles = new Role[WsnRoleType.values().length];
		int index = 0;
		for (WsnRoleType type : WsnRoleType.values()) {
			roles[index++] = new WsnRole(type);
		}
		return roles;
	}

	@Override
	public String getKey() {
		switch (this.type) {
		case COMMON:
			return "C";
		case BRIDGE:
			return "B";
		case STAR:
			return "S";
		case HUB_COMMON:
			return "HC";
		case HUB_STAR:
			return "HS";
		case HUB_BRIDGE:
			return "HB";
		}
		return null;
	}

	@Override
	public int toIndex() {
		return this.toInt();
	}

	@Override
	public RoleType getRoleType() {
		return RoleType.WSN;
	}

}

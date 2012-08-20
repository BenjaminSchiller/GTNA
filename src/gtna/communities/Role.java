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
 * Role.java
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

/**
 * @author benni
 * 
 */
public abstract class Role {
	public static enum RoleType {
		GUIMERA, WSN
	};

	public abstract String getName();

	public abstract String getKey();

	public abstract String toString();

	public abstract int toInt();

	public abstract int toIndex();

	public abstract void setType(String typeStr);

	public abstract void setType(int typeInt);

	public abstract Role[] getRoleTypes();

	public abstract RoleType getRoleType();

	public static Role[] getRoleTypes(RoleType type) {
		switch (type) {
		case GUIMERA:
			return new GuimeraRole().getRoleTypes();
		case WSN:
			return new WsnRole().getRoleTypes();
		default:
			return new Role[0];
		}
	}

	public static Role fromString(RoleType type, String rs) {
		switch (type) {
		case GUIMERA:
			return new GuimeraRole(GuimeraRole.GuimeraRoleType.valueOf(rs));
		case WSN:
			return new WsnRole(WsnRole.WsnRoleType.valueOf(rs));
		default:
			return null;
		}
	}
}

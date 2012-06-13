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
 * RolesNodeSorter.java
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
package gtna.graph.sorting;

import gtna.communities.GuimeraRole;

/**
 * @author benni
 * 
 */
public class GuimeraRolesNodeSorter extends RolesNodeSorter {

	public static final GuimeraRole[] GH_CH_PH_SC_P_UP_KN = new GuimeraRole[] {
			new GuimeraRole(GuimeraRole.GuimeraRoleType.GLOBAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.CONNECTOR_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PROVINCIAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.SATELLITE_CONNECTOR),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.ULTRA_PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.KINLESS_NODE) };

	public static final GuimeraRole[] UP_P_SC = new GuimeraRole[] {
			new GuimeraRole(GuimeraRole.GuimeraRoleType.ULTRA_PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.SATELLITE_CONNECTOR),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.GLOBAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.CONNECTOR_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PROVINCIAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.KINLESS_NODE) };

	public static final GuimeraRole[] UP_SC_P = new GuimeraRole[] {
			new GuimeraRole(GuimeraRole.GuimeraRoleType.ULTRA_PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.SATELLITE_CONNECTOR),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.GLOBAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.CONNECTOR_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PROVINCIAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.KINLESS_NODE) };

	public static final GuimeraRole[] P_UP_SC = new GuimeraRole[] {
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.ULTRA_PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.SATELLITE_CONNECTOR),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.GLOBAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.CONNECTOR_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PROVINCIAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.KINLESS_NODE) };

	public static final GuimeraRole[] P_SC_UP = new GuimeraRole[] {
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.SATELLITE_CONNECTOR),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.ULTRA_PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.GLOBAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.CONNECTOR_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PROVINCIAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.KINLESS_NODE) };

	public static final GuimeraRole[] SC_UP_P = new GuimeraRole[] {
			new GuimeraRole(GuimeraRole.GuimeraRoleType.SATELLITE_CONNECTOR),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.ULTRA_PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.GLOBAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.CONNECTOR_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PROVINCIAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.KINLESS_NODE) };

	public static final GuimeraRole[] SC_P_UP = new GuimeraRole[] {
			new GuimeraRole(GuimeraRole.GuimeraRoleType.SATELLITE_CONNECTOR),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.ULTRA_PERIPHERAL),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.GLOBAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.CONNECTOR_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.PROVINCIAL_HUB),
			new GuimeraRole(GuimeraRole.GuimeraRoleType.KINLESS_NODE) };

	public GuimeraRolesNodeSorter(GuimeraRole[] order) {
		super("GUIMERA", order);
	}

}

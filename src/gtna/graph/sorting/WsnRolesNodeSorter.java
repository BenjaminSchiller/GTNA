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

import gtna.communities.WsnRole;

/**
 * @author benni
 * 
 */
public class WsnRolesNodeSorter extends RolesNodeSorter {

	public static final WsnRole[] HS_S_HB_B_HC_C = new WsnRole[] {
			new WsnRole(WsnRole.WsnRoleType.HUB_STAR),
			new WsnRole(WsnRole.WsnRoleType.STAR),
			new WsnRole(WsnRole.WsnRoleType.HUB_BRIDGE),
			new WsnRole(WsnRole.WsnRoleType.BRIDGE),
			new WsnRole(WsnRole.WsnRoleType.HUB_COMMON),
			new WsnRole(WsnRole.WsnRoleType.COMMON) };
	
	public static final WsnRole[] HS_HB_HC_S_B_C = new WsnRole[] {
			new WsnRole(WsnRole.WsnRoleType.HUB_STAR),
			new WsnRole(WsnRole.WsnRoleType.HUB_BRIDGE),
			new WsnRole(WsnRole.WsnRoleType.HUB_COMMON),
			new WsnRole(WsnRole.WsnRoleType.STAR),
			new WsnRole(WsnRole.WsnRoleType.BRIDGE),
			new WsnRole(WsnRole.WsnRoleType.COMMON) };
	
	public static final WsnRole[] HS_HB_S_B_HC_C = new WsnRole[] {
		new WsnRole(WsnRole.WsnRoleType.HUB_STAR),
		new WsnRole(WsnRole.WsnRoleType.HUB_BRIDGE),
		new WsnRole(WsnRole.WsnRoleType.STAR),
		new WsnRole(WsnRole.WsnRoleType.BRIDGE),
		new WsnRole(WsnRole.WsnRoleType.HUB_COMMON),
		new WsnRole(WsnRole.WsnRoleType.COMMON) };

	public static final WsnRole[] S_B_HS_HB_HC_C = new WsnRole[] {
		new WsnRole(WsnRole.WsnRoleType.STAR),
		new WsnRole(WsnRole.WsnRoleType.BRIDGE),
		new WsnRole(WsnRole.WsnRoleType.HUB_STAR),
		new WsnRole(WsnRole.WsnRoleType.HUB_BRIDGE),
		new WsnRole(WsnRole.WsnRoleType.HUB_COMMON),
		new WsnRole(WsnRole.WsnRoleType.COMMON) };

	public static final WsnRole[] S_HS_B_HB_HC_C = new WsnRole[] {
		new WsnRole(WsnRole.WsnRoleType.STAR),
		new WsnRole(WsnRole.WsnRoleType.HUB_STAR),
		new WsnRole(WsnRole.WsnRoleType.BRIDGE),
		new WsnRole(WsnRole.WsnRoleType.HUB_BRIDGE),
		new WsnRole(WsnRole.WsnRoleType.HUB_COMMON),
		new WsnRole(WsnRole.WsnRoleType.COMMON) };

	public WsnRolesNodeSorter(WsnRole[] order) {
		super("WSN", order);
	}

}

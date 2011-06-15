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
 * LocalMCAttackRandomAll.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-06 : changed names of variables and parameters (BS)
 * 2011-06-06 : removed duplicate methods (BS)
 *
 */
package gtna.transformation.identifier.attackOld;

import gtna.routing.node.RingNode;

import java.util.Random;

public class LocalMCAttackRandomAll extends LocalMCAttack {

	public LocalMCAttackRandomAll(int mode, int iterations, double p,
			double delta, int c, boolean includeDegree1, int attackers) {
		super("LOCALMC_ATTACK_RANDOM_ALL", mode, iterations, p, delta, c,
				includeDegree1, attackers);
	}

	public void lauchAttackActive(RingNode node, Random rand) {
		node.getID().pos = rand.nextDouble();

	}

	public double lauchAttackPassiv(RingNode node, RingNode cur, double min,
			Random rand) {
		double res;
		int k = -1;
		for (int i = 0; i < node.out().length; i++) {
			if (node.out()[i].equals(cur)) {
				k = i;
				break;
			}
		}
		if (k == -1) {
			res = node.getID().pos;
		} else {
			res = node.getID().pos + k * (double) 1 / (node.out().length);
			if (res > 1) {
				res--;
			}
		}
		return res;
	}

}

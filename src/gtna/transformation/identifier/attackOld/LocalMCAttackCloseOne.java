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
 * LocalMCAttackCloseOne.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Stefanie Roos;
 * Contributors:    Benjamin Schiller;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-06 : changed names of variables and parameters (BS)
 * 2011-06-06 : removed duplicate methods (BS)
 *
 */
package gtna.transformation.identifier.attackOld;

import gtna.routing.node.RingNode;

import java.util.HashMap;
import java.util.Random;

public class LocalMCAttackCloseOne extends LocalMCAttack {

	HashMap<Integer, Integer> toAttack;

	public LocalMCAttackCloseOne(int mode, int iterations, double p,
			double delta, int c, boolean includeDegree1, int attackers) {
		super("LOCALMC_ATTACK_CLOSE_ONE", mode, iterations, p, delta, c,
				includeDegree1, attackers);
	}

	public void lauchAttackActive(RingNode node, Random rand) {

	}

	public double lauchAttackPassiv(RingNode node, RingNode cur, double min,
			Random rand) {
		double res;
		if (!node.out()[toAttack.get(node.index())].equals(cur)) {
			return node.getID().pos;
		}
		if (this.mode == 2 || this.mode == 3) {
			res = cur.getID().pos + min + min * rand.nextDouble();
		} else {
			res = cur.getID().pos + min * rand.nextDouble();
		}
		if (res > 1) {
			res = res - 1;
		}
		return res;
	}

}

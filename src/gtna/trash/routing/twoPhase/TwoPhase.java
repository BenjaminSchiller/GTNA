/*
 * ===========================================================
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
 * TwoPhase.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.trash.routing.twoPhase;


// TODO reimplement TwoPhase
public abstract class TwoPhase{
//public abstract class TwoPhase extends RoutingAlgorithmImpl implements
//		RoutingAlgorithm {
//	protected TwoPhase(String key, String[] configKeys, String[] configValues) {
//		super(key, configKeys, configValues);
//	}
//
//	public boolean applicable(Node[] nodes) {
//		return nodes[0] instanceof IDNode;
//	}
//
//	public void init(Node[] nodes) {
//	}
//
//	public Route randomRoute(Node[] nodes, Node src, Random rand) {
//		IDNode s = (IDNode) src;
//		Identifier dest = s.randomID(rand, nodes);
//		return this.phase1(s, s, dest, rand, new IDRouteImpl(dest));
//	}
//
//	protected Route phase1(IDNode src, IDNode current, Identifier dest,
//			Random rand, Route route) {
//		route.add((Node) current);
//		if (current.contains(dest)) {
//			route.setSuccess(true);
//			return route;
//		}
//
//		IDNode max = null;
//		int d = current.out().length + current.in().length;
//		Node[] out = current.out();
//		for (int i = 0; i < out.length; i++) {
//			if (out[i].out().length + out[i].in().length > d) {
//				max = (IDNode) out[i];
//				d = out[i].out().length + out[i].in().length;
//			}
//		}
//		if (max == null) {
//			route.incMessages();
//			return Greedy.route(src, current, dest, route);
//		} else {
//			route.incMessages();
//			return this.phase1(src, max, dest, rand, route);
//		}
//	}
//
//	protected abstract Route phase2(IDNode src, IDNode current,
//			Identifier dest, Random rand, Route route);
}

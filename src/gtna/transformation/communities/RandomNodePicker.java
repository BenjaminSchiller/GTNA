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
 * NodePicker.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.communities;

import java.util.HashMap;

import gtna.graph.Node;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * A <code>RandomNodePicker</code> returns the nodes in random order.
 * 
 * @author Philipp Neubrand
 * 
 */
public class RandomNodePicker implements NodePicker {
	HashMap<Integer, Node> data;

	@Override
	public void addAll(Node[] nodes) {
		data = new HashMap<Integer, Node>();
		for (Node akt : nodes) {
			data.put(akt.getIndex(), akt);
		}

	}

	@Override
	public boolean empty() {
		return (data.size() == 0);
	}

	@Override
	public Node pop() {
		int id = (int) (Math.random() * data.size());
		Node ret = data.values().toArray(new Node[data.size()])[id];
		data.remove(ret.getIndex());
		return ret;
	}

	@Override
	public void remove(int akt) {
		data.remove(akt);
	}

	@Override
	public Parameter[] getParameterArray() {
		return new Parameter[] { new StringParameter("NP_NAME", "Random") };
	}

	@Override
	public int size() {
		return data.size();
	}

}

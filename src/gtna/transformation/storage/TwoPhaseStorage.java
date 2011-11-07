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
 * TwoPhaseStorage.java
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
package gtna.transformation.storage;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.BIIdentifier;
import gtna.id.BIIdentifierSpace;
import gtna.id.DIdentifier;
import gtna.id.DIdentifierSpace;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.id.storage.Storage;
import gtna.id.storage.StorageList;
import gtna.metricsOld.IDSpace;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.math.BigInteger;

/**
 * @author benni
 * 
 */
@SuppressWarnings("rawtypes")
public class TwoPhaseStorage extends TransformationImpl implements
		Transformation {

	private boolean register;

	public TwoPhaseStorage(boolean register) {
		super("TWO_PHASE_STORAGE", new String[] { "" }, new String[] { ""
				+ register });
		this.register = register;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Graph transform(Graph g) {
		GraphProperty[] gps = g.getProperties("ID_SPACE");
		for (GraphProperty p : gps) {
			IdentifierSpace ids = (IdentifierSpace) p;
			Storage[] list = new Storage[g.getNodes().length];
			for (int i = 0; i < list.length; i++) {
				list[i] = new Storage();
			}
			for (Node n : g.getNodes()) {
				Identifier id = ids.getPartitions()[n.getIndex()]
						.getRepresentativeID();
				list[n.getIndex()].add(id);
				if (this.register) {
					Node target = this.firstPhase(n, id, g, ids);
					list[target.getIndex()].add(ids.getPartitions()[n
							.getIndex()].getRepresentativeID());
				}
			}
			g.addProperty(g.getNextKey("STORAGE_LIST"), new StorageList(list));
		}
		return g;
	}

	private Node firstPhase(Node current, Identifier id, Graph g,
			IdentifierSpace ids) {
		int outDegree = current.getOutDegree();
		Node next = null;
		for (int out : current.getOutgoingEdges()) {
			if (g.getNode(out).getOutDegree() > outDegree) {
				next = g.getNode(out);
			}
		}
		if (next == null) {
			return this.secondPhase(current, id, g, ids);
		} else {
			return this.firstPhase(next, id, g, ids);
		}
	}

	private Node secondPhase(Node current, Identifier id, Graph g,
			IdentifierSpace ids) {
		if (ids instanceof BIIdentifierSpace) {
			return this.secondPhaseBI(current, (BIIdentifier) id, g,
					(BIIdentifierSpace) ids);
		} else if (ids instanceof DIdentifierSpace) {
			return this.secondPhaseD(current, (DIdentifier) id, g,
					(DIdentifierSpace) ids);
		} else {
			return null;
		}
	}

	private Node secondPhaseBI(Node current, BIIdentifier id, Graph g,
			BIIdentifierSpace ids) {
		BigInteger minDist = ids.getPartitions()[current.getIndex()]
				.distance(id);
		Node next = null;
		for (int out : current.getOutgoingEdges()) {
			BigInteger dist = ids.getPartitions()[out].distance(id);
			if (dist.compareTo(minDist) == -1) {
				minDist = dist;
				next = g.getNode(out);
			}
		}
		if (next == null) {
			return current;
		} else {
			return this.secondPhaseBI(next, id, g, ids);
		}
	}

	private Node secondPhaseD(Node current, DIdentifier id, Graph g,
			DIdentifierSpace ids) {
		double minDist = ids.getPartitions()[current.getIndex()]
				.getRepresentativeID().distance(id);
		Node next = null;
		for (int out : current.getOutgoingEdges()) {
			double dist = ids.getPartitions()[out].distance(id);
			if (dist < minDist) {
				minDist = dist;
				next = g.getNode(out);
			}
		}
		if (next == null) {
			return current;
		} else {
			return this.secondPhaseD(next, id, g, ids);
		}
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("ID_SPACE_0");
	}

}

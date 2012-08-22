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
 * Chord.java
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
package gtna.networks.p2p.chord;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomChordIDSpace;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.math.BigInteger;

/**
 * @author benni
 * 
 */
public class Chord extends Network {
	public static enum IDSelection {
		UNIFORM, RANDOM
	}

	private int bits;

	private IDSelection selection;

	private int successorLinks;

	// private int predecessorLinks;

	public static Chord[] get(int[] nodes, int bits, int successorLinks,
			IDSelection selection, Transformation[] t) {
		Chord[] nw = new Chord[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw[i] = new Chord(nodes[i], bits, successorLinks, selection, t);
		}
		return nw;
	}

	public static Chord[] get(int nodes, int[] bits, int successorLinks,
			IDSelection selection, Transformation[] t) {
		Chord[] nw = new Chord[bits.length];
		for (int i = 0; i < bits.length; i++) {
			nw[i] = new Chord(nodes, bits[i], successorLinks, selection, t);
		}
		return nw;
	}

	public static Chord[] get(int nodes, int bits, int successorLinks,
			IDSelection[] selection, Transformation[] t) {
		Chord[] nw = new Chord[selection.length];
		for (int i = 0; i < selection.length; i++) {
			nw[i] = new Chord(nodes, bits, successorLinks, selection[i], t);
		}
		return nw;
	}

	public static Chord[] get(int nodes, int bits, int[] successorLinks,
			IDSelection selection, Transformation[] t) {
		Chord[] nw = new Chord[successorLinks.length];
		for (int i = 0; i < successorLinks.length; i++) {
			nw[i] = new Chord(nodes, bits, successorLinks[i], selection, t);
		}
		return nw;
	}

	public Chord(int nodes, int bits, int successorLinks,
			IDSelection selection, Transformation[] t) {
		super("CHORD", nodes, new Parameter[] { new IntParameter("BITS", bits),
				new IntParameter("SUCCESSOR_LINKS", successorLinks),
				new StringParameter("ID_SELECTION", selection.toString()) }, t);
		this.bits = bits;
		this.selection = selection;
		this.successorLinks = successorLinks;
	}

	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		graph.setNodes(nodes);
		RandomChordIDSpace t = new RandomChordIDSpace(this.bits,
				this.selection == IDSelection.UNIFORM);
		graph = t.transform(graph);

		ChordIdentifierSpace idSpace = (ChordIdentifierSpace) graph
				.getProperty("ID_SPACE_0");
		ChordPartition[] partitions = (ChordPartition[]) idSpace
				.getPartitions();

		int[] successors = new int[nodes.length];
		Edges edges = new Edges(nodes, nodes.length * this.bits);
		for (Node node : nodes) {
			ChordPartition p = partitions[node.getIndex()];
			BigInteger id = ((ChordIdentifier) p.getRepresentativeIdentifier()).position;
			// BigInteger predID = p.getPred().getId();
			BigInteger succID = id.add(BigInteger.ONE).mod(idSpace.modulus);

			// int predIndex = this.find(partitions, p.getPred(),
			// node.getIndex());
			successors[node.getIndex()] = this.find(partitions,
					new ChordIdentifier(succID, this.bits), node.getIndex());

			BigInteger add = BigInteger.ONE;
			int[] fingerIndex = new int[this.bits];
			BigInteger[] fingerID = new BigInteger[this.bits];
			for (int i = 0; i < this.bits; i++) {
				fingerID[i] = id.add(add).mod(idSpace.getModulus());
				fingerIndex[i] = this.find(partitions, new ChordIdentifier(
						fingerID[i], this.bits), node.getIndex());
				add = add.shiftLeft(1);
			}

			// edges.add(node.getIndex(), predIndex);
			edges.add(node.getIndex(), successors[node.getIndex()]);
			for (int finger : fingerIndex) {
				edges.add(node.getIndex(), finger);
			}
		}

		for (int s = 1; s < this.successorLinks; s++) {
			for (int n = 0; n < nodes.length; n++) {
				edges.add(n, successors[(n + s) % successors.length]);
			}
		}

		edges.fill();

		return graph;
	}

	private int find(ChordPartition[] partitions, ChordIdentifier id, int start) {
		int index = start;
		while (!partitions[index].contains(id)) {
			index = (index + 1) % partitions.length;
		}
		return index;
	}

}

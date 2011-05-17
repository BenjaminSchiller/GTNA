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
 * WOTReader.java
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
package gtna.io.networks;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;

import java.io.File;
import java.io.RandomAccessFile;
import gtna.util.Timer;
import gtna.util.Util;
import java.util.Vector;

/**
 * This is a reader for the Web of Trust file format version 0.2.
 * 
 * The Web of Trust file format specification is available at:
 *   http://www.lysator.liu.se/~jc/wotsap/wotfileformat.txt
 * 
 * *.wot files are compressed with bzip2, this reader assumes a uncompressed
 * version of the file.
 * 
 * @author Zoran Zaric <zz@zoranzaric.de>
 *
 */
public class WOTReader {
	private final static int SIGNATURE_TYPE_MASK  = 0xF0000000;
	private final static int SIGNATURE_INDEX_MASK = 0x0FFFFFFF;
	
	public static Graph read(String filename) throws Exception {
		Timer timer = new Timer();
		
		RandomAccessFile file = new RandomAccessFile(new File(filename), "r");
		
		// seek to names
		while (!file.readLine().startsWith("names/")) {
		}
		
		// read names
		Vector<WOTNode> wotNodes = new Vector<WOTNode>();
		int index = 0;
		String line;
		while (!(line = file.readLine()).startsWith("keys/")) {
			if (!line.equals("")) {
				WOTNode node = new WOTNode(index, line);
				wotNodes.add(node);
				index += 1;
			}
		}
		
		// read keys
		for (index = 0; index < wotNodes.size(); index++) {
			byte[] key = new byte[4];
			file.read(key);
			WOTNode node = wotNodes.get(index);
			node.key = Util.toInt(key);
		}
		
		// skip the "signatures/" line
		String signaturesLine = file.readLine();
		if (!signaturesLine.startsWith("signatures/")) {
			// FIXME an IllegalArgumentException isn't really semantically correct
			throw new IllegalArgumentException("A line starting with \"signatures/\" was expected.");
		}
		
		// read signatures
		for (index = 0; index < wotNodes.size(); index++) {
			WOTNode node = wotNodes.get(index);
			
			// read how many keys are signed by this key
			byte[] buffer = new byte[4];
			file.read(buffer);
			int signatureCount = Util.toInt(buffer);
			
			// inspect all signatures
			for (int signatureIndex = 0; signatureIndex < signatureCount; signatureIndex++) {
				byte[] signatureBuffer = new byte[4];
				file.read(signatureBuffer);
				
				int signature = Util.toInt(signatureBuffer);
				
				// get the signature type
				// TODO do something with the signature type
				int signatureType = signature & SIGNATURE_TYPE_MASK;
				
				// get the target key
				int targetIndex = signature & SIGNATURE_INDEX_MASK;
				if (targetIndex >= wotNodes.size()) {
					// FIXME an IllegalArgumentException isn't really semantically correct
					throw new IllegalArgumentException("A target index that matches a node was expected.");
				}
				
				// retrieve the target node
				WOTNode target = wotNodes.get(targetIndex);
				node.signatures.add(target);
			}
		}
		// skip the "debug/" line
		String debugLine = file.readLine();
		if (!debugLine.startsWith("debug/")) {
			// FIXME an IllegalArgumentException isn't really semantically correct
			throw new IllegalArgumentException("A line starting with \"debug/was expected.");
		}
		
		file.close();
		timer.end();

		// prepare return graph
		NodeImpl[] nodes = NodeImpl.init(wotNodes.size());
		Edges edges = new Edges(nodes, nodes.length);
		for (WOTNode source : wotNodes) {
			for (WOTNode target : source.signatures) {
				edges.add(nodes[source.index], nodes[target.index]);
			}
		}
		edges.fill();
		Graph g = new Graph("NAME", nodes, timer);
		return g;
	}

	private static class WOTNode {
		public int index;

		public String name;

		public int key;

		public Vector<WOTNode> signatures;

		public WOTNode(int index, String name) {
			this.index = index;
			this.name = name;
			this.key = -1;
			this.signatures = new Vector<WOTReader.WOTNode>();
		}

		public String toString() {
			return this.name + " [" + this.key + "]";
		}
	}
}

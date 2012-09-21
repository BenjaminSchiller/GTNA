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
 * SpanningTree.java
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
package gtna.graph.spanningTree;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Util;

import java.util.ArrayList;

/**
 * @author benni
 * 
 */
public class SpanningTree extends GraphProperty {
	private int[] parent;
	private int[][] children;
	private int[] depth;

	private int src;

	public SpanningTree() {
		this.parent = new int[0];
		this.children = new int[0][0];
		this.src = -1;
	}

	public SpanningTree(Graph g, ArrayList<ParentChild> pcs) {
		this.fill(g.getNodes().length, pcs);
	}

	private void fill(int nodes, ArrayList<ParentChild> pcs) {
		this.parent = Util.initIntArray(nodes, -1);
		this.children = new int[nodes][];
		this.depth = Util.initIntArray(nodes, -1);
		this.src = -1;
		int[] counter = new int[nodes];
		// fill parent and depth list
		for (ParentChild pc : pcs) {
			if (pc.getParent() == -1)
				continue;
			this.parent[pc.getChild()] = pc.getParent();
			this.depth[pc.getChild()] = pc.getDepth();
			counter[pc.getParent()]++;
		}
		// init children list
		for (int i = 0; i < this.children.length; i++) {
			this.children[i] = new int[counter[i]];
		}
		// fill children list
		for (ParentChild pc : pcs) {
			if (pc.getParent() == -1)
				continue;
			this.children[pc.getParent()][this.children[pc.getParent()].length
					- counter[pc.getParent()]] = pc.getChild();
			counter[pc.getParent()]--;
		}
		// find src
		for (int i = 0; i < this.parent.length; i++) {
			if (this.parent[i] == -1) {
				this.src = i;
				this.depth[i] = 0;
				break;
			}
		}
	}

	public Edge[] generateEdgesUnidirectional() {
		int index = 0;
		Edge[] edges = new Edge[this.parent.length - 1];
		for (int i = 0; i < this.parent.length; i++) {
			if (this.parent[i] != -1) {
				edges[index++] = new Edge(this.parent[i], i);
			}
		}
		return edges;
	}

	public Edge[] generateEdgesBidirectional() {
		int index = 0;
		Edge[] edges = new Edge[this.parent.length * 2 - 2];
		for (int i = 0; i < this.parent.length; i++) {
			if (this.parent[i] != -1) {
				edges[index++] = new Edge(this.parent[i], i);
				edges[index++] = new Edge(i, this.parent[i]);
			}
		}
		return edges;
	}

	public ParentChild[] generateParentChildList() {
		int index = 0;
		ParentChild[] pcs = new ParentChild[this.parent.length - 1];
		for (int i = 0; i < this.parent.length; i++) {
			if (parent[i] != -1) {
				pcs[index++] = new ParentChild(parent[i], i, depth[i]);
			}
		}
		return pcs;
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		this.writeHeader(fw, this.getClass(), key);

		this.writeParameter(fw, "Nodes", this.parent.length);

		ParentChild[] pcs = this.generateParentChildList();
		for (ParentChild pc : pcs) {
			fw.writeln(pc.toString());
		}

		return fw.close();
	}

	@Override
	public String read(String filename) {
		Filereader fr = new Filereader(filename);

		String key = this.readHeader(fr);

		int nodes = Integer.parseInt(fr.readLine());

		ArrayList<ParentChild> pcs = new ArrayList<ParentChild>();
		String line = null;
		while ((line = fr.readLine()) != null) {
			pcs.add(new ParentChild(line));
		}

		this.fill(nodes, pcs);

		fr.close();

		return key;
	}

	public int getParent(int child) {
		return this.parent[child];
	}

	public int getDepth(int child) {
		return this.depth[child];
	}

	public int[] getChildren(int parent) {
		return this.children[parent];
	}

	public int getSrc() {
		return this.src;
	}

	public boolean isSrc(int node) {
		return this.src == node;
	}

}

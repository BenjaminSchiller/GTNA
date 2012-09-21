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
 * Communities.java
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
package gtna.communities;

import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class CommunityList extends GraphProperty {
	private Community[] communities;

	private int[] communityOfNode;

	public CommunityList() {
		this.communities = new Community[] {};
		this.communityOfNode = new int[] {};
	}

	public CommunityList(HashMap<Integer, Integer> map) {
		this(CommunityList.compute(map));
	}

	private static Community[] compute(HashMap<Integer, Integer> map) {
		int max = 0;
		for (int value : map.values()) {
			if (value > max) {
				max = value;
			}
		}
		HashMap<Integer, ArrayList<Integer>> communities = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 0; i <= max; i++) {
			communities.put(i, new ArrayList<Integer>());
		}
		for (int index : map.keySet()) {
			int community = map.get(index);
			communities.get(community).add(index);
		}
		Community[] c = new Community[max + 1];
		for (int i = 0; i < c.length; i++) {
			c[i] = new Community(i, communities.get(i));
		}
		return c;
	}

	public CommunityList(ArrayList<Community> communities) {
		this.communities = new Community[communities.size()];
		for (int i = 0; i < communities.size(); i++) {
			this.communities[i] = communities.get(i);
		}
		this.computeCommunityOfNodes();
	}

	public CommunityList(Community[] communities) {
		this.communities = communities;
		this.computeCommunityOfNodes();
	}

	private void computeCommunityOfNodes() {
		int sum = 0;
		for (Community c : this.communities) {
			sum += c.getNodes().length;
		}
		this.communityOfNode = new int[sum];
		for (Community c : this.communities) {
			for (int n : c.getNodes()) {
				this.communityOfNode[n] = c.getIndex();
			}
		}
	}

	public Community[] getCommunities() {
		return communities;
	}

	public Community getCommunityOfNode(int nodeIndex) {
		return communities[communityOfNode[nodeIndex]];
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		this.writeHeader(fw, this.getClass(), key);

		this.writeParameter(fw, "Communities", this.communities.length);

		for (Community community : this.communities) {
			fw.writeln(community.toString());
		}

		return fw.close();
	}

	@Override
	public String read(String filename) {
		Filereader fr = new Filereader(filename);

		String key = this.readHeader(fr);
		this.communities = new Community[this.readInt(fr)];

		String line = null;
		int index = 0;
		while ((line = fr.readLine()) != null) {
			this.communities[index++] = new Community(line);
		}

		fr.close();

		this.computeCommunityOfNodes();

		return key;
	}
}

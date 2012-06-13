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
 * DataTransformation.java
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
package gtna.projects.conext12;

import gtna.graph.Graph;
import gtna.io.graphReader.GraphReader;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * @author benni
 * 
 */
public class DataTransformation {
	public static void main(String[] args) throws IOException {
		for (int nodes = 1000; nodes <= 10000; nodes += 1000) {
			String pre1 = "/home/benni/results-florian/";
			String pre2 = "/home/benni/results-florian-transformed/";
			DataTransformation.transform(pre1 + "random/" + nodes + "/", nodes,
					pre2 + "random/" + nodes + "/");
			DataTransformation.transform(pre1 + "hotspot/" + nodes + "/",
					nodes, pre2 + "/hotspot/" + nodes + "/");
		}
	}

	public static void transform(String src, int nodes, String dst)
			throws IOException {
		System.out.println("\n\n" + src + " ====> " + dst);
		(new File(dst)).mkdirs();

		for (int i = 1; i <= 100; i++) {
			String pre = src + nodes + "-" + i;

			String graphSrc = pre + "-graph.txt";
			String communitiesSrc = pre + "-communities.txt";
			String locationsSrc = pre + "-locations.txt";

			String graph = dst + i + ".gtna";
			String communities = graph + "_COMMUNITIES_0";
			String locations = graph + "_ID_SPACE_0";

			if (!(new File(graphSrc)).exists()
					|| !(new File(communitiesSrc)).exists()
					|| !(new File(locationsSrc)).exists()) {
				System.out.println("not all files found for " + i);
				continue;
			}

			System.out.println("transforming " + i);

			DataTransformation.transformFile(graphSrc, graph);
			DataTransformation.transformFile(communitiesSrc, communities);
			DataTransformation.transformFile(locationsSrc, locations);
		}
	}

	public static void transformFile(String src, String dst) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(src));
		BufferedWriter w = new BufferedWriter(new FileWriter(dst));

		String line = null;
		while ((line = r.readLine()) != null) {
			if (line.equals("Locations")) {
				w.write("ID_SPACE_0" + "\n");
			} else if (line.equals("Communities")) {
				w.write("COMMUNITIES_0" + "\n");
			} else if (line.equals("gtna.communities.Communities")) {
				w.write("gtna.communities.CommunityList" + "\n");
			} else {
				w.write(line + "\n");
			}
		}

		w.close();
		r.close();
	}
}

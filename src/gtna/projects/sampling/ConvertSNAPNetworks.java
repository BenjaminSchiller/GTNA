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
 * SamplingThesis.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.sampling;

import gtna.graph.Graph;
import gtna.io.graphReader.SnapGraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;

import java.io.File;
import java.text.ParseException;

/**
 * @author Tim
 * 
 */
public class ConvertSNAPNetworks {

	private static String folder;
	private static String fileending;

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {

		if (args.length == 0
				|| (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			System.out.println("Usage: \n ./java -jar <yourJARname>.jar folder=<foldercontainingSNAPfile> fileending=<SNAPfileending>");
			System.exit(0);
		}

		for (String s : args) {
			matchArgument(s);
		}
		
		SnapGraphReader sgr = new SnapGraphReader();
		
		File f = new File(folder);
		if(f.isDirectory()){
			File[] files = f.listFiles();
			
			for(File snaps : files){
				if(snaps.getName().endsWith(fileending) && !(new File(folder+snaps.getName().replace(fileending, ".gtna")).exists())){
					System.out.println("> " + snaps.getName());
					String name = snaps.getName().replace(fileending, "");
					
					Graph g = sgr.read(folder + snaps.getName());
					writeGraphToFile(g, folder + name + ".gtna");
					
					
				} else {
					System.out.println("skipping: " + snaps.getName());
				}
			}
		}



	}

	private static String writeGraphToFile(Graph g, String filename) {
		new GtnaGraphWriter().writeWithProperties(g, filename);
		return filename;
	}

	

	/**
	 * @param s
	 * @throws ParseException
	 */
	private static void matchArgument(String s) throws ParseException {
		if (s.startsWith("fileending=")) {
			fileending = s.substring(11);
		} else if (s.startsWith("folder=")) {
			folder = s.substring(7);
			File f = new File(folder);
			if (!f.isDirectory()) {
				throw new IllegalArgumentException("Folder not existing.");
			}
		}
	}
}

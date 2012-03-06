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
 * GDAEvaluation_runtimes.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.GD;

import java.io.File;
import gtna.io.Filereader;
import gtna.io.Filewriter;

/**
 * @author Nico
 * 
 */
public class GDAEvaluation_runtimes {

	public static void main(String[] args) {
		int lengthOfSeries = 25;
		String folderName;
		String rootFolder = "./resources/evaluation/";
		String tempString, lastLine, runtimeString, rtMaxString, rtMinString;

		String[] base = new String[] { "1000/erdosRenyi-10.0-true-b-wcp-gcc-", "5000/erdosRenyi-10.0-true-b-wcp-gcc-",
				"10000/erdosRenyi-10.0-true-b-wcp-gcc-", "1000/barabasiAlbert-10-b-wcp-gcc-",
				"5000/barabasiAlbert-10-b-wcp-gcc-", "10000/barabasiAlbert-10-b-wcp-gcc-", "20057/CAIDA-b-wcp-gcc-",
				"11407/SPI-b-wcp-gcc-", "25487/WOT-b-wcp-gcc-" };
		String[] gdaFolders = new String[] { "CCC-1-100.0-true/", "st-1-100.0-true/", "stBFS-hd-kBST-100.0-100.0/",
//				"stBFS-rand-kBST-100.0-100.0/", "stBFS-hd-ws-100.0-100.0/", "stBFS-rand-ws-100.0-100.0/",
//				"stBFS-rand-mh-100.0-100.0/", "stBFS-hd-mh-100.0-100.0/", "stBFS-hd-bt-100.0-100.0/",
				"stBFS-rand-bt-100.0-100.0/", "fr-100-[100.0, 100.0]-false-100/" };

		int lastFile;
		double runtime, currRt, rtMin, rtMax;
		File tempFile;
		Filereader tempFileReader;
		Filewriter runtimeLogger;
		String[] tempSplit;

		for (String singleBase : base) {
			for (String singleGDA : gdaFolders) {
				folderName = rootFolder + singleBase + singleGDA;
				lastFile = 0;
				runtime = 0;
				rtMin = Double.MAX_VALUE;
				rtMax = Double.MIN_VALUE;
				for (lastFile = 0; lastFile < lengthOfSeries; lastFile++) {
					lastLine = null;
					tempFile = new File(folderName + lastFile + ".txt_ID_SPACE_0");
					if (!tempFile.exists()) {
						break;
					}
					tempFile = new File(folderName + lastFile + ".txt.RUNTIME");
					if (!tempFile.exists()) {
						break;
					}					
					tempFileReader = new Filereader(folderName + lastFile + ".txt.RUNTIME");
					while ((tempString = tempFileReader.readLine()) != null) {
						lastLine = tempString;
					}
					if (lastLine == null) {
						System.err.println("Broken input " + folderName + lastFile + ".txt.RUNTIME");
						continue;
					}
					if (!lastLine.startsWith("GDA")) {
						System.err.println("Last line was not caused by an GDA\n" + lastLine + "\n");
					}
					tempSplit = lastLine.split(":");
					currRt = Double.parseDouble(tempSplit[tempSplit.length - 1]);
					if ( currRt < rtMin ) rtMin = currRt;
					if ( currRt > rtMax ) rtMax = currRt;
					runtime += currRt;
				}
				runtime = runtime / lastFile;
				runtimeString = runtime + " msec";
				if (runtime / 1000 > 1)
					runtimeString += " = " + (runtime / 1000) + " sec";
				if (runtime / 60000 > 0.2)
					runtimeString += " = " + (runtime / 60000) + " min";
				
				rtMaxString = "" + rtMax + "ms";
				rtMinString = "" + rtMin + "ms";
				if ( rtMax / 1000 > 1) {
					rtMaxString = "" + (rtMax/1000) + "s";
					rtMinString = "" + (rtMin/1000) + "s";
				}
				if ( rtMax / 60000 > 0.5) {
					rtMaxString = "" + (rtMax/60000) + "min";
					rtMinString = "" + (rtMin/60000) + "min";
				}

				if ( lastFile < lengthOfSeries ) runtimeString = " -- MISSING -- " + runtimeString;
//				else continue;
				System.out.println(folderName.replace("-b-wcp-gcc-", "-").replace("./resources/evaluation/", "") + ": " + runtimeString + " (min: " + rtMinString + ", max: " + rtMaxString + " after " + lastFile + " simulations)");
//				System.out.println("~/gtna/" + folderName + " " + runtimeString + " (min: " + rtMinString + ", max: " + rtMaxString + " after " + lastFile + " simulations)");
				runtimeLogger = new Filewriter(folderName + "averageGDA.RUNTIME");
				runtimeLogger.writeln("AVG for " + lastFile + " simulations: " + runtimeString);
				runtimeLogger.close();
			}

		}
	}
}

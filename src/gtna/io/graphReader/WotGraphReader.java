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
 * Original Author: Zoran Zaric;
 * Contributors:    Benjamin Schiller;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-05-18 : added support for downloading and reading of multiple files (BS);
 * 
 */
package gtna.io.graphReader;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Output;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.util.Config;
import gtna.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Vector;

/**
 * This is a reader for the Web of Trust file format version 0.2.
 * 
 * The Web of Trust file format specification is available at:
 * http://www.lysator.liu.se/~jc/wotsap/wotfileformat.txt
 * 
 * *.wot files are compressed with bzip2, this reader assumes a uncompressed
 * version of the file.
 * 
 * @author Zoran Zaric <zz@zoranzaric.de>
 * 
 */
public class WotGraphReader extends GraphReader {

	public WotGraphReader() {
		super("WOT");
	}

	private final static int SIGNATURE_INDEX_MASK = 0x0FFFFFFF;

	@Override
	public Graph read(String filename) {
		try {
			RandomAccessFile file = new RandomAccessFile(new File(filename),
					"r");

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
				throw new IllegalArgumentException(
						"A line starting with \"signatures/\" was expected.");
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
					// int signatureType = signature & SIGNATURE_TYPE_MASK;

					// get the target key
					int targetIndex = signature & SIGNATURE_INDEX_MASK;
					if (targetIndex >= wotNodes.size()) {
						// FIXME an IllegalArgumentException isn't really
						// semantically correct
						throw new IllegalArgumentException(
								"A target index that matches a node was expected.");
					}

					// retrieve the target node
					WOTNode target = wotNodes.get(targetIndex);
					node.signatures.add(target);
				}
			}

			// skip the "debug/" line
			String debugLine = file.readLine();
			if (!debugLine.startsWith("debug/")) {
				// FIXME an IllegalArgumentException isn't really semantically
				// correct
				throw new IllegalArgumentException(
						"A line starting with \"debug/was expected.");
			}
			file.close();

			// prepare return graph
			Graph graph = new Graph(this.getGraphName(filename));
			Node[] nodes = Node.init(wotNodes.size(), graph);
			Edges edges = new Edges(nodes, nodes.length);
			for (WOTNode source : wotNodes) {
				for (WOTNode target : source.signatures) {
					edges.add(source.index, target.index);
				}
			}
			edges.fill();
			graph.setNodes(nodes);

			return graph;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int nodes(String filename) {
		try {
			RandomAccessFile file = new RandomAccessFile(new File(filename),
					"r");

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
			file.close();

			return wotNodes.size();
		} catch (Exception e) {
			return 0;
		}
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
			this.signatures = new Vector<WotGraphReader.WOTNode>();
		}

		public String toString() {
			return this.name + " [" + this.key + "]";
		}
	}

	private static final boolean PRINT_ERRORS = Config
			.getBoolean("WOT_READER_PRINT_ERRORS");

	public static void getWOTBetween(int Y1, int M1, int D1, int Y2, int M2,
			int D2) {
		int millisInDay = 1000 * 60 * 60 * 24;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Y2);
		c.set(Calendar.MONTH, M2 - 1);
		c.set(Calendar.DAY_OF_MONTH, D2);
		long end = c.getTimeInMillis();
		c.set(Calendar.YEAR, Y1);
		c.set(Calendar.MONTH, M1 - 1);
		c.set(Calendar.DAY_OF_MONTH, D1);
		while (c.getTimeInMillis() <= end) {
			int Y = c.get(Calendar.YEAR);
			int M = c.get(Calendar.MONTH) + 1;
			int D = c.get(Calendar.DAY_OF_MONTH);
			System.out.println(Y + "-" + M + "-" + D);
			System.out.println("   " + getWOT(Y, M, D));
			c.setTimeInMillis(c.getTimeInMillis() + millisInDay);
		}
	}

	public static boolean getWOT(int y, int m, int d) {
		String Y = "" + y;
		String M = m < 10 ? "0" + m : "" + m;
		String D = d < 10 ? "0" + d : "" + d;

		String fetchOut = Config.get("WOT_READER_FETCH_OUT");
		String fetchFolder = Config.get("WOT_READER_FETCH_FOLDER");
		String fetchCmd = Config.get("WOT_READER_FETCH_CMD");
		fetchOut = replace(fetchOut, Y, M, D);
		fetchFolder = replace(fetchFolder, Y, M, D);
		fetchCmd = replace(fetchCmd, Y, M, D);
		fetchCmd = fetchCmd.replace("%OUT", fetchOut);

		String unzipOut = Config.get("WOT_READER_UNZIP_OUT");
		String unzipFolder = Config.get("WOT_READER_UNZIP_FOLDER");
		String unzipCmd = Config.get("WOT_READER_UNZIP_CMD");
		unzipOut = replace(unzipOut, Y, M, D);
		unzipFolder = replace(unzipFolder, Y, M, D);
		unzipCmd = replace(unzipCmd, Y, M, D);
		unzipCmd = unzipCmd.replace("%WOT", fetchOut);
		unzipCmd = unzipCmd.replace("%OUT", unzipOut);

		String wot = fetchFolder + fetchOut;
		String unzipped = unzipFolder + unzipOut;
		String filename = Config.get("WOT_READER_GRAPH_OUT");
		filename = replace(filename, Y, M, D);

		execute(fetchCmd, fetchFolder);
		if (!(new File(wot)).exists()) {
			return false;
		}

		execute(unzipCmd, unzipFolder);
		if (!(new File(unzipped)).exists()) {
			return false;
		}

		try {
			Graph g = new WotGraphReader().read(unzipped);
			new GtnaGraphWriter().write(g, filename);
			if (Config.getBoolean("WOT_READER_DELETE_WOT_FILE")) {
				String deleteFolder = Config
						.get("WOT_READER_DELETE_WOT_FOLDER");
				String deleteCmd = Config.get("WOT_READER_DELETE_WOT_CMD");
				deleteCmd = replace(deleteCmd, Y, M, D);
				execute(deleteCmd, deleteFolder);
			}
			if (Config.getBoolean("WOT_READER_DELETE_UNZIPPED_FILE")) {
				String deleteFolder = Config
						.get("WOT_READER_DELETE_UNZIPPED_FOLDER");
				String deleteCmd = Config.get("WOT_READER_DELETE_UNZIPPED_CMD");
				deleteCmd = replace(deleteCmd, Y, M, D);
				execute(deleteCmd, deleteFolder);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private static String replace(String str, String Y, String M, String D) {
		return str.replace("%Y", Y).replace("%M", M).replace("%D", D);
	}

	private static boolean execute(String cmd, String dir) {
		if (PRINT_ERRORS) {
			System.out.println("EXECUTING " + cmd + " IN " + dir);
		}
		try {
			Process p = null;
			if (dir == null) {
				p = Runtime.getRuntime().exec(cmd);
			} else {
				p = Runtime.getRuntime().exec(cmd, null, new File(dir));
			}
			if (PRINT_ERRORS) {
				InputStream stderr = p.getErrorStream();
				InputStreamReader isr = new InputStreamReader(stderr);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					Output.writeln(line);
				}
			}
			p.waitFor();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
}

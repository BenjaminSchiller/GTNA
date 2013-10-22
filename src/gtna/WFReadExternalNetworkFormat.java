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
package gtna;

import gtna.graph.Graph;
import gtna.io.graphReader.CaidaGraphReader;
import gtna.io.graphReader.GraphReader;
import gtna.io.graphReader.SnapGraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tim
 * 
 */
public class WFReadExternalNetworkFormat {

	private static String path;
	private static boolean snap = true;
	private static boolean caida = false;;

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ParseException, IOException {


		for (String s : args) {
			matchArgument(s);
		}
		GraphReader gr = null;
		
		System.out.print("initialize with Reader: ");
		if(snap){
		    	System.out.println("SNAP");
			gr = new SnapGraphReader();
		} else if (caida){
		    System.out.println("CAIDA");
		    gr = new CaidaGraphReader();
		}
		
		System.out.println("Reader: " + gr.hashCode());
		
		File f = new File(path);
		
		List<File> lof = Arrays.asList(f.listFiles());
		ArrayList<File> alof = new ArrayList<File>();
		ArrayList<String> names = new ArrayList<String>();
		for(File fi : lof){
			if(fi.isFile() && !fi.isHidden()){
			    	System.out.println("Path: " + fi.getAbsolutePath());
			    	System.out.println("Name: " + fi.getName());
				alof.add(fi);
				names.add(fi.getName());
			}
		}
//		File[] arlof = alof.toArray(new File[0]);
//		String[] gNames = names.toArray(new String[0]);
		
		for(int i = 0; i < alof.size(); i++){
		    	String p = alof.get(i).getAbsolutePath();
		    	System.out.println("Trying to read: " + p);
			Graph g = gr.read(p);
			writeGraphToFile(g, path + "/" + names.get(i));
			g = null;
		}
	}

	private static String writeGraphToFile(Graph g, String filename) {
		new GtnaGraphWriter().writeWithProperties(g, filename + ".gtna");
		return filename;
	}

	

	/**
	 * @param s
	 * @throws ParseException
	 */
	private static void matchArgument(String s) throws ParseException {

		// parse network generation details
		if (s.startsWith("dir=")) {
			String f = s.substring(4);
			File ff = new File(f);
			if(!ff.exists() || !ff.isDirectory()){
				System.out.println("File " + f + " is not readable.");
			}
			path = f;
		} else if (s.startsWith("type=")) {
			if(s.equalsIgnoreCase("type=snap")){
				setSnap();
			}else if(s.equalsIgnoreCase("type=caida")){
				setCaida();
			} else {
				System.out.println("type must be one of {snap, caida}");
			}
		}
	}

	/**
	 * 
	 */
	private static void setCaida() {
		caida = true;
		snap = false;
	}

	/**
	 * 
	 */
	private static void setSnap() {
		snap = true;
		caida = false;		
	}
}

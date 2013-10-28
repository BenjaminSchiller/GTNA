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

import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.Clique;
import gtna.networks.model.CondonAndKarp;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.Regular;
import gtna.networks.model.WattsStrogatz;
import gtna.networks.model.ZhouMondragon;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Tim
 * 
 */
public class WFNetworkGen {

    private enum EnumNetworks {
	ER, BA, CK, RC, WS, REG, CL, RING
    };

    private static EnumNetworks net;

    private static int size = 0;
    private static int startIndex = 1;
    private static int endIndex = 1;
    private static boolean uni = true;
    private static String dir;

    private static int degree = 10;

    private static boolean ring = false;

    private static double avgdegree = 10.0;

    private static int edgespernode = 10;

    private static int numberofcommunities = 5;

    private static double pin = 0.005;

    private static double pout = 0.0005;

    private static int successors = 10;

    private static double p = 0.3;

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {

	if (args.length == 0
		|| (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
	    printHelp();
	    System.exit(0);
	}

	for (String s : args) {
	    matchArgument(s);
	}

	Network n = instantiateNetwork();
	String wn;	
	for(int i = startIndex; i < endIndex; i++) {
	    wn = writeGraphToFile(n.generate(), dir + size + "/" + net.toString() + "/" + net.toString() + "_" + i);
	    System.out.println(wn);
	}

	System.out.println("Built network(s): " + net + ", size= " + size
		+ " (Seq: " + startIndex + " - " + endIndex + ")");

    }
    
    private static String writeGraphToFile(Graph g, String filename) {
	new GtnaGraphWriter().writeWithProperties(g, filename+".gtna");
	return filename;
}

    /**
     * @return 
     * 
     */
    private static Network instantiateNetwork() {
	Network n;
	switch (net) {
	case BA:
	    if (size == 0 || edgespernode == 0)
		throw new IllegalArgumentException(
			"For initializing a Barabasi-Albert network the parameters size and edgespernode have to be set!");
	    n = new BarabasiAlbert(size, edgespernode, null);
	    break;
	case CK:
	    if (size == 0 || numberofcommunities == 0 || pin == 0.0
		    || pout == 0.0)
		throw new IllegalArgumentException(
			"For initializing a community network the parameters size, numberofcommunities, pin and pout have to be set!");
	    n = new CondonAndKarp(size, numberofcommunities, pin, pout, null);
	    break;
	case ER:
	    if (size == 0 || avgdegree == 0.0)
		throw new IllegalArgumentException(
			"For initializing a Erdos-Renyi network the parameters size and avgdegree have to be set!");
	    n = new ErdosRenyi(size, avgdegree, uni, null);
	    break;
	case RC:
	    n = new ZhouMondragon(size, p, edgespernode, null);
	    break;
	case REG: 
	    if(size == 0 || degree == 0) throw new IllegalArgumentException("For initializing a regular network the parameters size and degree have to be set!");
	    n = new Regular(size, degree, false, !uni, null);
	    break;
	case RING: 
	    if(size == 0 || degree == 0) throw new IllegalArgumentException("For initializing a regular network the parameters size and degree have to be set!");
	    n = new Regular(size, degree, true, !uni, null);
	    break;
	case WS:
	    if(size == 0 || successors == 0 || p == 0.0) 
		throw new IllegalArgumentException("For initializing a Watts-Strogatz network the parameters size, successors and p have to be set!");
	    n = new WattsStrogatz(size, successors, p, null);
	    break;
	case CL:
	    if(size == 0)
		throw new IllegalArgumentException("For initializing a Clique network the parameter size haS to be set!");
	    n = new Clique(size, null);
	    break;
	default:
	    throw new IllegalArgumentException("Network type " + net.toString() + " is not known!");
	}
	
	
	return n;
    }

    /**
     * @param s
     * @throws ParseException
     */
    private static void matchArgument(String s) throws ParseException {

	// parse network generation details
	if (s.startsWith("network=")) {
	    String sn = s.substring(8);
	    net = matchNetwork(sn);
	} else if (s.startsWith("size=")) {
	    size = Integer.parseInt(s.substring(5));
	} else if (s.startsWith("p=")) {
	    p = Double.parseDouble(s.substring(2));
	} else if (s.startsWith("pin=")) {
	    pin = Double.parseDouble(s.substring(4));
	} else if (s.startsWith("pout=")) {
	    pout = Double.parseDouble(s.substring(5));
	} else if (s.startsWith("avgdegree=")) {
	    avgdegree = Double.parseDouble(s.substring(10));
	} else if (s.startsWith("degree=")) {
	    degree = Integer.parseInt(s.substring(7));
	} else if (s.startsWith("successors=")) {
	    successors = Integer.parseInt(s.substring(11));
	} else if (s.startsWith("edgespernode=")) {
	    edgespernode = Integer.parseInt(s.substring(13));
	} else if (s.startsWith("numberofcommunities=")) {
	    numberofcommunities = Integer.parseInt(s.substring(20));
	} else if (s.startsWith("bidirectional=")) {
	    if (s.equalsIgnoreCase("bidirectional=true")) {
		uni = false;
	    } else {
		uni = true;
	    }
	} else if (s.startsWith("ring=")) {
	    if (s.equalsIgnoreCase("ring=true")) {
		ring = true;
	    } else {
		ring = false;
	    }
	} else if (s.startsWith("dir=")) {
	    dir = s.substring(4);
	    File f = new File(dir);
	    if (!f.isDirectory()) {
		f.mkdirs();
	    }
	} else if (s.startsWith("seq=")) {
	    String seq = s.substring(4);
	    String[] se = seq.split("-");
	    startIndex = Integer.parseInt(se[0]);
	    endIndex = Integer.parseInt(se[1]);
	} 
    }

    /**
     * @param sn
     * @return
     */
    private static EnumNetworks matchNetwork(String sn) {

	for (EnumNetworks n : EnumNetworks.values()) {
	    if (sn.equalsIgnoreCase(n.toString())) {
		return n;
	    }
	}
	System.out.println("Network unknown!");
	System.exit(1);
	return null;
    }

    private static void printHelp() {
	System.out
		.println("Usage:"
			+ "randomSeed=<dd.mm.yyyy> \n"
			+ "network=<synthetic network> size=<number of nodes> p1=<network_probability1> p2=<network_probability2> p3=<network_probability3> bidirectional=<true/false> \n"
			+ "dir=directory"
			+ "seq=startIndex-endIndex \n"
			+ "(all arguments, except p1/p2/p3, are mandatory, for some types of networks the arguments p1-3 have to be provided too)");
    }

}

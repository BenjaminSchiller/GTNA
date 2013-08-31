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

import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.CondonAndKarp;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.Regular;
import gtna.networks.model.WattsStrogatz;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ColorSampledSubgraph;
import gtna.transformation.sampling.subgraph.ColoredHeatmapSampledSubgraph;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Tim
 * 
 */
public class WFMetricCalc {
    	
    	private static Collection<Metric> metrics = new ArrayList<Metric>();
	private static String dir;
    
    	
    	
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("help")){
				printHelp();
				System.exit(0);
			}
		}
		
		for (String s : args) {
			
			if (s.equalsIgnoreCase("DD")) {
			    metrics.add(new DegreeDistribution());
			} else if(s.equalsIgnoreCase("CC")) {
			    metrics.add(new DegreeDistribution());
			}else if(s.equalsIgnoreCase("HP")) {
			    metrics.add(new DegreeDistribution());
			}else if(s.equalsIgnoreCase("DIAM")) {
			    metrics.add(new DegreeDistribution());
			}else if(s.equalsIgnoreCase("ECC")) {
			    metrics.add(new DegreeDistribution());
			}else if(s.equalsIgnoreCase("BC")) {
			    metrics.add(new DegreeDistribution());
			}else if(s.equalsIgnoreCase("PR")) {
			    metrics.add(new DegreeDistribution());
			}else if(s.equalsIgnoreCase("ASS")) {
			    metrics.add(new DegreeDistribution());
			}
			// readable folder?
			else if (s.startsWith("loadDir=")){
				dir = s.substring(8);
			} else {
				printHelp();
				System.exit(0);
			}
		}

	}

	private static void printHelp() {
		System.out.println("Usage:"
				+ "sampling=<samplingalgorithm> dimension=<how many walker> scaledown=<sample size in percentage> revisiting=<true/false> randomSeed=<dd.mm.yyyy>"
				+ "loadDir=<directory with prepared networks>"
				+ "network=<synthetic network> size=<number of nodes> p1=<network_probability1> p2=<network_probability2> bidirectional=<true/false>");
	}

}

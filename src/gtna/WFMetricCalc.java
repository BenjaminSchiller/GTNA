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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

/**
 * @author Tim
 * 
 */
public class WFMetricCalc {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		SamplingAlgorithm alg;
		int dim, size;
		boolean rev, uni;
		double sd, seed, p1, p2;
		String dir;
		
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("help")){
				printHelp();
				System.exit(0);
			}
		}
		
		for (String s : args) {
			
			if (s.equalsIgnoreCase("DD")) {
				//TODO instantiate metric
			} else if(s.equalsIgnoreCase("CC")) {
			    // TODO instantiate metric
			}else if(s.equalsIgnoreCase("HP")) {
			    // TODO instantiate metric
			}else if(s.equalsIgnoreCase("DIAM")) {
			    // TODO instantiate metric
			}else if(s.equalsIgnoreCase("ECC")) {
			    // TODO instantiate metric
			}else if(s.equalsIgnoreCase("BC")) {
			    // TODO instantiate metric
			}else if(s.equalsIgnoreCase("PR")) {
			    // TODO instantiate metric
			}else if(s.equalsIgnoreCase("ASS")) {
			    // TODO instantiate metric
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

	private static Transformation[] instantiateSamplingTransformation(
			SamplingAlgorithm alg, double scaledown, int dimension,
			boolean revisiting, Long randomSeed, String subgraphing) {
		Transformation samplingalgorithm = SamplingAlgorithmFactory
				.getInstanceOf(alg, scaledown, revisiting, dimension,
						randomSeed);

		Transformation sg;
		if (subgraphing.equalsIgnoreCase("subgraph")) {
			sg = new ExtractSampledSubgraph();
		} else if (subgraphing.equalsIgnoreCase("coloring")) {
			sg = new ColorSampledSubgraph();
		} else if (subgraphing.equalsIgnoreCase("heatmap")) {
			sg = new ColoredHeatmapSampledSubgraph();
		} else {
			throw new IllegalArgumentException(
					"sg must be one of {subgraph, coloring, heatmap}");
		}
		Transformation[] t1 = new Transformation[] { samplingalgorithm, sg };
		return t1;
	}
	
	public static Network[] instantiateNetworkModels() {
		Network nw1 = new ErdosRenyi(100, 3, false, null);
		Network nw2 = new BarabasiAlbert(500, 2, null);
		Network nw3 = new WattsStrogatz(500, 6, 0.2, null);
		Network nw4 = new CondonAndKarp(500, 3, 0.05, 0.0005, null);
		Network nw5 = new Regular(100, 2, true, false, null);

//		 Network[] n = new Network[] { nw1, nw2, nw3, nw4, nw5 };
//		Network[] n = new Network[] { nw2, nw3, nw4, nw5 };

		Network[] n = new Network[] { nw5 };
		return n;
	}

}

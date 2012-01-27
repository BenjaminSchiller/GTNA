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
 * Test.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.canonical.Ring;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.smallWorld.Kleinberg;
import gtna.networks.model.smallWorld.Kleinberg1D;
import gtna.networks.model.smallWorld.Kleinberg1DC;
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedyVariations.BacktrackGreedy;
import gtna.routing.greedyVariations.DepthFirstEdgeGreedy;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.routing.greedyVariations.DistRestrictEdgeGreedy;
import gtna.routing.greedyVariations.DistRestrictGreedy;
import gtna.routing.greedyVariations.FactorRestrictEdgeGreedy;
import gtna.routing.greedyVariations.FactorRestrictGreedy;
import gtna.routing.greedyVariations.GravityPressureRouting;
import gtna.routing.greedyVariations.KWorseGreedy;
import gtna.routing.greedyVariations.OneWorseGreedy;
import gtna.routing.greedyVariations.PureGreedy;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.attackableEmbedding.swapping.Swapping;
import gtna.util.Config;

/**
 * @author stef
 *
 */
public class Test {
	
	public static void main(String[] args){
//		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, CLUSTERING_COEFFICIENT, SHORTEST_PATHS ");
////		int[] sizes = {1000,10000,100000};
////		int[] C = {1,5,10};
//		int[] sizes = {20000,30000, 400000, 50000, 60000, 70000, 80000, 90000};
//		int[] C = {5};
//		for (int i = 0; i < sizes.length; i++){
//			for (int j = 0; j < C.length; j++){
//				testKleinberg1C(sizes[i], C[j]);
//			}
//		}
		Config.overwrite("METRICS", "ROUTING");
		int size = Integer.parseInt(args[0]);
		int C = Integer.parseInt(args[1]);
		int iter = Integer.parseInt(args[2]);
		testKWorse(size, C, iter);
	}
	
	private static void testEmbedding(){
		Transformation[] t = {new Swapping(1000), new Swapping(1000,0.001, Swapping.ATTACK_KLEINBERG, Swapping.ATTACKER_SELECTION_LARGEST, 1)};
		Network barabasi = new BarabasiAlbert(200,2,null,t);
		Series.generate(barabasi, 1);
		
		Transformation[] tLMC = {new LMC(1000,LMC.MODE_UNRESTRICTED, 0,""+ 0.001, 1), new LMC(1000,LMC.MODE_UNRESTRICTED, 0,""+ 0.001, 1)};
		Network barabasLMC = new BarabasiAlbert(200,2,null,tLMC);
		Series.generate(barabasLMC, 1);
	}
	
	private static void testRouting(){
		RoutingAlgorithm[] ra = {new PureGreedy(50), new BacktrackGreedy(50), new DepthFirstEdgeGreedy(50), new DepthFirstGreedy(50), new DistRestrictEdgeGreedy(50),
				new DistRestrictGreedy(50), new FactorRestrictEdgeGreedy(50), new FactorRestrictGreedy(50), new GravityPressureRouting(50), new OneWorseGreedy(50)};
		for (int i = 0; i < ra.length; i++){
			Network barabasi = new BarabasiAlbert(200,2,ra[i],null);
			Series.generate(barabasi, 1);
		}
	}
	
	private static void testKWorse(int size, int C, int iter){
		RoutingAlgorithm ra = new KWorseGreedy(2);
		Network kleinbergC = new Kleinberg1DC(size,1,1,1,true,true,C,ra,null);
		Series.generate(kleinbergC, iter);
	}

	private static void testSmallWorld(){
		Network kleinberg = new Kleinberg(10,3,1,1,3,true,true,null,null);
		Series.generate(kleinberg, 1);
		Network kleinberg1 = new Kleinberg1D(10,1,1,1,true,true,null,null);
		Series.generate(kleinberg1, 1);
		Network kleinbergC = new Kleinberg1DC(10,1,1,3,true,true,2,null,null);
		Series.generate(kleinbergC, 1);
		Network scalefree = new ScaleFreeUndirected(10,3,1,10,null,null);
		Series.generate(scalefree, 1);
	}
	
	private static void testKleinberg1C(int size, int C){
		
		Network kleinbergC = new Kleinberg1DC(size,1,1,1,true,true,C,null,null);
		Series.generate(kleinbergC, 5);
		
	}
	
	private static void testMotifs(){
		Network network = new Ring(4, null, null);
		Series.generate(network, 1);
	}
	
	private static void testFailures(){
		//Transformation[] t = {new LargestFailure(5), new RandomFailure(5)};
		Network net = new BarabasiAlbert(300,2,null,null);
		Series.generate(net, 1);
	}

}

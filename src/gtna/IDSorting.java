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
 * IDSorting.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-12 : v1 (BS)
 *
 */
package gtna;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.io.GraphReader;
import gtna.io.GraphWriter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.GreedyNextBestBacktracking;
import gtna.transformation.Transformation;
import gtna.transformation.identifier.LocalMC;
import gtna.transformation.identifier.RandomID;
import gtna.transformation.identifier.SwappingSorting;
import gtna.transformation.identifier.attackOld.SwappingAttack;
import gtna.transformation.identifier.attackOld2.LMCAttackCloseAll;
import gtna.transformation.identifier.attackOld2.LMCAttackCloseOne;
import gtna.transformation.identifier.attackOld2.LMCAttackRandomAll;
import gtna.transformation.sorting.lmc.LMC;
import gtna.util.Config;
import gtna.util.Timer;

import java.io.File;
import java.sql.Time;
import java.util.HashMap;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class IDSorting {
	public static void main(String[] args) {
		if (true) {
			Graph g = (new ErdosRenyi(10, 5, true, null, null)).generate();
			Transformation r = new RandomID(RandomID.RING_NODE, 0);
			Transformation lmc = new LMC(12, LMC.MODE_X, 0.5, LMC.DELTA_1_N,
					10, false, LMC.ATTACK_CONTRACTION_WC, 3);
			g = r.transform(g);
			g = lmc.transform(g);

			return;
		}

		Config.overwrite("MAIN_DATA_FOLDER", "./data/" + name + "/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/" + name + "/");
		Config.overwrite("METRICS", metrics);
		if (JPEG) {
			Config.overwrite("PLOT_EXTENSION", ".jpg");
			Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
		}

		Transformation r = new RandomID(RandomID.RING_NODE, 0);

		Transformation lmc, ca1, ca10, ca100, co1, co10, co100, ra1, ra10, ra100, sw, swa1, swa2, swa3, swa10;
		lmc = new LocalMC(IT_LMC, M, P, D, C, DEG1, null);
		ca1 = new LMCAttackCloseAll(IT_LMC_A, M, P, D, C, DEG1, null, 1);
		ca10 = new LMCAttackCloseAll(IT_LMC_A, M, P, D, C, DEG1, null, 10);
		ca100 = new LMCAttackCloseAll(IT_LMC_A, M, P, D, C, DEG1, null, 100);
		co1 = new LMCAttackCloseOne(IT_LMC_A, M, P, D, C, DEG1, null, 1);
		co10 = new LMCAttackCloseOne(IT_LMC_A, M, P, D, C, DEG1, null, 10);
		co100 = new LMCAttackCloseOne(IT_LMC_A, M, P, D, C, DEG1, null, 100);
		ra1 = new LMCAttackRandomAll(IT_LMC_A, M, P, D, C, DEG1, null, 1);
		ra10 = new LMCAttackRandomAll(IT_LMC_A, M, P, D, C, DEG1, null, 10);
		ra100 = new LMCAttackRandomAll(IT_LMC_A, M, P, D, C, DEG1, null, 100);
		sw = new SwappingSorting(V, IT_SW);
		swa1 = new SwappingAttack(IT_SW_A, 1);
		swa2 = new SwappingAttack(IT_SW_A, 2);
		swa3 = new SwappingAttack(IT_SW_A, 3);
		swa10 = new SwappingAttack(IT_SW_A, 10);

		Transformation[] R, LMC, SW;
		R = new Transformation[] { r };
		LMC = new Transformation[] { r, lmc };
		SW = new Transformation[] { r, sw };

		Transformation[] CA1, CA10, CA100;
		CA1 = new Transformation[] { r, ca1 };
		CA10 = new Transformation[] { r, ca10 };
		CA100 = new Transformation[] { r, ca100 };

		Transformation[] CO1, CO10, CO100;
		CO1 = new Transformation[] { r, co1 };
		CO10 = new Transformation[] { r, co10 };
		CO100 = new Transformation[] { r, co100 };

		Transformation[] RA1, RA10, RA100;
		RA1 = new Transformation[] { r, ra1 };
		RA10 = new Transformation[] { r, ra10 };
		RA100 = new Transformation[] { r, ra100 };

		Transformation[] LMC_CA1, LMC_CA10, LMC_CA100;
		LMC_CA1 = new Transformation[] { ca1 };
		LMC_CA10 = new Transformation[] { ca10 };
		LMC_CA100 = new Transformation[] { ca100 };

		Transformation[] LMC_CO1, LMC_CO10, LMC_CO100;
		LMC_CO1 = new Transformation[] { co1 };
		LMC_CO10 = new Transformation[] { co10 };
		LMC_CO100 = new Transformation[] { co100 };

		Transformation[] LMC_RA1, LMC_RA10, LMC_RA100;
		LMC_RA1 = new Transformation[] { ra1 };
		LMC_RA10 = new Transformation[] { ra10 };
		LMC_RA100 = new Transformation[] { ra100 };

		Transformation[] SWA1, SWA2, SWA3, SWA10;
		SWA1 = new Transformation[] { r, swa1 };
		SWA2 = new Transformation[] { r, swa2 };
		SWA3 = new Transformation[] { r, swa3 };
		SWA10 = new Transformation[] { r, swa10 };

		Transformation[] SW_SWA1, SW_SWA2, SW_SWA3, SW_SWA10;
		SW_SWA1 = new Transformation[] { swa1 };
		SW_SWA2 = new Transformation[] { swa2 };
		SW_SWA3 = new Transformation[] { swa3 };
		SW_SWA10 = new Transformation[] { swa10 };

		HashMap<Transformation[], String> names = new HashMap<Transformation[], String>();
		names.put(R, "Random");
		names.put(LMC, "LMC");
		names.put(SW, "Swapping");

		names.put(CA1, "CloseAll-1");
		names.put(CA10, "CloseAll-10");
		names.put(CA100, "CloseAll-100");
		names.put(CO1, "CloseOne-1");
		names.put(CO10, "CloseOne-10");
		names.put(CO100, "CloseOne-100");
		names.put(RA1, "RandomAll-1");
		names.put(RA10, "RandomAll-10");
		names.put(RA100, "RandomAll-100");

		names.put(LMC_CA1, "LMC-CloseAll-1");
		names.put(LMC_CA10, "LMC-CloseAll-10");
		names.put(LMC_CA100, "LMC-CloseAll-100");
		names.put(LMC_CO1, "LMC-CloseOne-1");
		names.put(LMC_CO10, "LMC-CloseOne-10");
		names.put(LMC_CO100, "LMC-CloseOne-100");
		names.put(LMC_RA1, "LMC-RandomAll-1");
		names.put(LMC_RA10, "LMC-RandomAll-10");
		names.put(LMC_RA100, "LMC-RandomAll-100");

		names.put(SWA1, "SWA-1");
		names.put(SWA2, "SWA-2");
		names.put(SWA3, "SWA-3");
		names.put(SWA10, "SWA-10");

		names.put(SW_SWA1, "SW-SWA-1");
		names.put(SW_SWA2, "SW-SWA-2");
		names.put(SW_SWA3, "SW-SWA-3");
		names.put(SW_SWA10, "SW-SWA-10");

		Transformation[][] basic = new Transformation[][] { R, LMC, SW };
		Transformation[][] ca = new Transformation[][] { CA1, CA10, CA100 };
		Transformation[][] co = new Transformation[][] { CO1, CO10, CO100 };
		Transformation[][] ra = new Transformation[][] { RA1, RA10, RA100 };
		Transformation[][] lmc_ca = new Transformation[][] { LMC_CA1, LMC_CA10,
				LMC_CA100 };
		Transformation[][] lmc_co = new Transformation[][] { LMC_CO1, LMC_CO10,
				LMC_CO100 };
		Transformation[][] lmc_ra = new Transformation[][] { LMC_RA1, LMC_RA10,
				LMC_RA100 };
		Transformation[][] swa = new Transformation[][] { SWA1, SWA2, SWA3,
				SWA10 };
		Transformation[][] sw_swa = new Transformation[][] { SW_SWA1, SW_SWA2,
				SW_SWA3, SW_SWA10 };

		int times = 50;
		int maxTimes = 50;

		// generateGraphFromSPI(basic, times, names);
		// generateGraphFromSPI(ca, times, names);
		// generateGraphFromSPI(co, times, names);
		// generateGraphFromSPI(ra, times, names);
		// generateGraphFrom(lmc_ca, times, names, LMC);
		// generateGraphFrom(lmc_co, times, names, LMC);
		// generateGraphFrom(lmc_ra, times, names, LMC);
		// generateGraphFromSPI(swa, times, names);
		// generateGraphFrom(sw_swa, times, names, SW);

		// Transformation[][] T_RA = new Transformation[][] { R, LMC, SW, RA100,
		// LMC_RA100 };
		// Series[] S_RA = generateData(T_RA, maxTimes, names);
		// Plot.multiAvg(S_RA, "RA100/");

		// Transformation[][] T_SW = new Transformation[][] { SW, SWA1, SW_SWA1
		// };
		// Series[] S_SW = generateData(T_SW, maxTimes, names);
		// Plot.multiAvg(S_SW, "SW100/");

		Transformation[][] T_SW = new Transformation[][] { LMC, RA100,
				LMC_RA100, SW, SWA1, SW_SWA1 };
		Series[] S_SW = generateData(T_SW, maxTimes, names);
		Plot.multiAvg(S_SW, "100/");

		//
		// // Transformation[][] T = new Transformation[][] { LMC, CA1, CA10,
		// // CA100,
		// // CO1, CO10, CO100, RA1, RA10, RA100, SW, SWA1, R };
		// Transformation[][] T = new Transformation[][] { SWA1, SWA2, SWA3,
		// SWA10 };
		//

		// Network[] NW = new Network[T.length * R.length];
		// int index = 0;
		// for (int i = 0; i < T.length; i++) {
		// for (int j = 0; j < RA.length; j++) {
		// Network rf = new ReadableFile("SPI", "spi", file,
		// GraphReader.OWN_FORMAT, RA[j], T[i]);
		// NW[index++] = new DescriptionWrapper(rf, names.get(T[i]) + "-"
		// + R[j].folder());
		// }
		// }

	}

	private static final String SPI = "./resources/SPI-3-LCC/2010-08.spi.txt";

	private static final int IT_LMC = 2000;
	private static final int IT_LMC_A = 2000;
	private static final int IT_SW = 10000;
	private static final int IT_SW_A = 10000;

	private static final int TTL = 174;

	private static final int C = 10;
	private static final double D = -1;
	private static final double P = 0.5;
	private static final boolean DEG1 = false;
	private static final int M = 1;
	private static final int V = 1;

	private static final boolean TEST = false;

	private static final boolean GET = true;

	private static final boolean JPEG = true;

	private static final boolean PLOT_EACH = false;

	private static final RoutingAlgorithm RA = new GreedyNextBestBacktracking(
			TTL);

	private static final String name = "dataGeneration-" + RA.folder();

	// ID_SPACE_DISTANCES, ID_SPACE_HOPS, RL
	// ID_SPACE
	private static final String metrics = "ID_SPACE_DISTANCES, ID_SPACE_HOPS, RL";

	public static String graphFolder(Transformation[] ts,
			HashMap<Transformation[], String> names) {
		return "data/graphs/" + names.get(ts) + "/";
	}

	public static Series[] generateData(Transformation[][] tss, int maxTimes,
			HashMap<Transformation[], String> names) {
		Series[] s = new Series[tss.length];
		for (int i = 0; i < tss.length; i++) {
			s[i] = generateData(tss[i], maxTimes, names);
		}
		return s;
	}

	public static Series generateData(Transformation[] ts, int maxTimes,
			HashMap<Transformation[], String> names) {
		ReadableFolder nw1 = new ReadableFolder(names.get(ts), names.get(ts),
				graphFolder(ts, names), GraphReader.RING_NODES, RA, null);
		Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
		int times = Math.min(maxTimes, nw1.getFiles().length);
		System.out.println("DATA: " + names.get(ts) + " (" + times + ")");
		if (!TEST) {
			Series s = GET ? Series.get(nw2) : Series.generate(nw2, times);
			if (PLOT_EACH) {
				Plot.multiAvg(s, "_" + names.get(ts) + "/");
			}
			return s;
		} else {
			return null;
		}
	}

	public static void generateGraphFromSPI(Transformation[][] tss, int times,
			HashMap<Transformation[], String> names) {
		for (Transformation[] ts : tss) {
			generateGraphFromSPI(ts, times, names);
		}
	}

	public static void generateGraphFromSPI(Transformation[] ts, int times,
			HashMap<Transformation[], String> names) {
		System.out.println("\nGRAPHS: " + names.get(ts));
		for (int i = 0; i < times; i++) {
			Timer timer = new Timer("  " + i + " @ "
					+ (new Time(System.currentTimeMillis())).toString());
			Network nw1 = new ReadableFile("SPI", "spi", SPI,
					GraphReader.OWN_FORMAT, null, ts);
			Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
			String filename = graphFolder(ts, names) + i + ".txt";
			if (!(new File(filename)).exists()) {
				if (!TEST) {
					Graph g = nw2.generate();
					for (Transformation t : ts) {
						g = t.transform(g);
					}
					GraphWriter
							.write(g, filename, GraphWriter.TO_STRING_FORMAT);
				}
				System.out.print("            ");
			} else {
				System.out.print("  [exists]  ");
			}
			timer.end();
		}
	}

	public static void generateGraphFrom(Transformation[][] tss, int times,
			HashMap<Transformation[], String> names, Transformation[] from) {
		for (Transformation[] ts : tss) {
			generateGraphFrom(ts, times, names, from);
		}
	}

	public static void generateGraphFrom(Transformation[] ts, int times,
			HashMap<Transformation[], String> names, Transformation[] from) {
		System.out.println("\nGRAPHS " + names.get(ts) + " from "
				+ names.get(from));
		for (int i = 0; i < times; i++) {
			Timer timer = new Timer("  " + i + " @ "
					+ (new Time(System.currentTimeMillis())).toString());
			String src = graphFolder(from, names) + i + ".txt";
			if (!(new File(src)).exists()) {
				System.out.print("  [skipping]");
			} else {
				Network nw1 = new ReadableFile(names.get(from),
						names.get(from), src, GraphReader.RING_NODES, null, ts);
				Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
				String filename = graphFolder(ts, names) + i + ".txt";
				if (!(new File(filename)).exists()) {
					if (!TEST) {
						Graph g = nw2.generate();
						for (Transformation t : ts) {
							g = t.transform(g);
						}
						GraphWriter.write(g, filename,
								GraphWriter.TO_STRING_FORMAT);
					}
					System.out.print("            ");
				} else {
					System.out.print("  [exists]  ");
				}
			}
			timer.end();
		}
	}
}

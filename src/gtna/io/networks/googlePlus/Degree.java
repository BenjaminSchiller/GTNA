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
 * Degree.java
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
package gtna.io.networks.googlePlus;

import gtna.util.Distribution;

import java.io.File;
import java.util.HashSet;

/**
 * @author benni
 * 
 */
public class Degree {
//	public static Distribution[] computeDDInSubGraph(Crawl crawl,
//			Mapping mapping) {
//		int[] d = new int[mapping.getMap().size()];
//		int[] in = new int[mapping.getMap().size()];
//		int[] out = new int[mapping.getMap().size()];
//		HashSet<String> edges = new HashSet<String>();
//		for (File node : crawl.getNodeList()) {
//			String[] temp = node.getName().split("-");
//			int tid = Integer.parseInt(temp[0]);
//			int tlid = Integer.parseInt((new File(node.getParent()).getName()));
//			String u_id = temp[1];
//			Task task = new Task(tid, crawl.getCid(), tlid, u_id, 0, 0);
//			Node n = Node.read(node.getAbsolutePath(), task);
//
//			if (n.getTask().getU_id().length() != 21) {
//				continue;
//			}
//			if (!mapping.getMap().containsKey(n.getTask().getU_id())) {
//				continue;
//			}
//			String id = n.getTask().getU_id();
//			int index = mapping.getMap().get(id);
//
//			for (User o : n.getOut()) {
//				if (o.getId().length() != 21) {
//					continue;
//				}
//				if (!mapping.getMap().containsKey(o.getId())) {
//					continue;
//				}
//				String edge = id + "_" + o.getId();
//				if (!edges.contains(edge)) {
//					edges.add(edge);
//					out[index]++;
//					int indexO = mapping.getMap().get(o.getId());
//					in[indexO]++;
//				}
//			}
//			for (User i : n.getIn()) {
//				if (i.getId().length() != 21) {
//					continue;
//				}
//				if (!mapping.getMap().containsKey(i.getId())) {
//					continue;
//				}
//				String edge = id + "_" + i.getId();
//				if (!edges.contains(edges)) {
//					edges.add(edge);
//					in[index]++;
//					int indexI = mapping.getMap().get(i.getId());
//					out[indexI]++;
//				}
//			}
//		}
//		Distribution DD = new Distribution(Degree.computeFrequency(d));
//		Distribution DDI = new Distribution(Degree.computeFrequency(in));
//		Distribution DDO = new Distribution(Degree.computeFrequency(out));
//		return new Distribution[] { DD, DDI, DDO };
//	}
//
//	private static double[] computeFrequency(int[] degree) {
//		int max = 0;
//		for (int d : degree) {
//			if (d > max) {
//				max = d;
//			}
//		}
//		double[] frequency = new double[max + 1];
//		for (int d : degree) {
//			frequency[d]++;
//		}
//		return frequency;
//	}
//
//	public static double[] computeDDInCompleteGraph(Crawl crawl, Mapping mapping) {
//		
//	}
}

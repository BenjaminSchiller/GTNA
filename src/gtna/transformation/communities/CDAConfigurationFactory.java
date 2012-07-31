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
 * CDAConfigurationFactory.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.communities;

import gtna.transformation.Transformation;

/**
 * The <code>CDAConfigurationFactory</code> is a simple class that creates and
 * returns an array containing a lot of different configurations for the
 * different CD Algorithms.
 * 
 * 
 * @author Philipp Neubrand
 * 
 */
public class CDAConfigurationFactory {


	public static Transformation[] getConfigArray() {

		// Different NodePickers
		NodePicker[] nps = new NodePicker[] { new RandomNodePicker() };

		// different THresholds for the SimilarityMeasures
		double[] th = new double[] { 0.01, 0.1, 0.25, 0.5, 0.9 };
		
		// different similarity measures
		SimilarityMeasure[] sms = new SimilarityMeasure[] {
				new EqualityFractionMeasure(), new CosineMeasure(),
				new DiffNumberMeasure() };
		
		SimilarityMeasureContainer[] smcs = new SimilarityMeasureContainer[th.length
				* sms.length];
		int i = 0;
		for (double akt : th) {
			for (SimilarityMeasure sm : sms) {
				smcs[i] = new SimilarityMeasureContainer(sm, akt);
				i++;
			}
		}
		
		int[] rwcs = new int[]{5, 10, 20, 50};

		// Different alpha values for CDExpandingSHells, 1.9 was used by the original authors for Karate
		double[] expShellAlphas = new double[] { 0.5, 0.8, 0.9, 1, 1.9, 5 };
		
		Transformation[] ret = new Transformation[1 + 2
				+ (expShellAlphas.length * nps.length)
				+ (expShellAlphas.length * smcs.length) + nps.length * 2 * rwcs.length + smcs.length * 2 * rwcs.length];

		ret[0] = new CDDeltaQ("long", true, 0);
		ret[1] = new CDLPA(1);
		ret[2] = new CDLPA(10);
		int j = 3;
		for (NodePicker aktNP : nps) {
			for (double aktAlpha : expShellAlphas) {
				ret[j] = new CDExpandingSpheres(aktAlpha, aktNP);
				j++;
			}
		}

		for (SimilarityMeasureContainer aktSMC : smcs) {
			for (double aktAlpha : expShellAlphas) {
				ret[j] = new CDExpandingSpheres(aktAlpha, aktSMC);
				j++;
			}
		}
		
		ret[j] = new CDFastUnfolding();
		j++;
		
		for(NodePicker np : nps){
			for(int rwc : rwcs){
				ret[j] = new CDRandomWalk(rwc, true, np);
				ret[j+1] = new CDRandomWalk(rwc, false, np);
				j += 2;
			}
		}

		for(SimilarityMeasureContainer aktSMC : smcs){
			for(int rwc : rwcs){
				ret[j] = new CDRandomWalk(rwc, true, aktSMC);
				ret[j+1] = new CDRandomWalk(rwc, false, aktSMC);
				j += 2;
			}
		}
		return ret;
	}

}

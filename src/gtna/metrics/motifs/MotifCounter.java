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
 * MotifCounter.java
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
package gtna.metrics.motifs;

import gtna.data.Value;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.util.Timer;



/**
 * abstract class for counting motifs in a graph
 * @author stef
 *
 */
public abstract class MotifCounter extends Metric {
	protected double[] counts;
	protected Timer runtime;
	/**
	 * @param key
	 */
	public MotifCounter(String key) {
		super(key);
		// TODO Auto-generated constructor stub
	}

	

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.counts,
				this.name() + "_MOTIF_COUNT", folder);
		double sum = 0;
		for (int i = 0; i < this.counts.length; i++){
			sum = sum + this.counts[i];
		}
		double[] d = new double[this.counts.length];
		for (int i = 0; i < d.length; i++){
			d[i] = (double)counts[i]/sum;
		}
		success &= DataWriter.writeWithIndex(d,
				this.name() + "_MOTIF_DISTRIBUTION", folder);
		
		return success;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getValues()
	 */
	@Override
	public Value[] getValues() {
		return new Value[] {new Value(this.name() +"_RUNTIME",
				this.runtime.getRuntime())};
	}
	
	
	
	

}

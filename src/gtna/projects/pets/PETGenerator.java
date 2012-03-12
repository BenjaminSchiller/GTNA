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
 * Generator.java
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
package gtna.projects.pets;

import gtna.networks.Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author benni
 * 
 */
public abstract class PETGenerator extends Thread {
	protected ArrayList<Network> nw;

	protected int threads;

	protected int offset;

	protected HashMap<Integer, Integer> times;

	public PETGenerator(ArrayList<Network> nw, int threads, int offset,
			HashMap<Integer, Integer> times) {
		this.nw = nw;
		this.threads = threads;
		this.offset = offset;
		this.times = times;
	}

	protected abstract boolean process(Network nw);

	public void run() {
		int index = this.offset;
		LinkedList<Network> queue = new LinkedList<Network>();
		while (index < this.nw.size()) {
			queue.addLast(this.nw.get(index));
			index += this.threads;
		}
		int timesWaited = 0;
		while (!queue.isEmpty()) {
			Network nw = queue.pollFirst();
			boolean success = this.process(nw);
			if (!success) {
				queue.addLast(nw);
				try {
					timesWaited++;
					long waitTime = timesWaited * PET.waitTime;
					System.out.println(this.offset + ": waiting " + waitTime
							+ " msec (" + timesWaited + ")");
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

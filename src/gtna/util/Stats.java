/*
 * ===========================================================
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
 * Stats.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.util;

import gtna.io.Output;

public class Stats {
	private long startTime;

	private long endTime;

	private long totalTime;

	private long totalMemory;

	public Stats() {
		this.startTime = System.currentTimeMillis();
	}

	public void end() {
		this.endTime = System.currentTimeMillis();
		this.totalTime = this.endTime - this.startTime;
		this.totalMemory = Runtime.getRuntime().totalMemory();
		String out = Config.get("STATS");
		out = out.replace("%MSEC", this.totalTime + "");
		out = out.replace("%SEC", (this.totalTime / 1000) + "");
		out = out.replace("%MEM", (this.totalMemory / (1024 * 1024)) + "");
		Output.writeln(out);
		// if (this.totalTime > 10000) {
		// String filename = "/System/Library/Sounds/Glass.aiff";
		// InputStream in;
		// try {
		// in = new FileInputStream(filename);
		// AudioStream as = new AudioStream(in);
		// AudioPlayer.player.start(as);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
	}
}

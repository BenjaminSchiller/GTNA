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
 * Timer.java
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

public class Timer {
	private long start;

	private long end = -1;

	String name;

	public Timer() {
		this.start = System.currentTimeMillis();
		this.name = null;
	}

	public Timer(String name) {
		this.start = System.currentTimeMillis();
		this.name = name;
		Output.write(name);
	}

	public void end() {
		end("");
	}

	public Timer(double duration) {
		this.start = 0;
		if (Config.get("TIMER_TYPE").equals("sec")) {
			this.end = 1000 * (long) duration;
		} else if (Config.get("TIMER_TYPE").equals("msec")) {
			this.end = (long) duration;
		} else {
			this.end = -1;
		}
	}

	public void end(String msg) {
		this.end = System.currentTimeMillis();
		if (this.name != null) {
			String out = Config.get("TIMER_END");
			out = out.replace("%MSG", msg);
			out = out.replace("%MSEC", getMsec() + "");
			out = out.replace("%SEC", getSec() + "");
			Output.writeln(out);
		}
	}

	public long getMsec() {
		if (this.end == -1) {
			return System.currentTimeMillis() - this.start;
		} else {
			return this.end - this.start;
		}
	}

	public long getSec() {
		return this.getMsec() / 1000;
	}

	public double getRuntime() {
		if (Config.get("TIMER_TYPE").equals("sec")) {
			return (double) this.getMsec() / (double) 1000;
		} else if (Config.get("TIMER_TYPE").equals("msec")) {
			return this.getMsec();
		} else {
			return -1;
		}
	}
}

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
 * CrawlDuration.java
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

import gtna.io.Filereader;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author benni
 * 
 */
public class CrawlDuration {
	private int cid;

	private long start;

	private long end;

	public CrawlDuration(int cid, long start, long end) {
		this.cid = cid;
		this.start = start;
		this.end = end;
	}

	public static ArrayList<CrawlDuration> read(String filename, String sep) {
		Filereader fr = new Filereader(filename);
		ArrayList<CrawlDuration> list = new ArrayList<CrawlDuration>();
		String line = null;
		while ((line = fr.readLine()) != null) {
			String[] crawl = line.split(sep);
			int cid = Integer.parseInt(crawl[0]);
			long start = Long.parseLong(crawl[1]);
			long end = Long.parseLong(crawl[2]);
			list.add(new CrawlDuration(cid, start, end));
		}
		fr.close();
		return list;
	}

	public String toString() {
		Date s = new Date(this.start);
		Date e = new Date(this.end);
		return this.getInternalID() + "/" + this.getCid() + " : "
				+ s.toString() + " / " + e.toGMTString();
	}

	public long timeBetween(CrawlDuration c) {
		if (this.getStart() > c.getStart()) {
			return this.getStart() - c.getEnd();
		} else {
			return c.getStart() - this.getEnd();
		}
	}

	public int getInternalID() {
		if (this.cid < 19) {
			return this.cid - 1;
		} else if (this.cid > 19) {
			return this.cid - 2;
		} else {
			return -1;
		}
	}

	public double timeBetweenH(CrawlDuration c) {
		return (double) this.timeBetween(c) / 1000 / 60 / 60;
	}

	public long getDuration() {
		return this.getEnd() - this.getStart();
	}

	public long getDurationH() {
		return this.getDuration() / 1000 / 60 / 60;
	}

	/**
	 * @return the cid
	 */
	public int getCid() {
		return this.cid;
	}

	/**
	 * @return the start
	 */
	public long getStart() {
		return this.start;
	}

	/**
	 * @return the end
	 */
	public long getEnd() {
		return this.end;
	}
}

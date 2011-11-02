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
 * Task.java
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


/**
 * @author benni
 *
 */
public class Task {
	private int tid;

	private int cid;

	private int tlid;

	private String u_id;

	private long start;

	private long end;

	private Node node;

	public Task(int tid, int cid, int tlid, String u_id, long start,
			long end) {
		this.tid = tid;
		this.cid = cid;
		this.tlid = tlid;
		this.u_id = u_id;
		this.start = start;
		this.end = end;
		this.node = null;
	}

	public Task(int cid, int tlid, String u_id, long start, long end) {
		this(0, cid, tlid, u_id, start, end);
	}

	public void start() {
		this.start = System.currentTimeMillis();
	}

	public void end() {
		this.end = System.currentTimeMillis();
	}

	public int getTid() {
		return this.tid;
	}

	public int getCid() {
		return this.cid;
	}

	public int getTlid() {
		return this.tlid;
	}

	public String getU_id() {
		return this.u_id;
	}

	public long getStart() {
		return this.start;
	}

	public long getEnd() {
		return this.end;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String toString() {
		return "T/" + this.cid + "/" + this.tlid + "/" + this.tid + "/"
				+ this.u_id;
	}
}

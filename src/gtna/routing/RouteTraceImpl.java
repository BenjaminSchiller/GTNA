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
 * RouteTraceImpl.java
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
package gtna.routing;

import java.util.ArrayList;

/**
 * @author benni
 * 
 */
public class RouteTraceImpl extends RouteImpl implements RouteTrace {
	private Message[] messages;

	public RouteTraceImpl(ArrayList<Message> messages,
			ArrayList<Integer> route, boolean successful) {
		super(route, successful);
		this.messages = new Message[messages.size()];
		for (int i = 0; i < messages.size(); i++) {
			this.messages[i] = messages.get(i);
		}
	}

	@Override
	public Message[] getMessages() {
		return this.messages;
	}

}

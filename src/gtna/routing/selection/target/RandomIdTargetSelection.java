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
 * RandomTargetSelection.java
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
package gtna.routing.selection.target;

import gtna.graph.Graph;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;

/**
 * @author benni
 * 
 */
public class RandomIdTargetSelection extends TargetSelection {

	protected IdentifierSpace identifierSpace;

	public RandomIdTargetSelection() {
		super("TARGET_SELECTION_RANDOM_ID");
	}

	public void init(Graph graph) {
		super.init(graph);
		this.identifierSpace = (IdentifierSpace) this.graph
				.getProperty("ID_SPACE_0");
	}

	@Override
	public Identifier getNextTarget() {
		return this.identifierSpace.getRandomIdentifier(this.rand);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0")
				&& graph.getProperty("ID_SPACE_0") instanceof IdentifierSpace;
	}

}

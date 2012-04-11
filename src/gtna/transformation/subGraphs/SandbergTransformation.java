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
 * SandbergTransformation.java
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
package gtna.transformation.subGraphs;

import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author stef
 * 
 *         Sandberg's Transformation as a special case of BuildSubGraph
 */
public class SandbergTransformation extends BuildSubGraph {

	/**
	 * @param include
	 *            :number of nodes in subgraph
	 * @param selection
	 *            : selection of first nodes
	 */
	public SandbergTransformation(int include, CliqueSelection selection) {
		super("SANDBERG",include,1,Integer.MAX_VALUE,1, selection,
				BuildSubGraph.NextSelection.MAX,
				new Parameter[] { new IntParameter("INCLUDE", include),
						new StringParameter("SELECTION", selection.toString()) });
	}

}

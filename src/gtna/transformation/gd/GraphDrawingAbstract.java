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
 * GraphDrawingAbstract.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.gd;

import gtna.drawing.GraphPlotter;
import gtna.graph.Graph;
import gtna.transformation.Transformation;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * @author Nico
 * 
 */
public abstract class GraphDrawingAbstract extends Transformation implements
		Cloneable {
	protected GraphPlotter graphPlotter;
	Random rand;

	public GraphDrawingAbstract(String key, Parameter[] parameters) {
		super(key, parameters);
		rand = new Random();
	}

	protected abstract void initIDSpace(Graph g);

	protected abstract void writeIDSpace(Graph g);

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	public abstract GraphDrawingAbstract clone();
}

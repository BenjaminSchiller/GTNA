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

import java.util.Random;

import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.plot.GraphPlotter;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

/**
 * @author Nico
 * 
 */
public abstract class GraphDrawingAbstract extends TransformationImpl implements Transformation, Cloneable {
	protected GraphPlotter graphPlotter;
	protected boolean generateIDSpace = true;	
	Random rand;
	
	public GraphDrawingAbstract(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
		rand = new Random();
	}

	protected abstract void initIDSpace(Graph g);

	protected abstract void writeIDSpace(Graph g);

	public abstract void setIDSpace(IdentifierSpace idSpace);
	
	@Override
	public boolean applicable(Graph g) {
		return true;
	}
	
	public abstract GraphDrawingAbstract clone();
}

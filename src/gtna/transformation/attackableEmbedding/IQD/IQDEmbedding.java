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
 * IQDEmbedding.java
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
package gtna.transformation.attackableEmbedding.IQD;

import gtna.transformation.attackableEmbedding.AttackableEmbedding;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author stef
 *
 */
public abstract class IQDEmbedding extends AttackableEmbedding {
	IdentifierMethod idMethod;
	DecisionMethod deMethod;
	double epsilon;
	boolean checkold;
	
	

	/**
	 * @param iterations
	 * @param key
	 * @param parameters
	 */
	public IQDEmbedding(int iterations, String key, IdentifierMethod idMethod, DecisionMethod deMethod,
	double epsilon, boolean checkold, Parameter[] parameters) {
		super(iterations, key, combineParameter(idMethod,deMethod,epsilon,checkold,parameters));
		this.idMethod = idMethod;
		this.deMethod = deMethod;
		this.epsilon = epsilon;
		this.checkold = checkold;
	}
	
	public IdentifierMethod getIdMethod() {
		return this.idMethod;
	}

	public DecisionMethod getDeMethod() {
		return this.deMethod;
	}

	public double getEpsilon() {
		return this.epsilon;
	}

	public boolean isCheckold() {
		return this.checkold;
	}

	private static Parameter[] combineParameter(IdentifierMethod idMethod, DecisionMethod deMethod,
			double epsilon, boolean checkold, Parameter[] parameters){
		Parameter[] res = new Parameter[parameters.length+4];
		res[0] = new StringParameter("IDENTIFER_METHOD", idMethod.toString());
		res[1] = new StringParameter("DECISION_METHOD", deMethod.toString());
		res[2] = new DoubleParameter("EPSILON", epsilon);
		res[3] = new BooleanParameter("CHECKOLD", checkold);
		for (int i = 4; i < res.length; i++){
			res[i] = parameters[i-4];
		}
		return res;
	}
	
	public static enum IdentifierMethod{
		ONERANDOM, TWORANDOM, RANDNEIGHBOR, ALLNEIGHBOR, ALLNEIGHBORMIDDLE
	}
	
	public static enum DecisionMethod{
		BESTPREFEROLD, BESTPREFERNEW, METROPOLIS, TEMPERATUR 
	}

	

}

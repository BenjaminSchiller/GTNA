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

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.DIdentifierSpace;
import gtna.id.Partition;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.AttackableEmbedding;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;

/**
 * @author stef 1D-embedding in three phases: identifier choice, quality
 *         evaluation, decision
 * 
 */
public abstract class IQDEmbedding extends AttackableEmbedding {

	public static enum IdentifierMethod {
		ONERANDOM, TWORANDOM, RANDNEIGHBOR, RANDNEIGHBORMIDDLE, ALLNEIGHBOR, ALLNEIGHBORMIDDLE, SWAPPING
	}

	public static enum DecisionMethod {
		BESTPREFEROLD, BESTPREFERNEW, METROPOLIS, PROPORTIONAL, SA, SATIMEDEPENDENT
	}

	IdentifierMethod idMethod;
	DecisionMethod deMethod;
	Distance distance;
	double epsilon;
	boolean checkold;
	RingIdentifier[] ids;
	boolean adjustOneDegree;

	/**
	 * 
	 * @param iterations
	 * @param key
	 * @param idMethod
	 *            : IdentifierMethod
	 * @param deMethod
	 *            : DecisionMethod
	 * @param distance
	 *            : RING, CLOCKWISE or SIGNED
	 * @param epsilon
	 *            : distance within which there should be one neighbor
	 * @param checkold
	 *            : check if condition of ID is fulfilled by current ID,
	 *            otherwise ndont considered it further
	 * @param adjustOne
	 *            : adjust nodes of degree 1 to neighbor
	 * @param parameters
	 * @add: are the parameters added to the others in the description, or
	 *       should the description only contain those
	 * 
	 */
	public IQDEmbedding(int iterations, String key, IdentifierMethod idMethod,
			DecisionMethod deMethod, Distance distance, double epsilon,
			boolean checkold, boolean adjustOne, Parameter[] parameters,
			boolean add) {
		super(iterations, key, add ? combineParameter(iterations, idMethod,
				deMethod, distance, epsilon, checkold, adjustOne, parameters)
				: parameters);
		this.idMethod = idMethod;
		this.deMethod = deMethod;
		this.distance = distance;
		this.epsilon = epsilon;
		this.checkold = checkold;
		this.adjustOneDegree = adjustOne;
	}

	public IQDEmbedding(int iterations, String key, IdentifierMethod idMethod,
			DecisionMethod deMethod, Distance distance, double epsilon,
			boolean checkold, boolean adjustOne, Parameter[] parameters) {
		super(iterations, key, combineParameter(iterations, idMethod, deMethod,
				distance, epsilon, checkold, adjustOne, parameters));
		this.idMethod = idMethod;
		this.deMethod = deMethod;
		this.distance = distance;
		this.epsilon = epsilon;
		this.checkold = checkold;
		this.adjustOneDegree = adjustOne;
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

	private static Parameter[] combineParameter(int iter,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			Distance distance, double epsilon, boolean checkold,
			boolean adjustone, Parameter[] parameters) {
		Parameter[] res = new Parameter[parameters.length + 7];
		res[0] = new IntParameter("ITERATIONS", iter);
		res[1] = new StringParameter("IDENTIFIER_METHOD", idMethod.toString());
		res[2] = new StringParameter("DECISION_METHOD", deMethod.toString());
		res[3] = new DoubleParameter("EPSILON", epsilon);
		res[4] = new BooleanParameter("CHECKOLD", checkold);
		res[5] = new StringParameter("DISTANCE", distance.toString());
		res[6] = new BooleanParameter("ADJUST_ONE", adjustone);
		for (int i = 7; i < res.length; i++) {
			res[i] = parameters[i - 7];
		}
		return res;
	}

	/**
	 * test if identifier selection and decision method are compatible a simple
	 * simulated annealing technique cannot be used with more than two
	 * possibilities
	 * 
	 * @param d
	 * @param id
	 * @return
	 */
	public static boolean isCombinable(DecisionMethod d, IdentifierMethod id) {
		if (d == DecisionMethod.BESTPREFERNEW
				|| d == DecisionMethod.BESTPREFEROLD
				|| d == DecisionMethod.PROPORTIONAL) {
			return true;
		}
		if (d == DecisionMethod.METROPOLIS || d == DecisionMethod.SA
				|| d == DecisionMethod.SATIMEDEPENDENT) {
			if (id == IdentifierMethod.TWORANDOM
					|| id == IdentifierMethod.ALLNEIGHBOR
					|| id == IdentifierMethod.ALLNEIGHBORMIDDLE) {
				return false;
			}
		}
		return true;
	}

	/**
	 * check if parameters make sense
	 * 
	 * @param checkold
	 * @param adjustone
	 * @param decision
	 * @param id
	 * @return
	 */
	public static boolean isSensible(boolean checkold, boolean adjustone,
			DecisionMethod decision, IdentifierMethod id) {
		if (checkold) {
			// when identifiers are chosen unconditionally, old does not need
			// check
			if (id == IdentifierMethod.ONERANDOM
					|| id == IdentifierMethod.TWORANDOM
					|| id == IdentifierMethod.SWAPPING) {
				return false;
			}
		}
		if (adjustone) {
			// identifer chosen close to neighbor anyway, no need for special
			// treatment for 1 degree nodes
			if (id == IdentifierMethod.ALLNEIGHBOR
					|| id == IdentifierMethod.RANDNEIGHBOR) {
				return false;
			}
		}
		return true;
	}

	public Graph transform(Graph g) {
		// get or create ID space
		GraphProperty[] prop = g.getProperties("ID_SPACE");
		DIdentifierSpace idSpace;
		int k = -1;
		for (int i = 0; i < prop.length; i++) {
			if (prop[i] instanceof RingIdentifierSpaceSimple) {
				k = i;
			}
		}
		if (k == -1) {
			Transformation idTrans = new RandomRingIDSpaceSimple();
			g = idTrans.transform(g);
			prop = g.getProperties("ID_SPACE");
			k = prop.length - 1;
		}
		idSpace = (DIdentifierSpace) prop[k];
		this.setIdspace(idSpace);

		// initialize
		Random rand = new Random();
		AttackableEmbeddingNode[] nodes = this.generateNodes(g, rand);
		AttackableEmbeddingNode[] selectionSet = this.generateSelectionSet(
				nodes, rand);
		g.setNodes(nodes);
		RingIdentifier[] ids = this.getIds();
		for (int i = 0; i < ids.length; i++) {
			((IQDNode) nodes[i]).setID(ids[i].getPosition());
		}
		for (int i = 0; i < ids.length; i++) {
			((IQDNode) nodes[i]).updateNeighbors(rand);
		}

		// run embedding algorithm round-based
		for (int i = 0; i < this.iterations * selectionSet.length; i++) {
			int index = rand.nextInt(selectionSet.length);
			if (selectionSet[index].getOutDegree() > 0) {
				if (this.adjustOneDegree
						&& selectionSet[index].getDegree() == 2) {
					IQDNode node = (IQDNode) selectionSet[index];
					double neighPos = ((IQDNode) nodes[node.getOutgoingEdges()[0]])
							.getID();
					if (Math.abs(this.computeDistance(neighPos, node.getID())) > this.epsilon) {
						double id = neighPos - rand.nextDouble() * this.epsilon;
						if (id < 0)
							id++;
						node.setID(id);
					}
				} else {
					selectionSet[index].updateNeighbors(rand);
					selectionSet[index].turn(rand);
				}
			}
		}

		// post-processing: set new IDs
		for (int i = 0; i < ids.length; i++) {
			ids[i].setPosition(((IQDNode) nodes[i]).getID());
		}
		Partition<Double>[] parts = new RingPartitionSimple[g.getNodes().length];
		for (int i = 0; i < parts.length; i++) {
			parts[i] = new RingPartitionSimple(ids[i]);
		}
		idSpace.setPartitions(parts);
		return g;
	}

	public double computeDistance(double a, double b) {
		if (this.distance == Distance.RING) {
			return Math.min(Math.abs(a - b), Math.min(1 - a + b, 1 - b + a));
		}
		if (this.distance == Distance.CLOCKWISE) {
			if (a > b) {
				return a - b;
			} else {
				return 1 + a - b;
			}
		}
		if (this.distance == Distance.SIGNED) {
			if (a > b) {
				if (a - b < 0.5) {
					return a - b;
				} else {
					return -(1 + b - a);
				}
			} else {
				if (a - b > -0.5) {
					return a - b;
				} else {
					return 1 + a - b;
				}
			}
		}
		throw new IllegalArgumentException("Distance calculation not set!");
	}

	/**
	 * init IdSpace from a graph g
	 * 
	 * @param g
	 */
	public void initIds() {
		DIdentifierSpace idSpaceD = this.getIdspace();
		this.ids = new RingIdentifier[idSpaceD.getPartitions().length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = (RingIdentifier) idSpaceD.getPartitions()[i]
					.getRepresentativeID();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbedding#getIDs()
	 */
	@Override
	public RingIdentifier[] getIds() {
		return this.ids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbedding#
	 * generateSelectionSet
	 * (gtna.transformation.attackableEmbedding.AttackableEmbeddingNode[],
	 * java.util.Random)
	 */
	@Override
	protected AttackableEmbeddingNode[] generateSelectionSet(
			AttackableEmbeddingNode[] nodes, Random rand) {
		return nodes;
	}

	public Distance getDistance() {
		return this.distance;
	}

}

/* ===========================================================
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
package gtna.transformation.attackableEmbedding.md;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.DIdentifierSpace;
import gtna.id.Partition;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDIdentifierSpaceSimple.DistanceMD;
import gtna.id.md.MDPartitionSimple;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingPartitionSimple;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.AttackableEmbedding;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.id.RandomMDIDSpaceSimple;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;

/**
 * @author stef
 * 
 */
public abstract class IQDMDEmbedding extends AttackableEmbedding {

	public static enum IdentifierMethod {
		ONERANDOM, TWORANDOM, RANDNEIGHBOR, ALLNEIGHBOR, SWAPPING
	}

	public static enum DecisionMethod {
		BESTPREFEROLD, BESTPREFERNEW, METROPOLIS, PROPORTIONAL, SA, SATIMEDEPENDENT
	}

	IdentifierMethod idMethod;
	DecisionMethod deMethod;
	int dimension;
	DistanceMD distance;
	double epsilon;
	boolean checkold;
	MDIdentifier[] ids;
	boolean adjustOneDegree;

	/**
	 * @param iterations
	 * @param key
	 * @param parameters
	 */
	public IQDMDEmbedding(int iterations, String key,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustOne, Parameter[] parameters, boolean add) {
		super(iterations, key, add?combineParameter(iterations, idMethod, deMethod,
				dimension, epsilon, checkold, adjustOne, distance, parameters):parameters);
		this.idMethod = idMethod;
		this.deMethod = deMethod;
		this.dimension = dimension;
		this.epsilon = epsilon;
		this.checkold = checkold;
		this.adjustOneDegree = adjustOne;
		this.distance = distance;
	}
	
	public IQDMDEmbedding(int iterations, String key,
			IdentifierMethod idMethod, DecisionMethod deMethod,
			DistanceMD distance, int dimension, double epsilon,
			boolean checkold, boolean adjustOne, Parameter[] parameters) {
		super(iterations, key, combineParameter(iterations, idMethod, deMethod,
				dimension, epsilon, checkold, adjustOne, distance, parameters));
		this.idMethod = idMethod;
		this.deMethod = deMethod;
		this.dimension = dimension;
		this.epsilon = epsilon;
		this.checkold = checkold;
		this.adjustOneDegree = adjustOne;
		this.distance = distance;
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
			IdentifierMethod idMethod, DecisionMethod deMethod, int dimension,
			double epsilon, boolean checkold, boolean adjustone,
			DistanceMD distance, Parameter[] parameters) {
		Parameter[] res = new Parameter[parameters.length + 8];
		res[0] = new IntParameter("ITERATIONS", iter);
		res[1] = new StringParameter("IDENTIFIER_METHOD", idMethod.toString());
		res[2] = new StringParameter("DECISION_METHOD", deMethod.toString());
		res[3] = new DoubleParameter("EPSILON", epsilon);
		res[4] = new BooleanParameter("CHECKOLD", checkold);
		res[5] = new IntParameter("DIMENSION", dimension);
		res[6] = new BooleanParameter("ADJUST_ONE", adjustone);
		res[7] = new StringParameter("DISTANCE", distance.toString());
		for (int i = 8; i < res.length; i++) {
			res[i] = parameters[i - 8];
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
					|| id == IdentifierMethod.ALLNEIGHBOR) {
				return false;
			}
		}
		return true;
	}

	public static boolean isSensible(boolean checkold, boolean adjustone,
			DecisionMethod decision, IdentifierMethod id) {
		if (checkold) {
			if (id == IdentifierMethod.ONERANDOM
					|| id == IdentifierMethod.TWORANDOM
					|| id == IdentifierMethod.SWAPPING) {
				return false;
			}
		}
		if (adjustone) {
			if (id == IdentifierMethod.ALLNEIGHBOR
					|| id == IdentifierMethod.RANDNEIGHBOR) {
				return false;
			}
		}
		return true;
	}

	public Graph transform(Graph g) {
		GraphProperty[] prop = g.getProperties("ID_SPACE");
		DIdentifierSpace idSpace;
		int k = -1;
		for (int i = 0; i < prop.length; i++) {
			if (prop[i] instanceof MDIdentifierSpaceSimple
					&& ((MDIdentifierSpaceSimple) prop[i]).getDimensions() == this.dimension) {
				k = i;
			}
		}
		if (k == -1) {
			double[] modulus = new double[this.dimension];
			for (int m = 0; m < this.dimension; m++) {
				modulus[m] = 1.0;
			}
			Transformation idTrans = new RandomMDIDSpaceSimple(1, modulus,
					true, this.distance);
			g = idTrans.transform(g);
			prop = g.getProperties("ID_SPACE");
			k = prop.length - 1;
		}
		idSpace = (DIdentifierSpace) prop[k];
		this.setIdspace(idSpace);
		Random rand = new Random();
		AttackableEmbeddingNode[] nodes = this.generateNodes(g, rand);
		AttackableEmbeddingNode[] selectionSet = this.generateSelectionSet(
				nodes, rand);
		g.setNodes(nodes);

		MDIdentifier[] ids = this.getIdsMD();
		for (int i = 0; i < ids.length; i++) {
			((IQDMDNode) nodes[i]).setID(ids[i].getCoordinates());
		}
		for (int i = 0; i < ids.length; i++) {
			((IQDMDNode) nodes[i]).updateNeighbors(rand);
		}
		for (int i = 0; i < this.iterations * selectionSet.length; i++) {
			int index = rand.nextInt(selectionSet.length);
			if (selectionSet[index].getOutDegree() > 0) {
				if (this.adjustOneDegree
						&& selectionSet[index].getDegree() == 2) {
					IQDMDNode node = (IQDMDNode) selectionSet[index];
					double[] neighPos = ((IQDMDNode) nodes[node
							.getOutgoingEdges()[0]]).getID();
					if (Math.abs(this.computeDistance(neighPos, node.getID())) > this.epsilon) {
						double[] id = new double[this.dimension];
						for (int j = 0; j < this.dimension; j++) {
							id[j] = neighPos[j] - rand.nextDouble()
									* this.epsilon / (double) this.dimension;
							if (id[j] < 0)
								id[j]++;
						}
						node.setID(id);
					}
				} else {
					selectionSet[index].updateNeighbors(rand);
					selectionSet[index].turn(rand);
				}
			}
		}
		for (int i = 0; i < ids.length; i++) {
			ids[i].setCoordinates(((IQDMDNode) nodes[i]).getID());
		}
		Partition<Double>[] parts = new MDPartitionSimple[g.getNodes().length];

		for (int i = 0; i < parts.length; i++) {
			parts[i] = new MDPartitionSimple(ids[i]);
		}
		idSpace.setPartitions(parts);
		return g;
	}

	public double computeDistance(double[] a, double[] b) {
		double temp;
		if (this.distance == DistanceMD.EUCLIDEAN) {
			double squarredResult = 0;
			for (int i = 0; i < this.dimension; i++) {
				temp = Math.min(Math.abs(a[i] - b[i]),
						Math.min(1 + a[i] - b[i], 1 - a[i] + b[i]));

				squarredResult += Math.pow(temp, 2);
			}
			return Math.sqrt(squarredResult);
		}
		if (this.distance == DistanceMD.MANHATTAN) {
			double result = 0;
			for (int i = 0; i < this.dimension; i++) {
				temp = Math.min(Math.abs(a[i] - b[i]),
						Math.min(1 + a[i] - b[i], 1 - a[i] + b[i]));
				result += temp;
			}
			return result;
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
		this.ids = new MDIdentifier[idSpaceD.getPartitions().length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = (MDIdentifier) idSpaceD.getPartitions()[i]
					.getRepresentativeID();
		}
	}

	public MDIdentifier[] getIdsMD() {
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

	public DistanceMD getDistance() {
		return this.distance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.attackableEmbedding.AttackableEmbedding#getIds()
	 */
	@Override
	public RingIdentifier[] getIds() {
		// TODO Auto-generated method stub
		return null;
	}

}

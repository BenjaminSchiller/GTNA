package gtna.transformation.communities.matrices;

/**
 * Encapsulates the Matrix Q of the community detection algorithm based on
 * improving Q in every step. Further information on this algorithm can be found
 * in "Fast algorithm for detecting community structure in networks" by M. E. J.
 * Newman, published 18 June 2004. Note that this Matrix is intended for
 * directed graphs (or more precise: graphs with an asymmetric adjacency
 * matrix). If the graph is undirected (or the adjacency matrix is symmetric for
 * a directed graph) use of MyQEMatrix.java is advised as it halfs the memory
 * requirement, however using this EMatrix is possible. Memory usage scales with
 * the square of the node count.
 * 
 * @author Philipp Neubrand
 * 
 */
public class MyQMatrixClassic implements IMyQMatrix {
	// The dimension of the matrix, will be equal to the number of nodes.
	private int dimension;

	// Signals if a node was deleted. A deleted node will be ignored when trying
	// to find the next merge and when updating the matrix. This is assumed to
	// be faster than resizing the array at each iteration.
	private boolean[] deleted;

	// Holds the actual data of the matrix, stored as an array of rows,
	// data[column, row].
	private int[][] data;

	// Stores the EMatrix of which this QMatrix was created. This matrix will
	// then be used when calculating the update.
	private MyEMatrixInt e;

	// Stores the change in modularity for the last merge.
	private double lastDelta;

	private boolean debug = false;

	private int edges;

	/**
	 * Static constructor to create a Q matrix out of an E matrix. This is
	 * basically the only initiation that makes sense since all the values of
	 * the Q matrix depend on the E matrix. Note that this MyQMatrix is intended
	 * for usage with non-symmetrical E matrices. If E is symmetric, use
	 * MyQEMatrix instead, it will save about 50% memory.
	 * 
	 * @param e
	 *            the EMatrix from what to create the QMatrix
	 * @return a ready to use QMatrix, fully initialized
	 */
	public MyQMatrixClassic(MyEMatrixInt e) {

		dimension = e.getDimension();

		deleted = new boolean[dimension];
		data = new int[dimension][dimension];

		int edges = e.numEdges();
		setEdges(edges);
		for (int i = 0; i < dimension; i++) {
			for (int j = i + 1; j < dimension; j++) {
				if (e.getValue(i, j) == 0 && e.getValue(j, i) == 0)
					setValue(i, j, Integer.MAX_VALUE);
				// if there is no edge between two nodes they can not be joined
				// and therefore the associated value in the QMatrix is set to
				// Integer.MAX_VALUE so this join will be ignored
				else
					setValue(i, j,
							(edges * (e.getValue(j, i) + e.getValue(i, j)) - 2
									* e.getRowSum(i) * e.getRowSum(j)));

				// as defined by the paper referred to in the Class Description
			}
		}

		setEMatrix(e);
	}

	private void setEdges(int numEdges) {
		edges = numEdges;
	}

	/**
	 * Setter for the EMatrix this QMatrix is based on.
	 * 
	 * @param e2
	 *            The EMatrix to set
	 */
	public void setEMatrix(MyEMatrixInt e2) {
		e = e2;
	}

	/**
	 * Standard Setter for a value of the matrix.
	 * 
	 * @param i
	 *            the row index of the value
	 * @param j
	 *            the column index of the value
	 * @param d
	 *            the new value
	 */
	private void setValue(int i, int j, int d) {
		data[i][j] = d;
	}

	/**
	 * Standard Getter for a value of the matrix.
	 * 
	 * @param i
	 *            the row index of the value
	 * @param j
	 *            the column index of the value
	 * @return the value data[i][j]
	 */
	private int getValue(int i, int j) {
		return data[i][j];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.communities.IMyQMatrix#getNextMerge()
	 */
	public void getNextMerge(int[] erg) {
		int aktMaxValue = -Integer.MAX_VALUE;
		int aktMaxI = -1;
		int aktMaxJ = -1;
		for (int i = 0; i < dimension; i++) {
			if (!deleted[i]) {
				// since the deltaQ matrix is an upper right triangular matrix
				// (with even the diagonal elements being 0) only these elements
				// are checked
				for (int j = i + 1; j < dimension; j++) {
					if (!deleted[j]) {
						if (getValue(i, j) > aktMaxValue
								&& getValue(i, j) != Integer.MAX_VALUE) {
							aktMaxValue = getValue(i, j);
							aktMaxI = i;
							aktMaxJ = j;
						}
					}
				}
			}
		}

		lastDelta = aktMaxValue;
		erg[0] = aktMaxI;
		erg[1] = aktMaxJ;

		// if(debug)
		// Log.debug("Next merge (" + aktMaxI + ", " + aktMaxJ + ") = " +
		// aktMaxValue);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.communities.IMyQMatrix#update(int, int,
	 * gtna.metrics.communities.MyEMatrixInteger)
	 */
	public void update(int i, int j) {
		// first flag j as deleted so there are no updates done to the column
		deleted[j] = true;
		// assume i = 3 (j is irrelevant for the iteration since it will only be
		// flagged as deleted, no changes will be done to it
		// - 1 2 3 4
		// 1 .
		// 2 ...
		// 3 ..... x
		// 4 .......
		// first recalculate the row i (starting by i + 1 since merging is
		// commutative i.e. 1+2 = 2+1)
		for (int k = i + 1; k < dimension; k++) {
			if (!deleted[k]) {
				if (e.getValue(i, k) == 0 && e.getValue(k, i) == 0)
					setValue(i, k, Integer.MAX_VALUE);
				else
					setValue(
							i,
							k,
							(edges * (e.getValue(k, i) + e.getValue(i, k)) - (2 * e
									.getRowSum(k) * e.getRowSum(i))));

				// if (debug)
				// Log.debug("Set value for (" + i + ", " + k + ") to "
				// + getValue(i, k));
			}
		}
		// second recalculate the column i starting from 0 and ending at i-1 for
		// the same reason as above
		// - 1 2 3 4
		// 1 . x
		// 2 ... x
		// 3 .....
		// 4 .......
		// so in the end 1+3, 2+3 and 3+4 were updated (one of them will be
		// flagged as deleted as it will have been merged with 3 and therefore
		// it will have been ignored)
		for (int k = 0; k < i; k++) {
			if (!deleted[k])
				if (e.getValue(k, i) == 0 && e.getValue(i, k) == 0)
					setValue(k, i, Integer.MAX_VALUE);
				else
					setValue(
							k,
							i,
							(edges * (e.getValue(i, k) + e.getValue(k, i)) - (2 * e
									.getRowSum(k) * e.getRowSum(i))));

			// if (debug)
			// Log.debug("Set value for (" + i + ", " + j + ") to "
			// + getValue(i, k));
		}
	}

	/**
	 * Overwritten toString method for easier debugging
	 */
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("\r\n\t");
		for (int i = 0; i < dimension; i++) {
			if (!deleted[i]) {
				ret.append(i);
				ret.append("\t");
			}
		}
		ret.append("\r\n");
		for (int i = 0; i < dimension; i++) {
			if (!deleted[i]) {
				ret.append(i);
				ret.append("\t");
				for (int j = 0; j < dimension; j++) {
					if (!deleted[j]) {
						if (getValue(i, j) == Integer.MAX_VALUE)
							ret.append("x");
						else
							ret.append(getValue(i, j));
						ret.append("\t");
					}
				}
				ret.append("\r\n");
			}
		}
		return ret.toString();
	}

	@Override
	public double getLastDelta() {
		return lastDelta;
	}

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}

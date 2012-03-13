package gtna.transformation.communities.matrices;

import java.util.TreeSet;

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
public class MyQMatrixLong implements IMyQMatrix {
	// The dimension of the matrix, will be equal to the number of nodes.
	private int dimension;

	// Signals if a node was deleted. A deleted node will be ignored when trying
	// to find the next merge and when updating the matrix. This is assumed to
	// be faster than resizing the array at each iteration.
	private boolean[] deleted;

	private TreeSet<MergeValueLong> data;

	private long[][] data2;

	// Stores the EMatrix of which this QMatrix was created. This matrix will
	// then be used when calculating the update.
	private MyEMatrixLong e;

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
	public MyQMatrixLong(MyEMatrixLong e) {

		dimension = e.getDimension();

		deleted = new boolean[dimension];
		data = new TreeSet<MergeValueLong>(new MergeValueLongComparator());
		data2 = new long[dimension][dimension];

		int edges = e.numEdges();
		long val;
		setEdges(edges);
		MergeValueLong temp = null;
		for (int i = 0; i < dimension; i++) {
			for (int j = i + 1; j < dimension; j++) {
				if (e.getValue(i, j) != 0 || e.getValue(j, i) != 0) {
					val = (edges * (e.getValue(j, i) + e.getValue(i, j)) - 2
							* e.getRowSum(i) * e.getRowSum(j));
					temp = new MergeValueLong(i, j, val);
					data.add(temp);
					data2[i][j] = val;
				} else
					data2[i][j] = Integer.MAX_VALUE;

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
	public void setEMatrix(MyEMatrixLong e2) {
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
	private void setValue(int i, int j, long d) {
		data.add(new MergeValueLong(i, j, d));
		data2[i][j] = d;
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
	private long getValue(int i, int j) {
		return data2[i][j];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.communities.IMyQMatrix#getNextMerge()
	 */
	public void getNextMerge(int[] erg) {
		if (data.size() == 0) {
			erg[0] = -1;
			erg[1] = -1;
			return;
		}

		MergeValueLong next = data.last();

		lastDelta = next.value;

		erg[0] = next.i;
		erg[1] = next.j;

		// if (debug)
		// Log.debug("Next merge (" + erg[0] + ", " + erg[1] + ") = "
		// + lastDelta);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.communities.IMyQMatrix#update(int, int,
	 * gtna.metrics.communities.MyEMatrixInteger)
	 */
	public void update(int i, int j) {
		removeValue(i, j);

		// first flag j as deleted so there are no updates done to the column
		deleted[j] = true;

		for (int k = 0; k < dimension; k++) {
			if (k < i) {
				removeValue(k, i);
				if (!deleted[k]) {
					if (e.getValue(i, k) != 0 || e.getValue(k, i) != 0) {
						setValue(
								k,
								i,
								(edges * (e.getValue(k, i) + e.getValue(i, k)) - (2 * e
										.getRowSum(k) * e.getRowSum(i))));
					}
				}
			} else if (k > i) {
				removeValue(i, k);
				if (!deleted[k]) {
					if (e.getValue(i, k) != 0 || e.getValue(k, i) != 0) {
						setValue(
								i,
								k,
								(edges * (e.getValue(i, k) + e.getValue(k, i)) - (2 * e
										.getRowSum(k) * e.getRowSum(i))));
					}
				}

			}

			if (k < j)
				removeValue(k, j);
			else if (k > j)
				removeValue(j, k);

		}
	}

	private void removeValue(int j, int k) {
		if (data2[j][k] != Integer.MAX_VALUE) {
			data.remove(new MergeValueLong(j, k, data2[j][k]));
			data2[j][k] = Integer.MAX_VALUE;
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

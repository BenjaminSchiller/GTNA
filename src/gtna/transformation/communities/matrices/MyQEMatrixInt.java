package gtna.transformation.communities.matrices;

import gtna.graph.Edge;
import gtna.graph.Graph;

/**
 * An implementation combining the Q and E matrix into one class, using int for
 * internal storage.
 * 
 * @author Philipp Neubrand
 * 
 */
public class MyQEMatrixInt implements IMyEMatrix, IMyQMatrix {

	// stores the change in modularity for the last merge
	private double lastDelta;

	// stores the dimension of the matrix
	private int dimension;

	// stores the number of edges
	private int edges;

	// stores the actual data of the matrix.
	private int[][] data;

	// buffer for the row sums
	private int[] rowSums;

	// flags if a row/column was deleted
	private boolean[] deleted;

	// flag if debug is enabled
	private boolean debug = false;

	private int temp;

	/**
	 * Creates a QEMatrix from a given graph. First the upper right triangle is
	 * filled with values, creating the EMatrix. Then, based on that, the
	 * QMatrix is created in the lower left triangle.
	 * 
	 * @param graph
	 *            The graph from which to create the QEMatrix
	 * @return a fully initialized QEMatrix ready to use
	 */
	public static MyQEMatrixInt createFromGraph(Graph graph) {
		// initialize a matrix with the given dimension
		int dimension = graph.getNodes().length;
		int edges = graph.getEdges().size();
		MyQEMatrixInt ret = new MyQEMatrixInt(dimension);

		// insert the values for all the edges that are present
		for (Edge akt : graph.getEdges().getEdges()) {
			if (akt.getSrc() > akt.getDst())
				ret.setValue(akt.getDst(), akt.getSrc(), 1);
			else
				ret.setValue(akt.getSrc(), akt.getDst(), 1);

		}

		// calculate all row sums for the first time
		ret.calcAllRowSums();

		// Create the lower left part
		for (int i = 0; i < dimension; i++) {
			for (int j = i + 1; j < dimension; j++) {
				if (ret.getValue(i, j) == 0)
					ret.setValue(j, i, Integer.MAX_VALUE);
				// if there is no edge between two nodes they can not be joined
				// and therefore the associated value in the QMatrix is set to
				// Integer.MAX_VALUE so this join will be ignored
				else
					ret.setValue(j, i, (2 * edges * ret.getValue(i, j) - 2
							* ret.getRowSum(i) * ret.getRowSum(j)));
				// as defined by the paper referred to in the Class Description

			}
		}

		ret.setEdges(edges);

		return ret;
	}

	private void setEdges(int edges2) {
		edges = edges2;
	}

	/**
	 * Getter for the row sum for row i. Does not actually recalculate the sum
	 * but just returns the value in the buffer.
	 * 
	 * @param i
	 *            the row for which the row sum is to be calculated
	 * @return the row sum to be calculated
	 */
	private int getRowSum(int i) {
		return rowSums[i];
	}

	/**
	 * calculates the row sums for all the rows
	 */
	private void calcAllRowSums() {
		for (int i = 0; i < dimension; i++) {
			calcRowSum(i);
		}
	}

	/**
	 * calculates (and returns) the row sum for the given row.
	 * 
	 * @param i
	 *            the row for which the value is to be calculated
	 * @return the row sum
	 */
	private int calcRowSum(int i) {
		rowSums[i] = 0;
		for (int j = 0; j < dimension; j++) {
			if (i < j)
				rowSums[i] += getValue(i, j);
			else if (i >= j)
				rowSums[i] += getValue(j, i);
		}
		return rowSums[i];
	}

	private int getValue(int i, int j) {
		return data[i][j];
	}

	private void setValue(int index, int index2, int d) {
		data[index][index2] = d;
	}

	private MyQEMatrixInt(int dimension) {
		this.dimension = dimension;
		data = new int[dimension][dimension];
		rowSums = new int[dimension];
		deleted = new boolean[dimension];
	}

	public void merge(int i, int j) {
		// Update all values but the diagonal element, this needs to be handled
		// separately
		// if (debug)
		// Log.debug("Merging: " + i + " " + j);

		int jk = 0;
		for (int k = 0; k < dimension; k++) {
			if (k != i && k != j && !deleted[k]) {
				if (j > k)
					jk = getValue(k, j);
				else
					jk = getValue(j, k);

				if (i > k) {
					temp = k;
					k = i;
					i = temp;
				}
				// if (debug)
				// Log.debug("Set value for (" + i + "," + k + ") to " +
				// getValue(i, k) + " with jk = " + jk);

				setValue(i, k, getValue(i, k) + jk);
				// if (debug)
				// Log.debug("Set value for (" + i + "," + k + ") to " +
				// getValue(i, k) + " with jk = " + jk);
				if (k > i) {
					temp = k;
					k = i;
					i = temp;
				}
			}
		}

		// Update the diagonal element
		int ij = 0;
		if (i > j)
			ij = getValue(j, i);
		else
			ij = getValue(i, j);

		setValue(i, i, getValue(i, i) + 2 * ij + getValue(j, j));

		// if (debug)
		// Log.debug("Set value for (" + i + ", " + i + ") to " + getValue(i,
		// i));

		// Flag j as deleted
		deleted[j] = true;

		// recalculate the row sum, just add the cached row sums together
		rowSums[i] += rowSums[j];
		// if (debug)
		// Log.debug("Combined rowsum for " + i + " is: " + rowSums[i]);
	}

	/**
	 * Returns the change in modularity for the last merge.
	 */
	@Override
	public double getLastDelta() {
		return lastDelta;
	}

	/**
	 * Calculates the next merge and stores it in the supplied array. erg[0]
	 * will hold the first community to be merged, erg[1] the second one. Note
	 * that erg[0] < erg[1].
	 */
	@Override
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
						if (getValue(j, i) > aktMaxValue
								&& getValue(j, i) != Integer.MAX_VALUE) {
							aktMaxValue = getValue(j, i);
							aktMaxI = i;
							aktMaxJ = j;
						}
					}
				}
			}
		}

		lastDelta = aktMaxValue;
		// if(debug)
		// Log.debug("Next merge: (" + aktMaxI + ", " + aktMaxJ + ") = " +
		// aktMaxValue);
		erg[0] = aktMaxI;
		erg[1] = aktMaxJ;
	}

	/**
	 * Update the Q part of the matrix after two communities were merged.
	 */
	@Override
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
		// first recalculate the row i (starting at i + 1 since merging is
		// commutative i.e. 1+2 = 2+1)
		for (int k = i + 1; k < dimension; k++) {
			if (!deleted[k]) {
				if (getValue(i, k) == 0)
					setValue(k, i, Integer.MAX_VALUE);
				else
					setValue(
							k,
							i,
							(2 * edges * getValue(i, k) - (2 * getRowSum(k) * getRowSum(i))));

				// if(debug)
				// Log.debug("Set the value for ("+k + ", " + i+") to " +
				// getValue(k, i));
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
				if (getValue(k, i) == 0)
					setValue(i, k, Integer.MAX_VALUE);
				else
					setValue(
							i,
							k,
							(2 * edges * getValue(k, i) - (2 * getRowSum(k) * getRowSum(i))));

			// if(debug)
			// Log.debug("Set the value for ("+k + ", " + i+") to " +
			// getValue(k, i));

		}

	}

	/**
	 * Overwritten toString method to allow easier debugging
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
						else if (Integer.toString(getValue(i, j)).length() > 6)
							ret.append(Integer.toString(getValue(i, j))
									.substring(0, 5));
						else
							ret.append(Integer.toString(getValue(i, j)));
						ret.append("\t");
					}
				}
				ret.append("\r\n");
			}
		}
		return ret.toString();
	}

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;

	}

}

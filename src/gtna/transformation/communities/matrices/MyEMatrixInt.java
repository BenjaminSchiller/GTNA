package gtna.transformation.communities.matrices;

import gtna.graph.Edge;
import gtna.graph.Graph;

/**
 * An EMatrix implementation using int for internal storage.
 * 
 * @author Philipp Neubrand
 * 
 */
public class MyEMatrixInt implements IMyEMatrix {
	// The dimension of the matrix, will be the number of nodes.
	private int dimension;

	// Signals if a node was deleted. A deleted node will be ignored when
	// calculating row sums and when searching for the next merge. This
	// eliminates the need to resize the matrix after every iteration which
	// improves performance.
	private boolean[] deleted;

	// The data of this EMatrix stored as an array of rows, data[column][row].
	// This representation was chosen since row sums need to be calculated on a
	// regular basis (n at the beginning and 1 at each iteration for an overall
	// of 2n).
	private int[][] data;

	// Cache for the row sums. In every iteration only 1 row sum is changed
	// while up to n need to be accessed.
	private int[] rowSums;

	// Stores the number of edges.
	private int edges;

	/**
	 * Creates an EMatrix from the given graph. For a given node pair i and j
	 * the matrix[i, j] has a value of 1 if there is an edge between two nodes
	 * and 0 if there is not. In addition the rowsums are calculated for the
	 * generated matrix.
	 * 
	 * @param graph
	 *            The graph from which to create the EMatrix
	 * @return a fully initialized EMatrix ready for use with the deltaQ
	 *         algorithm
	 */
	public MyEMatrixInt(Graph graph) {
		// initialize a matrix with the given dimension
		this.dimension = graph.getNodes().length;
		deleted = new boolean[dimension];
		data = new int[dimension][dimension];
		rowSums = new int[dimension];

		edges = graph.getEdges().size();
		
		int aktRow = 0;

		// insert the values for all the edges that are present while at the
		// same time calculating the row sum
		for (Edge akt : graph.getEdges().getEdges()) {
			aktRow = akt.getSrc();
			setValue(aktRow, akt.getDst(), 1);
			rowSums[aktRow] += 1;
		}
	}

	/**
	 * Calculates all the row sums. Convenience method, could just call
	 * calRowSum(i) for all i (and does exactly that).
	 */
	public void calcAllRowSums() {
		for (int i = 0; i < dimension; i++) {
			calcRowSum(i);
		}
	}

	/**
	 * Calculates and returns the row sum of row i. The result is cached in the
	 * rowSums cache. Unlike getRowSum() this will recalculate the sum, usually
	 * getRowSum() should be used to get the sum of a row.
	 * 
	 * @param i
	 *            the row for which the sum is wanted
	 * @return the sum of the row i
	 */
	public int calcRowSum(int i) {

		int sum = 0;

		for (int j = 0; j < dimension; j++) {
			if (!deleted[j])
				sum += getValue(i, j);
		}

		rowSums[i] = sum;

		return rowSums[i];
	}

	/**
	 * Sets row <i>i</i>, column <i>j</i> to <i>value</i>.
	 * 
	 * @param i
	 *            the row
	 * @param j
	 *            the column
	 * @param value
	 *            the value to set
	 */
	public void setValue(int i, int j, int value) {
		data[i][j] = value;
	}

	/**
	 * Getter for the value at row <i>i</i>, column <i>j</i>
	 * 
	 * @param i
	 *            the row
	 * @param j
	 *            the column
	 * @return the value at (i, j)
	 */
	public int getValue(int i, int j) {
		return data[i][j];
	}

	/**
	 * Getter for the dimension of the matrix
	 * 
	 * @return the dimension
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * Getter for the RowSum of row <i>i</i>. Unlike calcRowSum() this only
	 * returns the cached value but does not recalculate it.
	 * 
	 * @param i
	 *            the row
	 * @return the cached sum of the row
	 */
	public int getRowSum(int i) {
		return rowSums[i];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.communities.IMyEMatrix#merge(int, int)
	 */
	public void merge(int i, int j) {

		// Update all values but the diagonal element, this needs to be handled
		// separately
		for (int k = 0; k < dimension; k++) {
			if (k != i && k != j && !deleted[k]) {
				setValue(i, k, getValue(i, k) + getValue(j, k));
				setValue(k, i, getValue(k, i) + getValue(k, j));
			}
		}

		// Update the diagonal element
		setValue(i, i, getValue(i, i) + getValue(i, j) + getValue(j, i) + getValue(j, j));

		// Flag j as deleted
		deleted[j] = true;

		// recalculate the row sum, just add the cached row sums together
		rowSums[i] += rowSums[j];
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
						if (Integer.toString(getValue(i, j)).length() > 7)
							ret.append(Integer.toString(getValue(i, j)).substring(0, 6));
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

	/**
	 * Getter for the number of edges of the graph.
	 * 
	 * @return the number of edges.
	 */
	public int numEdges() {
		return edges;
	}

}

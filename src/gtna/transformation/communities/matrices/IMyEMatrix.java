package gtna.transformation.communities.matrices;

/**
 * Encapsulates the Matrix E of the community detection algorithm based on
 * improving Q in every step. Further information on this algorithm can be found
 * in "Fast algorithm for detecting community structure in networks" by M. E. J.
 * Newman, published 18 June 2004. Note that this Matrix is intended for
 * directed graphs (or more precise: graphs with an asymmetric adjacency
 * matrix). If the graph is undirected (or the adjacency matrix is symmetric for
 * a directed graph) use of MyQEMatrix.java is advised as it halfs the memory
 * requirement however using this EMatrix is possible. Memory usage scales with
 * the square of the node count.
 * 
 * @author Philipp Neubrand
 * 
 */
public interface IMyEMatrix {

	/**
	 * Merges row <i>i</i> and row <i>j</i> and column <i>i</i> and column
	 * <i>j</i>. Rows and columns are merged by adding the corresponding values
	 * together. The new value of the diagonal element (i, i) is the sum of the
	 * 4 elements (i, i), (i, j), (j, i), (j, j) together. <i>j</i> will be
	 * deleted, <i>i</i> will be the new merged id. For algorithmic purposes
	 * <i>i</i> < <i>j</i> however the method will work if this is not the case.
	 * 
	 * @param i
	 *            the first row/column to be merged
	 * @param j
	 *            the second row/column to be merged
	 */
	public void merge(int i, int j);

}
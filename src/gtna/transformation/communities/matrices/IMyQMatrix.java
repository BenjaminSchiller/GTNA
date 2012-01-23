package gtna.transformation.communities.matrices;

/**
 * Interface for the matrix Q of the community detection algorithm based on
 * improving the modularity. Every implementation needs to provide the specified
 * methods, the interface was created to allow multiple implementations which
 * only differ in the internal format they use (int, double, float...).
 * 
 * 
 * @author Philipp Neubrand
 * 
 */
public interface IMyQMatrix {

	/**
	 * Determinates the best possible next merge which is the merge with the
	 * merge with the overall biggest value in the QMatrix (which represents the
	 * change in the modularity), for more information refer to the Paper in the
	 * class comment.
	 * 
	 * @param nextMerge
	 *            the array which will then contain the two communities to be
	 *            merged. Needs to be (at least) int[2]. No checking is done, if
	 *            the array is too short (or even null) an uncaught exception
	 *            will be thrown.
	 */
	public void getNextMerge(int[] nextMerge);

	/**
	 * Updates the QMatrix after E has changed. The parameters i and j identify
	 * the two communities that were merged in this iteration. Only the elements
	 * of the QMatrix that are concerning these communities need to be updated,
	 * which are the ith row and the ith column (since j is just flagged as
	 * deleted without changing anything).
	 * 
	 * @param i
	 *            one of the communities that was merged, by definition it is
	 *            the one that "survived" the merge
	 * @param j
	 *            the other communities that was merged, will be marked as
	 *            "deleted"
	 * @param e
	 *            the MyEMatrix that was updated
	 */
	public void update(int i, int j);

	/**
	 * Gets the change in modularity for the last best merge. This function was
	 * introduced to be able to have <code>getNextMerge()</code> return only an
	 * int array.
	 * 
	 * @return the change in modularity of the last merge
	 */
	public double getLastDelta();

	/**
	 * Sets the debugging flag for the matrix object.
	 * 
	 * @param debug
	 *            if debugging is enabled or not
	 */
	public void setDebug(boolean debug);
}
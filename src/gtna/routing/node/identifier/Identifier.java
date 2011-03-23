package gtna.routing.node.identifier;

/**
 * Interface for identifiers that are required by gtna.routing.node.IDNode. Each
 * instance of such an identifier is an element of an identifier space used by
 * the respective system like, e.g., DHTs. These identifier are then used to
 * enable routing using various algorithms.
 * 
 * Conventions: Please follow these conventions if possible and applicable to
 * the respective system.
 * 
 * 1. When using d-dimensional identifier spaces, use [0,1)^d.
 * 
 * 2. Normalize the distance function to produce values \in [0,1).
 * 
 * @author benni
 * 
 */
public interface Identifier {
	/**
	 * Computes the distance of this identifier to the given one. Note that
	 * according to the second convention, this values should always be \in
	 * [0,1).
	 * 
	 * @param id
	 *            identifier to compare the distance to
	 * @return distance between the two identifiers
	 */
	public double dist(Identifier id);

	/**
	 * Determines if this identifier has the same value as the given one.
	 * 
	 * @param id
	 *            identifier to compare to
	 * @return true if the given identifier's value equals this identifier's,
	 *         false otherwise
	 */
	public boolean equals(Identifier id);
}
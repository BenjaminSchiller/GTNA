package gtna.routing;

import gtna.graph.Node;

import java.util.ArrayList;

/**
 * Interface for representations of the results of a single routing attempt by a
 * routing algorithm. This contains the path taken by the algorithm, a flag for
 * success, and the total number of messages involved in this routing algorithm.
 * 
 * @author benni
 * 
 */
public interface Route {
	/**
	 * 
	 * @return array containing all hops on the routing path, starting at the
	 *         source and ending at the destination
	 */
	public ArrayList<Node> path();

	/**
	 * 
	 * @return true if the routing was successful, false otherwise
	 */
	public boolean success();

	/**
	 * 
	 * @return total number of message sent throughout the whole network as a
	 *         result of this routing attempt
	 */
	public int messages();

	/**
	 * Add the new node to the end of the current routing path.
	 * 
	 * @param n
	 *            new node to add
	 */
	public void add(Node n);

	/**
	 * Increments the message counter by 1.
	 */
	public void incMessages();

	/**
	 * Increments the message counter by the given number.
	 * 
	 * @param inc
	 *            number to increments the message counter
	 */
	public void incMessages(int inc);

	/**
	 * Sets the success flag.
	 * 
	 * @param success
	 *            new success flag
	 */
	public void setSuccess(boolean success);
}

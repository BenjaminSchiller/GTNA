package gtna.routing;

import gtna.graph.Node;

import java.util.ArrayList;

/**
 * Implements all features of the Path interface.
 * 
 * @author benni
 * 
 */
public class RouteImpl implements Route {
	private ArrayList<Node> path;

	private boolean success;

	private int messages;

	/**
	 * 
	 * @param route
	 *            array containing all hops on the routing path, starting at the
	 *            source and ending at the destination
	 * @param success
	 *            flag that indicates if this particular routing attempt was
	 *            successful, i.e., the last node of the path is the target node
	 *            or contains the requested information
	 * @param messages
	 *            total number of message sent as a result of this routing
	 *            attempt (equals path.length-1 in most cased but may differ
	 *            for, e.g., algorithms using parallel requests)
	 */
	public RouteImpl(ArrayList<Node> path, boolean success, int messages) {
		this.path = path;
		this.success = success;
		this.messages = messages;
	}

	public RouteImpl() {
		this.path = new ArrayList<Node>();
		this.success = false;
		this.messages = 0;
	}

	public void add(Node n) {
		this.path.add(n);
	}

	public void incMessages() {
		this.messages++;
	}

	public void incMessages(int inc) {
		this.messages += inc;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ArrayList<Node> path() {
		return this.path;
	}

	public boolean success() {
		return this.success;
	}

	public int messages() {
		return this.messages;
	}
}

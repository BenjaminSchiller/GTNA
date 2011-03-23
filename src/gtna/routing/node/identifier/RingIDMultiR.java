package gtna.routing.node.identifier;

/**
 * Implements an identifier similar to gtna.routing.node.identifier.RingID. The
 * only difference is that this implementation also has an attribute for the
 * reality it belongs to. This means that the network spans multiple overlays in
 * parallel and every node has a separate identifier in each overlay or reality.
 * 
 * @author benni
 * 
 */
public class RingIDMultiR extends RingID implements Identifier {
	public int reality;

	/**
	 * 
	 * @param pos
	 *            the actual identifier (position on the ring)
	 * @param reality
	 *            the reality that the identifier belongs to
	 */
	public RingIDMultiR(double pos, int reality) {
		super(pos);
		this.reality = reality;
	}

	public boolean equals(Identifier id) {
		return super.equals(id) && ((RingIDMultiR) id).reality == this.reality;
	}

	public String toString() {
		return "RIDMR(" + this.pos + "/" + this.reality + ")";
	}
}

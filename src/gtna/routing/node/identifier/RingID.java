package gtna.routing.node.identifier;

/**
 * Implements an Identifier for ring-based identifier spaces like Chord or
 * Symphony. All identifiers are assumed to be chosen from the identifier space
 * [0,1). All distances are computed using a wrap-around at 1. Therefore:
 * dist(0.1, 0.3) = dist(0.3, 0.1) = 0.2 and dist(0.8, 0.1) = 0.3
 * 
 * @author benni
 * 
 */
public class RingID implements Identifier {
	public double pos;

	/**
	 * 
	 * @param pos
	 *            the actual identifier (position on the ring)
	 */
	public RingID(double pos) {
		this.pos = pos;
	}

	public double dist(Identifier id) {
		double src = this.pos;
		double dest = ((RingID) id).pos;
		double dist = src <= dest ? dest - src : dest + 1 - src;
		return dist <= 0.5 ? dist : 1 - dist;
	}

	public boolean equals(Identifier id) {
		return ((RingID) id).pos == this.pos;
	}

	public String toString() {
		return "RID(" + this.pos + ")";
	}
}
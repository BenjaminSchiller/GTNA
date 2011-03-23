package gtna.routing.node.identifier;


/**
 * @deprecated
 */
public class GridIDManhattan extends GridID implements Identifier {
	public double[] pos;

	public GridIDManhattan(double[] pos) {
		super(pos);
		this.pos = pos;
	}

	public int dimensions() {
		return this.pos.length;
	}

	public double dist(Identifier id) {
		double[] pos = ((GridIDManhattan) id).pos;
		double sum = 0;
		for (int i = 0; i < this.pos.length; i++) {
			sum += Math.abs(this.pos[i] - pos[i]);
		}
		return Math.sqrt(sum);
	}

	public boolean equals(Identifier id) {
		double[] pos = ((GridIDManhattan) id).pos;
		for (int i = 0; i < this.pos.length; i++) {
			if (this.pos[i] != pos[i]) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer("(" + this.pos[0]);
		for (int i = 1; i < this.pos.length; i++) {
			buff.append(", " + this.pos[i]);
		}
		buff.append(")");
		return buff.toString();
	}
}

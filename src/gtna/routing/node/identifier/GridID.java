package gtna.routing.node.identifier;


/**
 * @deprecated
 */
public class GridID implements Identifier {
	public double[] x;

	public GridID(double[] x) {
		this.x = x;
	}

	public double dist(Identifier id) {
		GridID ID = (GridID) id;
		double sum = 0;
		for (int i = 0; i < this.x.length; i++) {
			sum += Math.pow(Math.abs(this.x[i] - ID.x[i]), this.x.length);
		}
		return Math.pow(sum, 1.0 / (double) this.x.length);
	}

	public int dimensions() {
		return this.x.length;
	}

	public boolean equals(GridID id) {
		for (int i = 0; i < this.x.length; i++) {
			if (this.x[i] != id.x[i]) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("(" + this.x[0]);
		for (int i = 1; i < this.x.length; i++) {
			buff.append("/" + this.x[i]);
		}
		buff.append(")");
		return buff.toString();
	}

	public GridID clone() {
		return new GridID(this.x.clone());
	}

	public boolean equals(Identifier id) {
		double[] x = ((GridID) id).x;
		for (int i = 0; i < this.x.length; i++) {
			if (x[i] != this.x[i]) {
				return false;
			}
		}
		return true;
	}
}

package gtna.routing.node.identifier;


/**
 * @deprecated
 */
public class GridIDEuclidean implements Identifier {
	public double[] pos;

	public GridIDEuclidean(double[] pos) {
		this.pos = pos;
	}

	public int dimensions() {
		return this.pos.length;
	}

	public double dist(Identifier id) {
		double[] pos = ((GridIDEuclidean) id).pos;
		double sum = 0;
		for (int i = 0; i < this.pos.length; i++) {
			sum += (this.pos[i] - pos[i]) * (this.pos[i] - pos[i]);
		}
		return Math.sqrt(sum);
	}

	public boolean equals(Identifier id) {
		double[] pos = ((GridIDEuclidean) id).pos;
		for (int i = 0; i < this.pos.length; i++) {
			if (this.pos[i] != pos[i]) {
				return false;
			}
		}
		return true;
	}
	
	public String toString(){
		StringBuffer buff = new StringBuffer("(" + this.pos[0]);
		for(int i=1; i<this.pos.length; i++){
			buff.append(", " + this.pos[i]);
		}
		buff.append(")");
		return buff.toString();
	}
}
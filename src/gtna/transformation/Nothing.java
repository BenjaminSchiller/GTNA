package gtna.transformation;

import gtna.graph.Graph;

/**
 * Transformation that does nothing.
 * 
 * @author benni
 * 
 */
public class Nothing extends TransformationImpl implements Transformation {
	public Nothing() {
		super("NOTHING", new String[] {}, new String[] {});
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		return g;
	}

}

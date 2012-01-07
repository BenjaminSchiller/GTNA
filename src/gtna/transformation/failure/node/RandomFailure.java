package gtna.transformation.failure.node;

import gtna.graph.Node;
import gtna.transformation.failure.NodeFailure;

import java.util.Random;

public class RandomFailure extends NodeFailure{

	public RandomFailure(int failure) {
		super("RandomFailure", new String[]{"FAILURE"}, new String[] {""+failure});
		this.failures = failure;
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.failure.NodeFailure#getDeletedSet(gtna.graph.Node[], boolean[])
	 */
	@Override
	public int[] getDeletedSet(Node[] nodes, boolean[] deleted) {
		int[] fails = new int[this.failures];
		int pos = 0;
		int count = 0;
		Random rand = new Random();
		int next;
		while (pos < fails.length && count < nodes.length){
			next = rand.nextInt(nodes.length);
			if (!deleted[next]){
				deleted[next] = true;
				fails[pos] = next;
				pos++;
				count = 0;
			} else {
				count++;
			}
		}
		return fails;
	}

	

	

}

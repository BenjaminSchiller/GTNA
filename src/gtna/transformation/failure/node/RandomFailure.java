package gtna.transformation.failure.node;

import gtna.graph.Node;
import gtna.transformation.failure.NodeFailure;

import java.util.Random;

public class RandomFailure extends NodeFailure{

	public RandomFailure(int failure) {
		super("RANDOMFAILURE", new String[]{"FAILURE"}, new String[] {""+failure});
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
		//randomly choose next node from set of nodes not deleted yet, include counter to prevent endless loops if all nodes are deleted
		while (pos < fails.length && count < nodes.length* nodes.length){
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

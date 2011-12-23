package gtna.transformation.failure.node;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.failure.NodeFailure;

import java.util.Random;

public class RandomFailure extends NodeFailure{

	public RandomFailure(double p) {
		super("RandomFailure", new String[]{"PERC"}, new String[] {""+p});
		this.p = p;
	}

	@Override
	public int[] getNewSet(Node[] nodes) {
		int[] nIndex = new int[nodes.length];
		Random rand = new Random();
		int count = 0;
		for (int i = 0; i < nodes.length; i++){
			if (rand.nextDouble() > this.p){
				nIndex[i] = count;
				count++;
			} else {
				nIndex[i] = -1;
			}
		}
		return nIndex;
	}

	

}

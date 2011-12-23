package gtna.transformation.failure.node;

import gtna.graph.Node;
import gtna.transformation.failure.NodeFailure;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class LargestFailure extends NodeFailure {

	public LargestFailure(double p) {
		super("LargestFailure", new String[]{"PERC"}, new String[] {""+p});
		this.p = p;
	}

	@Override
	public int[] getNewSet(Node[] nodes) {
		int[] index = new int[nodes.length];
		int[] degrees = new int[nodes.length];
		for (int i = 0; i < nodes.length; i++){
			degrees[i] = nodes[i].getDegree();
		}
		Arrays.sort(degrees);
		
		int border = (int)Math.floor((1-p)*degrees.length);
		int b = degrees[border];
		Vector<Integer> borderCases = new Vector<Integer>();
		int count = 0;
		for (int j = 0; j < nodes.length; j++){
			if (nodes[j].getDegree() < b){
				index[j] = count;
				count++;
			}
			if (nodes[j].getDegree() == b){
				borderCases.add(j);
			}
			if (nodes[j].getDegree() > b){
				index[j] = -1;
			}
		}
		Random rand = new Random();
		for (int i = count; i <= border; i++){
			index[borderCases.remove(rand.nextInt(borderCases.size()))] = i;
		}
		return index;
	}

	
	
	

}

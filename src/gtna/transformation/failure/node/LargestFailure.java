package gtna.transformation.failure.node;

import gtna.graph.Node;
import gtna.transformation.failure.NodeFailure;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

/**
 * largest degree node fail
 * @author stef
 *
 */

public class LargestFailure extends NodeFailure {

	public LargestFailure(int failure) {
		super("LargestFailure", new String[]{"FAILURE"}, new String[] {""+failure});
		this.failures = failure;
	}

	@Override
	public int[] getDeletedSet(Node[] nodes, boolean[] deleted) {
		LinkedList<Vector<Integer>> list = new LinkedList<Vector<Integer>>();
		LinkedList<Integer> countList = new LinkedList<Integer>();
		Vector<Integer> current;
		int size = 0;
		for (int i = 0; i < nodes.length; i++){
			if (deleted[i]){
				continue;
			}
			int c = -1;
			int index = list.size() -1;
			while (countList.get(index) <= nodes[i].getDegree() && index > -1){
				c = countList.get(index);
				index--;
			}
			if (c == nodes[i].getDegree()){
				list.get(index+1).add(i);
				size++;
			} else {
				if (c < 0){
					if (size < this.failures){
						current = new Vector<Integer>();
						current.add(i);
						list.add(current);
						size++;
					} 
				} else {
					current = new Vector<Integer>();
					current.add(i);
					list.add(index+1,current);
					size++;
				}
			}
		}
		
		int[] fails = new int[this.failures];
		int i = 0;
		int pos = 0;
		Random rand = new Random();
		while (i < list.size() && pos < fails.length){
			current = list.get(i);
			while (current.size() > 0 && pos < fails.length){
				fails[pos] = current.remove(rand.nextInt(current.size()));
				pos++;
			}
			i++;
		}
		return fails;
	}

	
	
	

}

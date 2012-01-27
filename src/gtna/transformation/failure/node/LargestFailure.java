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
		super("LARGESTFAILURE", new String[]{"FAILURE"}, new String[] {""+failure});
		this.failures = failure;
	}

	@Override
	public int[] getDeletedSet(Node[] nodes, boolean[] deleted) {
		//a list of nodes that should be deleted, nodes of equal degree are in one vector
		LinkedList<Vector<Integer>> list = new LinkedList<Vector<Integer>>();
		//corresponding degree for each of the above vectors
		LinkedList<Integer> countList = new LinkedList<Integer>();
		Vector<Integer> current;
		//number of nodes contained in list at this point
		int size = 0;
		for (int i = 0; i < nodes.length; i++){
			//exclude deleted nodes
			if (deleted[i]){
				continue;
			}
			//first node
			if (list.size() == 0){
				current = new Vector<Integer>();
				current.add(i);
				list.add(current);
				size++;
				countList.add(nodes[i].getDegree());
				continue;
			}
			//get index at which node should be added, if it is to be added
			int c = -1;
			int index = list.size() -1;
			while (index > -1 && countList.get(index) <= nodes[i].getDegree()){
				c = countList.get(index);
				index--;
			}
			
			
			if (c == nodes[i].getDegree()){
				//add node to an existing vector
				list.get(index+1).add(i);
				size++;
			} else {
				if (c < 0){
					if (size < this.failures){
						//add node to the end of the list if not enough nodes have been selected so far
						current = new Vector<Integer>();
						current.add(i);
						list.add(current);
						countList.add(nodes[i].getDegree());
						size++;
					} 
				} else {
					//add node in correct place, creating new vector
					current = new Vector<Integer>();
					current.add(i);
					list.add(index+1,current);
					countList.add(index+1, nodes[i].getDegree());
					size++;
				}
			}
		}
		
		int[] fails = new int[this.failures];
		int i = 0;
		int pos = 0;
		Random rand = new Random();
		//build list of failed nodes
		while (i < list.size() && pos < fails.length){
			current = list.get(i);
			while (current.size() > 0 && pos < fails.length){
				//randomize the order of nodes with an equal degree
				fails[pos] = current.remove(rand.nextInt(current.size()));
				pos++;
			}
			i++;
		}
		return fails;
	}

	
	
	

}

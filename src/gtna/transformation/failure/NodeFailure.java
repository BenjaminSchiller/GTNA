package gtna.transformation.failure;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.Random;

public abstract class NodeFailure extends Failure{

	public NodeFailure(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}
	
	@Override
	public Graph transform(Graph g) {
		Graph gNew = new Graph(g.getName());
		Node[] nodes = g.getNodes();
		int[] nIndex = this.getNewSet(nodes);
		Node[] nNew = new Node[nIndex.length];
		int nr;
		for (int i = 0; i < nodes.length; i++){
			if (nIndex[i] > -1){
				nNew[nIndex[i]] = nodes[i]; 
			}
		}
		
		
		Edges edges = new Edges(nNew, g.computeNumberOfEdges());
		for (int i = 0; i < nNew.length; i++){
			int[] neighbors = nNew[i].getIncomingEdges();
			for (int j = 0; j < neighbors.length; j++){
				nr = neighbors[j];
				if (nIndex[nr] > -1){
					edges.add(nIndex[nr], i);
				}
			}
			
			neighbors = nNew[i].getOutgoingEdges();
			for (int j = 0; j < neighbors.length; j++){
				nr = neighbors[j];
				if (nIndex[nr] > -1){
					edges.add(i,nIndex[nr]);
				}
			}
		}
		gNew.setNodes(nNew);
		edges.fill();
		return gNew;
	}
	
	public abstract int[] getNewSet(Node[] nodes);

}

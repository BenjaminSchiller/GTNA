package gtna.transformation.failure;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;

public abstract class NodeFailure extends Failure{
	

	public NodeFailure(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
		
	}
	
	@Override
	public Graph transform(Graph g) {
        Deleted p =  (Deleted) g.getProperty("Deleted_"+this.key());
        if (p == null){
        	p = new Deleted(g);
        	g.addProperty("Deleted_"+this.key(), p);
        }
		p.deleteNodes(this.getDeletedSet(g.getNodes(), p.getDeleted()));
        
		return g;
	}
	
	public abstract int[] getDeletedSet(Node[] nodes, boolean[] deleted);

}

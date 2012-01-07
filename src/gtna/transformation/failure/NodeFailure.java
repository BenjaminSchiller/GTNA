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
		GraphProperty[] prop = g.getProperties("Deleted");
		Deleted p = null;
		if (prop.length > 0){
           p =  (Deleted)prop[prop.length-1] ;
		}
        if (p == null || p.isClosed()){
        	p = new Deleted(g);
        	g.addProperty(g.getNextKey("Deleted"), p);
        }
		p.deleteNodes(this.getDeletedSet(g.getNodes(), p.getDeleted()));
        
		return g;
	}
	
	public abstract int[] getDeletedSet(Node[] nodes, boolean[] deleted);

}

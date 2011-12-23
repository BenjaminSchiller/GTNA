package gtna.transformation.failure;

import gtna.graph.Graph;
import gtna.transformation.TransformationImpl;

public abstract class Failure extends TransformationImpl {
	protected double p;
	

	public Failure(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}


   public double getPercentage(){
	   return this.p;
   }

	
	
	
	

	@Override
	public boolean applicable(Graph g) {
		// TODO Auto-generated method stub
		return true;
	}

}

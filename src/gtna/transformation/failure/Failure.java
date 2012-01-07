package gtna.transformation.failure;

import gtna.graph.Graph;
import gtna.transformation.TransformationImpl;

public abstract class Failure extends TransformationImpl {
	protected int failures;
	

	public Failure(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
	}


   public int getFailures(){
	   return this.failures;
   }

	
	
	
	

	@Override
	public boolean applicable(Graph g) {
		// TODO Auto-generated method stub
		return true;
	}

}

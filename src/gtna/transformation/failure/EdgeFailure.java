package gtna.transformation.failure;

import gtna.transformation.failure.Failure;

/**
 * abstract class for edge failures
 * @author stef
 *
 */

public abstract class EdgeFailure extends Failure {

	public EdgeFailure(String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
		// TODO Auto-generated constructor stub
	}

}

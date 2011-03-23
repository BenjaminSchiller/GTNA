package gtna.metrics.networkFragmentation;

import gtna.metrics.Metric;

public class NetworkFragmentationUR extends NetworkFragmentation implements Metric {
	public NetworkFragmentationUR(){
		super(NetworkFragmentation.RANDOM, false);
	}
}

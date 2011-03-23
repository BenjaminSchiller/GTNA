package gtna.metrics.networkFragmentation;

import gtna.metrics.Metric;

public class NetworkFragmentationBR extends NetworkFragmentation implements Metric {
	public NetworkFragmentationBR(){
		super(NetworkFragmentation.RANDOM, true);
	}
}

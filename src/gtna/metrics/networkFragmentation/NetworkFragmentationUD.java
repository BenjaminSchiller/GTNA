package gtna.metrics.networkFragmentation;

import gtna.metrics.Metric;

public class NetworkFragmentationUD extends NetworkFragmentation implements Metric {
	public NetworkFragmentationUD() {
		super(NetworkFragmentation.DEGREE_DESC, false);
	}
}

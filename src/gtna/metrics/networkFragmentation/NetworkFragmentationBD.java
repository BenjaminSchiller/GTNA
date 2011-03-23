package gtna.metrics.networkFragmentation;

import gtna.metrics.Metric;

public class NetworkFragmentationBD extends NetworkFragmentation implements Metric {
	public NetworkFragmentationBD() {
		super(NetworkFragmentation.DEGREE_DESC, true);
	}
}

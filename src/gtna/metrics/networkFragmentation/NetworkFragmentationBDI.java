package gtna.metrics.networkFragmentation;

import gtna.metrics.Metric;

public class NetworkFragmentationBDI extends NetworkFragmentation implements Metric {
	public NetworkFragmentationBDI(){
		super(NetworkFragmentation.IN_DEGREE_DESC, true);
	}
}

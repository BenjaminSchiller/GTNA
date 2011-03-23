package gtna.metrics.networkFragmentation;

import gtna.metrics.Metric;

public class NetworkFragmentationUDI extends NetworkFragmentation implements Metric {
	public NetworkFragmentationUDI(){
		super(NetworkFragmentation.IN_DEGREE_DESC, false);
	}
}

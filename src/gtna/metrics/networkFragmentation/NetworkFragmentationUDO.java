package gtna.metrics.networkFragmentation;

import gtna.metrics.Metric;

public class NetworkFragmentationUDO extends NetworkFragmentation implements Metric {
	public NetworkFragmentationUDO(){
		super(NetworkFragmentation.OUT_DEGREE_DESC, false);
	}
}

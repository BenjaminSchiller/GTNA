package gtna.metrics.networkFragmentation;

import gtna.metrics.Metric;

public class NetworkFragmentationBDO extends NetworkFragmentation implements Metric {
	public NetworkFragmentationBDO(){
		super(NetworkFragmentation.OUT_DEGREE_DESC, true);
	}
}

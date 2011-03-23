package gtna.metrics.SPUnderAttack;

import gtna.metrics.Metric;
import gtna.metrics.networkFragmentation.NetworkFragmentation;

public class SPUnderAttackD extends SPUnderAttack implements Metric {
	public SPUnderAttackD() {
		super(NetworkFragmentation.DEGREE_DESC);
	}
}

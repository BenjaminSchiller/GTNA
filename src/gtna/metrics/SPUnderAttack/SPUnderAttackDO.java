package gtna.metrics.SPUnderAttack;

import gtna.metrics.Metric;
import gtna.metrics.networkFragmentation.NetworkFragmentation;

public class SPUnderAttackDO extends SPUnderAttack implements Metric {
	public SPUnderAttackDO() {
		super(NetworkFragmentation.OUT_DEGREE_DESC);
	}
}

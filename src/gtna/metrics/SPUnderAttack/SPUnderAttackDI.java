package gtna.metrics.SPUnderAttack;

import gtna.metrics.Metric;
import gtna.metrics.networkFragmentation.NetworkFragmentation;

public class SPUnderAttackDI extends SPUnderAttack implements Metric {
	public SPUnderAttackDI() {
		super(NetworkFragmentation.IN_DEGREE_DESC);
	}
}

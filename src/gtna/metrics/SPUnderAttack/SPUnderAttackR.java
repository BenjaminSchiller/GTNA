package gtna.metrics.SPUnderAttack;

import gtna.metrics.Metric;
import gtna.metrics.networkFragmentation.NetworkFragmentation;

public class SPUnderAttackR extends SPUnderAttack implements Metric {
	public SPUnderAttackR() {
		super(NetworkFragmentation.RANDOM);
	}
}

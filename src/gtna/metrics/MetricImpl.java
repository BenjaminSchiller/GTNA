package gtna.metrics;

import gtna.util.Config;

public abstract class MetricImpl implements Metric{
	private String key;

	public MetricImpl(String key) {
		this.key = key;
	}

	public String[] dataPlots() {
		return Config.keys(this.key + "_DATA_PLOTS");
	}

	public String[] singlesPlots() {
		return Config.keys(this.key + "_SINGLES_PLOTS");
	}

	public String[] dataKeys() {
		return Config.keys(this.key + "_DATA_KEYS");
	}

	public String[] singlesKeys() {
		return Config.keys(this.key + "_SINGLES_KEYS");
	}

	public String name() {
		return Config.get(key + "_NAME");
	}

	public String key() {
		return this.key;
	}
}

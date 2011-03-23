package gtna.data;

public class Value {
	public String key;

	public double value;

	public Value(String key, double value) {
		this.key = key;
		this.value = value;
	}

	public static double get(Value[] values, String key) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null && key.equals(values[i].key)) {
				return values[i].value;
			}
		}
		return Double.NaN;
	}

	public String toString() {
		return this.key + " = " + this.value;
	}
}

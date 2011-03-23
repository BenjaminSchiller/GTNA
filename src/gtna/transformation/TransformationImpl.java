package gtna.transformation;

import gtna.util.Config;

/**
 * Implements all basic features of a transformation. It is recommended to
 * always extend this class when implementing a new transformation.
 * 
 * @author benni
 * 
 */
public abstract class TransformationImpl implements Transformation {
	private String key;

	private String[] configKeys;

	private String[] configValues;

	/**
	 * Constructor that must be used by any implementation of a transformation.
	 * 
	 * @param key
	 *            key of the transformation, used for the configuration of the
	 *            transformation
	 * @param configKeys
	 *            keys of the transformation's parameters, also used for the
	 *            configuration
	 * @param configValues
	 *            value for each parameter
	 */
	public TransformationImpl(String key, String[] configKeys,
			String[] configValues) {
		this.key = key;
		this.configKeys = configKeys;
		this.configValues = configValues;
	}
	
	
	public String compareName(Transformation t, String key) {
		if (!this.getClass().equals(t.getClass())) {
			return Config.get("TRANSFORMATION_NOT_COMPARABLE");
		}
		for (int i = 0; i < this.configValues.length; i++) {
			if (!this.configValues[i].equals(t.configValues()[i])) {
				return Config.get(this.key + "_" + this.configKeys[i] + "_NAME"
						+ key);
			}
		}
		return Config.get("TRANSFORMATION_COMPARE_SAME_NAME");
	}

	public String compareValue(Transformation t) {
		if (!this.getClass().equals(t.getClass())) {
			return Config.get("TRANSFORMATION_NOT_COMPARABLE");
		}
		for (int i = 0; i < this.configValues.length; i++) {
			if (!this.configValues[i].equals(t.configValues()[i])) {
				return this.configValues[i];
			}
		}
		return Config.get("TRANSFORMATION_COMPARE_SAME_VALUE");
	}

	public String key() {
		return this.key;
	}

	public String[] configKeys() {
		return this.configKeys;
	}

	public String[] configValues() {
		return this.configValues;
	}

	public String folder() {
		String folder = Config.get(this.key + "_FOLDER");
		for (int i = 0; i < this.configValues.length; i++) {
			folder += "-" + this.configValues[i];
		}
		return folder;
	}

	public String name() {
		return this.name("");
	}

	public String nameLong() {
		return this.name("_LONG");
	}

	public String nameShort() {
		return this.name("_SHORT");
	}

	private String name(String key) {
		String name = Config.get(this.key + "_NAME" + key);
		for (int i = 0; i < this.configKeys.length; i++) {
			name += "-"
					+ Config.get(this.key + "_" + this.configKeys[i] + "_NAME"
							+ key);
			name += "=" + this.configValues[i];
		}
		return name;
	}

}

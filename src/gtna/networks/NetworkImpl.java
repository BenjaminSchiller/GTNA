package gtna.networks;

import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.Util;

/**
 * Implements all basic features of a network generator. It is recommended to
 * always extend this class when implementing a new network generator.
 * 
 * @author benni
 * 
 */
public abstract class NetworkImpl implements Network {
	private String name;

	private String[] configValues;

	private String[] configKeys;

	private String[] folderValues;

	private String folder;

	private int nodes;

	private int edges;

	private boolean connected = true;

	private double connectivity;

	private boolean routingFailure = false;

	private double routingSuccess;

	private String key = "";

	private RoutingAlgorithm ra;

	private Transformation[] t;

	/**
	 * Constructor that must be used by any implementation of a network
	 * generator.
	 * 
	 * @param key
	 *            key of the network generator, used for the configuration of
	 *            the network generator
	 * @param nodes
	 *            number of nodes in the network
	 * @param configKeys
	 *            keys of the network's parameters, also used for the
	 *            configuration
	 * @param configValues
	 *            value for each parameter
	 * @param ra
	 *            routing algorithm used by the routing metric
	 * @param t
	 *            transformations to be performed after the generation of a
	 *            specific network instance
	 */
	public NetworkImpl(String key, int nodes, String[] configKeys,
			String[] configValues, RoutingAlgorithm ra, Transformation[] t) {
		this(key, Config.get(key + "_FOLDER"), nodes, configKeys, configValues,
				configValues, ra, t);
		this.key = key;
	}

	/**
	 * Constructor that must be used by any implementation of a network
	 * generator.
	 * 
	 * @param key
	 *            key of the network generator, used for the configuration of
	 *            the network generator
	 * @param nodes
	 *            number of nodes in the network
	 * @param configKeys
	 *            keys of the network's parameters, also used for the
	 *            configuration
	 * @param configValues
	 *            value for each parameter
	 * @param folderValues
	 *            values used for generating the deterministic storage location
	 *            of the series - in the other constructor, configValcues is
	 *            used for this!
	 * @param ra
	 *            routing algorithm used by the routing metric
	 * @param t
	 *            transformations to be performed after the generation of a
	 *            specific network instance
	 */
	public NetworkImpl(String key, int nodes, String[] configKeys,
			String[] configValues, String[] folderValues, RoutingAlgorithm ra,
			Transformation[] t) {
		this(key, Config.get(key + "_FOLDER"), nodes, configKeys, configValues,
				folderValues, ra, t);
		this.key = key;
	}

	private NetworkImpl(String key, String folder, int nodes,
			String[] configKeys, String[] configValues, String[] folderValues,
			RoutingAlgorithm ra, Transformation[] t) {
		this.nodes = nodes;
		this.key = key;
		this.name = Config.get(key + "_NAME");
		this.configKeys = configKeys;
		this.configValues = configValues;
		this.folderValues = configValues;
		this.ra = ra;
		this.t = t;
		if (this.t == null) {
			this.t = new Transformation[] {};
		}
		this.folder = folder;
		for (int i = 0; i < this.folderValues.length; i++) {
			this.folder += "-" + this.folderValues[i];
		}
		if (this.ra != null) {
			this.folder += "-" + this.ra.folder();
		}
		for (int i = 0; i < this.t.length; i++) {
			this.folder += "-" + this.t[i].folder();
		}
		this.folder += Config.get("FILESYSTEM_FOLDER_DELIMITER");
	}

	public int nodes() {
		return this.nodes;
	}

	public int edges() {
		return this.edges;
	}

	public boolean isConnected() {
		return this.connected;
	}

	public double getConnectivity() {
		return this.connectivity;
	}

	public boolean isRoutingFailure() {
		return this.routingFailure;
	}

	public double getRoutingSuccess() {
		return this.routingSuccess;
	}

	public String name() {
		return this.name;
	}

	public String folder() {
		return this.folder;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void setConnectivity(double connectivity) {
		this.connectivity = connectivity;
	}

	public void setRoutingFailure(boolean routingFailure) {
		this.routingFailure = routingFailure;
	}

	public void setRoutingSuccess(double routingSuccess) {
		this.routingSuccess = routingSuccess;
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
	}

	public void setEdges(int edges) {
		this.edges = edges;
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

	public String[] folderValues() {
		return this.folderValues;
	}

	public RoutingAlgorithm routingAlgorithm() {
		return this.ra;
	}

	public Transformation[] transformations() {
		return this.t;
	}

	/**
	 * COMPARE
	 */

	public String compareName(Network nw) {
		return this.compareName(nw, "");
	}

	public String compareNameShort(Network nw) {
		return this.compareName(nw, "_SHORT");
	}

	public String compareNameLong(Network nw) {
		return this.compareName(nw, "_LONG");
	}

	public String compareName(Network nw, String key) {
		if (!this.getClass().equals(nw.getClass())) {
			return Config.get("NETWORK_COMPARE_NETWORKS_NAME");
		}
		for (int i = 0; i < this.configValues.length; i++) {
			if (!this.configValues[i].equals(nw.configValues()[i])) {
				return Config.get(this.key + "_" + this.configKeys[i] + "_NAME"
						+ key);
			}
		}
		if (this.nodes != nw.nodes()) {
			return Config.get("NETWORK_COMPARE_NODES_NAME" + key);
		}
		if (this.ra != null && nw.routingAlgorithm() != null) {
			String compared = this.ra.compareName(nw.routingAlgorithm(), key);
			if (!compared.equals(Config
					.get("ROUTING_ALGORITHM_COMPARE_SAME_NAME"))) {
				return compared;
			}
		}
		for (int i = 0; i < Math
				.min(this.t.length, nw.transformations().length); i++) {
			String compared = this.t[i].compareName(nw.transformations()[i],
					key);
			if (!compared
					.equals(Config.get("TRANSFORMATION_COMPARE_SAME_NAME"))) {
				return compared;
			}
		}
		return Config.get("NETWORK_COMPARE_SAME_NAME");
	}

	public String compareValue(Network nw) {
		if (!this.getClass().equals(nw.getClass())) {
			return Config.get("NETWORK_COMPARE_NETWORKS_VALUE");
		}
		for (int i = 0; i < this.configValues.length; i++) {
			if (!this.configValues[i].equals(nw.configValues()[i])) {
				return this.configValues[i];
			}
		}
		if (this.nodes != nw.nodes()) {
			return "" + this.nodes;
		}
		if (this.ra != null && nw.routingAlgorithm() != null) {
			String compared = this.ra.compareValue(nw.routingAlgorithm());
			if (!compared.equals(Config
					.get("ROUTING_ALGORITHM_COMPARE_SAME_VALUE"))) {
				return compared;
			}
		}
		for (int i = 0; i < Math
				.min(this.t.length, nw.transformations().length); i++) {
			String compared = this.t[i].compareValue(nw.transformations()[i]);
			if (!compared.equals(Config
					.get("TRANSFORMATION_COMPARE_SAME_VALUE"))) {
				return compared;
			}
		}
		return Config.get("NETWORK_COMPARE_SAME_VALUE");
	}

	/**
	 * DESCRIPTION
	 */

	public String description() {
		return this.makeDescription("DEFAULT", (Network) this, (Network) this);
	}

	public String description(Network compare) {
		return this.makeDescription("DEFAULT", compare, compare);
	}

	public String description(Network compare1, Network compare2) {
		return this.makeDescription("DEFAULT", compare1, compare2);
	}

	public String description(String key) {
		return this.makeDescription(key, (Network) this, (Network) this);
	}

	public String description(String key, Network compare) {
		return this.makeDescription(key, compare, compare);
	}

	public String description(String key, Network compare1, Network compare2) {
		return this.makeDescription(key, compare1, compare2);
	}

	protected static String file(String filename) {
		return filename.replace("../", "").replace("./", "").replace("/", "-");
	}

	private String makeDescription(String key, Network x, Network y) {
		if (!this.getClass().equals(x.getClass())
				|| !this.getClass().equals(y.getClass())) {
			return Config.get("NETWORK_COMPARE_NETWORKS_DESCRIPTION");
		}
		String pre = key + "_NETWORK_DESCRIPTION";

		String connected = null;
		if (this.isConnected()) {
			connected = Config.get(pre + "_CONNECTED").replace("%CONNECTIVITY",
					"" + Util.round(this.getConnectivity(), 2));
		} else {
			connected = Config.get(pre + "_NOT_CONNECTED")
					.replace("%CONNECTIVITY",
							"" + Util.round(this.getConnectivity(), 2));
		}
		String routingFailure = null;
		if (this.isRoutingFailure()) {
			routingFailure = Config.get(pre + "_ROUTING_FAILURE").replace(
					"%ROUTING_SUCCESS",
					"" + Util.round(this.getRoutingSuccess(), 2));
		} else {
			routingFailure = Config.get(pre + "_NO_ROUTING_FAILURE").replace(
					"%ROUTING_SUCCESS",
					"" + Util.round(this.getRoutingSuccess(), 2));
		}

		StringBuffer buff = new StringBuffer();
		String start = Config.get(pre + "_CONFIG_START");
		String end = Config.get(pre + "_CONFIG_END");
		String separator = Config.get(pre + "_CONFIG_SEPARATOR");
		String middle = Config.get(pre + "_CONFIG_MIDDLE");
		String mode = Config.get(pre + "_CONFIG_MODE");
		String ra = Config.get(pre + "_ROUTING_ALGORITHM");
		String t = Config.get(pre + "_TRANSFORMATIONS");
		for (int i = 0; i < this.configValues.length; i++) {
			if (i > 0) {
				buff.append(separator);
			}
			if ("VALUES_ONLY".equals(mode)) {
				buff.append(this.configValues[i]);
			} else {
				String name = Config.get(this.key + "_" + this.configKeys[i]
						+ "_NAME_" + mode);
				String value = this.configValues[i];
				if (!this.configValues[i].equals(x.configValues()[i])) {
					value = Config.get("NETWORK_COMPARE_DIFFERENT_VALUE_X");
				} else if (!this.configValues[i].equals(y.configValues()[i])) {
					value = Config.get("NETWORK_COMPARE_DIFFERENT_VALUE_Y");
				}
				buff.append(name + middle + value);
			}
		}
		String config = null;
		if (this.configValues.length > 0) {
			config = start + buff.toString() + end;
		} else {
			config = buff.toString();
		}

		String description = Config.get(pre);
		String nodes = this.nodes() + "";
		String edges = this.edges + "";
		if (this.nodes != x.nodes()) {
			nodes = Config.get("NETWORK_COMPARE_DIFFERENT_NODES_X");
		} else if (this.nodes != y.nodes()) {
			nodes = Config.get("NETWORK_COMPARE_DIFFERENT_NODES_Y");
		}
		if (this.edges != x.edges()) {
			edges = Config.get("NETWORK_COMPARE_DIFFERENT_EDGES_X");
		} else if (this.edges != y.edges()) {
			edges = Config.get("NETWORK_COMPARE_DIFFERENT_EDGES_Y");
		}
		if (this.routingAlgorithm() == null) {
			ra = "";
		} else if (mode.equals("LONG")) {
			ra = ra.replace("%RA", this.routingAlgorithm().nameLong());
		} else if (mode.equals("SHORT")) {
			ra = ra.replace("%RA", this.routingAlgorithm().nameShort());
		} else {
			ra = ra.replace("%RA", this.routingAlgorithm().name());
		}
		String ts = "";
		for (int i = 0; i < this.t.length; i++) {
			if (i > 0) {
				ts += Config.get(pre + "_TRANSFORMATIONS_SEPARATOR");
			}
			if (mode.equals("LONG")) {
				ts += this.t[i].nameLong();
			} else if (mode.equals("SHORT")) {
				ts += this.t[i].nameShort();
			} else {
				ts += this.t[i].name();
			}
		}
		if (ts.equals("")) {
			t = "";
		} else {
			t = t.replace("%T", ts);
		}
		description = description.replace("%NAME", this.name());
		description = description.replace("%NODES", nodes);
		description = description.replace("%EDGES", edges);
		description = description.replace("%CONNECTED", connected);
		description = description.replace("%ROUTING_FAILURE", routingFailure);
		description = description.replace("%CONFIG", config);
		description = description.replace("%RA", ra);
		description = description.replace("%T", t);
		return description;
	}
}

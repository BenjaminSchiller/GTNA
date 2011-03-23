package gtna.data;

import gtna.graph.Graph;
import gtna.io.GraphWriter;
import gtna.io.Output;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.Timer;

import java.io.File;
import java.util.Hashtable;

public class Series {
	private Network network;

	private String folder;

	private String[] dataFolders;

	private String[] graphFolders;

	private String averageDataFolder;

	private String confidenceDataFolder;

	private String singlesFilename;

	private Singles averageSingles;

	private Series(Network network) {
		this.network = network;
		this.folder = Config.get("MAIN_DATA_FOLDER") + network.nodes()
				+ Config.get("FILESYSTEM_FOLDER_DELIMITER") + network.folder();
		this.averageDataFolder = this.folder
				+ Config.get("SERIES_AVERAGE_DATA_FOLDER");
		this.confidenceDataFolder = this.folder
				+ Config.get("SERIES_CONFIDENCE_DATA_FOLDER");
		this.singlesFilename = this.folder
				+ Config.get("SERIES_SINGLES_FILENAME");
		if ((new File(this.folder + Config.get("SERIES_AVERAGE_FILENAME")))
				.exists()) {
			this.averageSingles = new Singles(this.folder
					+ Config.get("SERIES_AVERAGE_FILENAME"));
			this.process();
		}
	}

	private void process() {
		if (this.averageSingles.getValue("EDGES") > 0) {
			this.network.setEdges((int) this.averageSingles.getValue("EDGES"));
		}
		if (this.averageSingles.getValue("NODES") > 0) {
			this.network.setNodes((int) this.averageSingles.getValue("NODES"));
		}
		if (this.averageSingles.getValue("CONN") >= 0) {
			this.network
					.setConnected(this.averageSingles.getValue("CONN") == 1.0);
			this.network.setConnectivity(this.averageSingles.getValue("CONN"));
		}
		if (this.averageSingles.getValue("RL_FR") >= 0) {
			this.network.setRoutingFailure(this.averageSingles
					.getValue("RL_FR") > 0);
			this.network.setRoutingSuccess(1 - this.averageSingles
					.getValue("RL_FR"));
		}
	}

	public static Series[][] get(Network[][] networks) {
		Series[][] s = new Series[networks.length][];
		for (int i = 0; i < networks.length; i++) {
			s[i] = get(networks[i]);
		}
		return s;
	}

	public static Series[] get(Network[] networks) {
		Series[] s = new Series[networks.length];
		for (int i = 0; i < networks.length; i++) {
			s[i] = get(networks[i]);
			if (s[i] == null) {
				return null;
			}
		}
		return s;
	}

	public static Series get(Network network) {
		Series s = new Series(network);
		int counter = 0;
		while ((new File(s.folder + counter
				+ Config.get("FILESYSTEM_FOLDER_DELIMITER"))).exists()) {
			counter++;
		}
		if (counter == 0) {
			return null;
		}
		s.graphFolders = new String[counter];
		s.dataFolders = new String[counter];
		for (int i = 0; i < counter; i++) {
			s.graphFolders[i] = s.folder + i
					+ Config.get("FILESYSTEM_FOLDER_DELIMITER");
			s.dataFolders[i] = s.graphFolders[i]
					+ Config.get("GRAPH_DATA_FOLDER");
		}
		return s;
	}

	public static Series[][] generate(Network[][] networks, int times) {
		Series[][] s = new Series[networks.length][];
		for (int i = 0; i < networks.length; i++) {
			s[i] = generate(networks[i], times);
		}
		return s;
	}

	public static Series[] generate(Network[] networks, int times) {
		Series[] s = new Series[networks.length];
		for (int i = 0; i < networks.length; i++) {
			s[i] = generate(networks[i], times);
		}
		return s;
	}

	public static Series generate(Network n, int times) {
		Series s = new Series(n);
		s.dataFolders = new String[times];
		s.graphFolders = new String[times];

		Output.open(s.folder + Config.get("SERIES_OUTPUT_FILENAME"));
		Output.writelnDelimiter();
		Output.writeIndent();
		String out = Config.get("SERIES_GENERATION");
		out = out.replace("%TIMES", "" + times);
		out = out.replace("%NETWORK", n.description());
		Output.writeln(out);
		Output.writelnDelimiter();

		String averageFilename = s.folder
				+ Config.get("SERIES_AVERAGE_FILENAME");

		String averageOutput = Config.get("AVERAGE_DATA").replace("%GRAPHS",
				times + "");
		String confidenceOutput = Config.get("CONFIDENCE_DATA").replace(
				"%GRAPHS", times + "");
		String avgSumOutput = Config.get("SINGLES_WRITER_OUTPUT").replace(
				"%FILENAME", averageFilename);
		String summaryOutput = Config.get("SINGLES_WRITER_OUTPUT").replace(
				"%FILENAME", s.singlesFilename);

		Metric[] metrics = Config.getMetrics();
		boolean error = false;
		for (int i = 0; i < metrics.length; i++) {
			String req = Config.get(metrics[i].key() + "_DEPENDENCY");
			if (req != null) {
				String[] required = req.split(Config
						.get("CONFIG_LIST_SEPARATOR"));
				for (int a = 0; a < required.length; a++) {
					boolean found = false;
					for (int b = 0; b < i; b++) {
						if (metrics[b].key().equals(required[a])) {
							found = true;
						}
					}
					if (!found) {
						String err = Config.get("METRICS_MISSING_PREDECESSOR");
						err = err.replace("%METRIC", metrics[i].key());
						err = err.replace("%PREDECESSOR", required[a]);
						System.out.println(err);
						error = true;
					}
				}
			}
		}
		if (error) {
			return null;
		}
		int maxLength = 0;
		for (int j = 0; j < metrics.length; j++) {
			if (metrics[j].name().length() > maxLength) {
				maxLength = metrics[j].name().length();
			}
		}
		if (averageOutput.length() > maxLength) {
			maxLength = averageOutput.length();
		}
		if (confidenceOutput.length() > maxLength) {
			maxLength = confidenceOutput.length();
		}
		if (avgSumOutput.length() > maxLength) {
			maxLength = avgSumOutput.length();
		}
		if (summaryOutput.length() > maxLength) {
			maxLength = summaryOutput.length();
		}

		for (int i = 0; i < times; i++) {
			int gc = Config.getInt("TIMES_TO_CALL_GARBAGE_COLLECTOR");
			for (int j = 0; j < gc; j++) {
				System.gc();
			}

			s.graphFolders[i] = s.folder + i
					+ Config.get("FILESYSTEM_FOLDER_DELIMITER");
			s.dataFolders[i] = s.graphFolders[i]
					+ Config.get("GRAPH_DATA_FOLDER");

			String singlesFilename = s.graphFolders[i]
					+ Config.get("GRAPH_SINGLES_FILENAME");
			String graphFilename = s.graphFolders[i]
					+ Config.get("GRAPH_FILENAME");
			String graphInfoFilename = s.graphFolders[i]
					+ Config.get("GRAPH_INFO_FILENAME");
			// String graphCoordinatesFilename = s.graphFolders[i]
			// + Config.get("GRAPH_COORDINATES_FILENAME");

			String networkOutput = Config.get("NETWORK_GENERATION").replace(
					"%NETWORK", n.description());
			String singlesOutput = "  "
					+ Config.get("SINGLES_WRITER_OUTPUT").replace("%FILENAME",
							singlesFilename);
			String graphOutput = "  "
					+ Config.get("GRAPH_WRITER_OUTPUT").replace("%FILENAME",
							graphFilename);
			String graphInfoOutput = "  "
					+ Config.get("GRAPH_WRITER_OUTPUT").replace("%FILENAME",
							graphInfoFilename);
			// String graphCoordinatesOutput = "  "
			// + Config.get("GRAPH_WRITER_OUTPUT").replace("%FILENAME",
			// graphCoordinatesFilename);
			if (networkOutput.length() > maxLength) {
				maxLength = networkOutput.length();
			}
			if (singlesOutput.length() > maxLength) {
				maxLength = singlesOutput.length();
			}
			if (graphOutput.length() > maxLength) {
				maxLength = graphOutput.length();
			}

			if (i > 0) {
				Output.writeln("");
			}

			Timer networkTimer = new Timer(networkOutput
					+ fill(maxLength - networkOutput.length()));
			Graph g = n.generate();
			Transformation[] t = n.transformations();
			for (int j = 0; j < t.length; j++) {
				if (t[j].applicable(g)) {
					g = t[j].transform(g);
				}
			}
			networkTimer.end();

			// metrics = Config.getMetrics();
			Hashtable<String, Metric> computedMetrics = new Hashtable<String, Metric>();
			for (int j = 0; j < metrics.length; j++) {
				Timer timer = new Timer("  - " + metrics[j].name()
						+ fill(maxLength - 4 - metrics[j].name().length()));
				metrics[j].computeData(g, n, computedMetrics);
				metrics[j].writeData(s.dataFolders[i]);
				timer.end();
				computedMetrics.put(metrics[j].key(), metrics[j]);
			}

			Timer swTimer = new Timer(singlesOutput
					+ fill(maxLength - singlesOutput.length()));
			Singles singles = new Singles(n.description(), metrics);
			singles.write(singlesFilename);
			swTimer.end();

			Timer gTimer = new Timer(graphOutput
					+ fill(maxLength - graphOutput.length()));
			GraphWriter.write(g, graphFilename);
			gTimer.end();
			Timer gInfoTimer = new Timer(graphInfoOutput
					+ fill(maxLength - graphInfoOutput.length()));
			GraphWriter.write(g, graphInfoFilename, GraphWriter.INFO_FORMAT);
			gInfoTimer.end();
			// TODO remove???
			// if (g.nodes[i] instanceof IDNode
			// && (((IDNode) g.nodes[i]).id() instanceof GridID)
			// && (((GridID) ((IDNode) g.nodes[i]).id()).x.length == 2)) {
			// Timer gCoordinatesTimer = new Timer(graphCoordinatesOutput
			// + fill(maxLength - graphIDsOutput.length()));
			// GraphWriter.write(g, graphCoordinatesFilename,
			// GraphWriter.GML_WITH_COORDINATES_FORMAT);
			// gCoordinatesTimer.end();
			// }

			for (int j = 0; j < gc; j++) {
				System.gc();
			}
		}
		Output.writelnDelimiter();

		Output.writeIndent();
		Timer avgTimer = new Timer(averageOutput);
		AverageData.generate(s.averageDataFolder, s.dataFolders);
		avgTimer.end();

		Output.writeIndent();
		Timer confTimer = new Timer(confidenceOutput
				+ fill(maxLength - confidenceOutput.length()));
		ConfidenceData.generate(s.confidenceDataFolder, s.dataFolders);
		confTimer.end();

		Singles[][] summaries = new Singles[1][times];
		for (int i = 0; i < times; i++) {
			summaries[0][i] = new Singles(s.folder + i
					+ Config.get("FILESYSTEM_FOLDER_DELIMITER")
					+ Config.get("GRAPH_SINGLES_FILENAME"));
		}
		String[] names = new String[] { n.name() };
		Output.writeIndent();
		Timer summaryTimer = new Timer(summaryOutput);
		Singles.write(summaries, s.singlesFilename, names);
		summaryTimer.end();

		s.averageSingles = Singles.average(s.summaries());

		Output.writeIndent();
		Timer avgSumTimer = new Timer(avgSumOutput);
		s.averageSingles.write(averageFilename);
		avgSumTimer.end();
		s.process();

		Output.writeIndent();
		Output.writelnDelimiter();
		Output.close();

		Output.writeln("\n\n\n");

		return s;
	}

	private static String fill(int length) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buff.append(" ");
		}
		return buff.toString();
	}

	public Singles[] summaries() {
		Singles[] summaries = new Singles[this.graphFolders.length];
		for (int i = 0; i < summaries.length; i++) {
			summaries[i] = new Singles(this.graphFolders[i]
					+ Config.get("GRAPH_SINGLES_FILENAME"));
		}
		return summaries;
	}

	public Singles avgSingles() {
		return this.averageSingles;
	}

	public String avgDataFolder() {
		return this.averageDataFolder;
	}

	public String confDataFolder() {
		return this.confidenceDataFolder;
	}

	public Network network() {
		return this.network;
	}

	public String[] graphFolders() {
		return this.graphFolders;
	}

	public String[] dataFolders() {
		return this.dataFolders;
	}
}

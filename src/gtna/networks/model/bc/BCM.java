package gtna.networks.model.bc;

import gtna.data.Series;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.centrality.BetweennessCentrality;
import gtna.networks.Network;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

public class BCM extends Network {

	public static void main(String[] args) {
		cfg();

		// args = new String[] { "graphs/", "data/", "plots/", "3", "2", "3",
		// "1" };

		if (args.length < 4) {
			System.err.println("expecting at least 5 argument");
			System.err.println("  0 : graph dir (e.g., graphs/)");
			System.err.println("  1 : data dir (e.g., data/)");
			System.err.println("  2 : plot dir (e.g., plots/)");
			// System.err.println("  3 : instances (e.g., 4)");
			System.err
					.println("  3-x : spans of the levels, simply add one argument per level");
			return;
		}

		String graphDir = args[0];
		String dataDir = args[1];
		String plotDir = args[2];
		// int runs = Integer.parseInt(args[3]);
		int runs = 1;

		int[] spans = new int[args.length - 3];
		for (int i = 0; i < args.length - 3; i++) {
			spans[i] = Integer.parseInt(args[i + 3]);
		}

		Config.overwrite("MAIN_DATA_FOLDER", dataDir);
		Config.overwrite("MAIN_PLOT_FOLDER", plotDir);

		Network nw = new BCM(spans);

		for (int i = 0; i < runs; i++) {
			String filename = graphDir + nw.getFolder() + i + ".gtna";
			System.out.println("generating graph " + i + " for "
					+ nw.getDescription());
			Graph g = nw.generate();
			GraphWriter w = new GtnaGraphWriter();
			w.write(g, filename);
			System.out.println("wrote graph to " + filename);
		}

		Network nw_ = new ReadableFolder(nw.getDescription(),
				nw.getFolderName(), graphDir + nw.getFolder(), ".gtna", null);
		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new ShortestPaths(), new BetweennessCentrality(0.01) };
		Series s = Series.generate(nw_, metrics, runs);
		Plotting.multi(s, metrics, nw.getFolderName() + "/");

		// Execute.exec("open " + Config.get("MAIN_PLOT_FOLDER"));
	}

	public static void cfg() {
		cfg("BCM", "BCModel");
		for (int i = 0; i < 30; i++) {
			cfg("BCM_SPAN" + i, "span" + i);
		}
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
	}

	public static void cfg(String key, String value) {
		Config.overwrite(key + "_NAME", value);
		Config.overwrite(key + "_NAME_LONG", value);
		Config.overwrite(key + "_NAME_SHORT", value);
	}

	private int[] spans;

	public BCM(int[] spans, Transformation... transformations) {
		super("BCM", getNodes(spans), getParameters(spans), transformations);
		this.spans = spans;
	}

	protected static int getNodes(int[] spans) {
		int p = 1;
		int sum = 1;
		for (int span : spans) {
			p *= span;
			sum += p;
		}
		return sum;
	}

	protected static String asString(int[] spans) {
		StringBuffer buff = new StringBuffer();
		for (int span : spans) {
			if (buff.length() == 0) {
				buff.append(span);
			} else {
				buff.append("-" + span);
			}
		}
		return buff.toString();
	}

	protected static Parameter[] getParameters(int[] spans) {
		Parameter[] p = new Parameter[spans.length];
		for (int i = 0; i < spans.length; i++) {
			p[i] = new IntParameter("SPAN" + i, spans[i]);
		}
		return p;
	}

	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, this.getNodes());

		int index = 0;
		Node[][] levels = new Node[spans.length + 1][];
		levels[0] = new Node[1];
		levels[0][0] = nodes[index++];
		for (int i = 0; i < spans.length; i++) {
			levels[i + 1] = new Node[spans[i] * levels[i].length];
			for (int j = 0; j < levels[i + 1].length; j++) {
				levels[i + 1][j] = nodes[index++];
				// System.out.println((i + 1) + " : " + j + " => " + (index -
				// 1));
			}
		}

		for (int i = 0; i < spans.length; i++) {
			// System.out.println("LEVEL: " + i + " @ " + levels[i].length);
			for (int j = 0; j < levels[i].length; j++) {
				Node src = levels[i][j];
				// System.out.println("SRC: " + src);
				for (int k = 0; k < spans[i]; k++) {
					Node dst = levels[i + 1][j * spans[i] + k];
					edges.add(src.getIndex(), dst.getIndex());
					edges.add(dst.getIndex(), src.getIndex());
					// System.out.println(i + ":" + j + " -> " + src.getIndex()
					// + "-" + dst.getIndex());
				}
			}
		}

		edges.fill();
		graph.setNodes(nodes);

		return graph;
	}

}

package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.routing.IDRoute;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.node.IDNode;
import gtna.routing.node.RegistrationNode;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;

public class Routing extends MetricImpl implements Metric {
	// local characteristic routing length
	private double[] lcrl;

	// local characteristic routing length (SHORT)
	private double[] lcrlShort;

	// routing length distribution
	private double[] rld;

	// cumulative routing length distribution
	private double[] crld;

	// routing progression
	private double[] prog;

	// copies stored at each node
	private double[] copies;

	// copies stored at each node (SHORT)
	private double[] copiesShort;

	// messages per routing
	private double[] msgs;

	// messages per routing (SHORT)
	private double[] msgsShort;

	public boolean writeData(String folder) {
		DataWriter.writeWithIndex(this.lcrl, "RL_LCRL", folder);
		DataWriter.writeWithIndex(this.lcrlShort, "RL_LCRL_SHORT", folder);
		DataWriter.writeWithIndex(this.rld, "RL_RLD", folder);
		DataWriter.writeWithIndex(this.crld, "RL_CRLD", folder);
		DataWriter.writeWithIndex(this.prog, "RL_PROG", folder);
		DataWriter.writeWithIndex(this.copies, "RL_COPIES", folder);
		DataWriter.writeWithIndex(this.copiesShort, "RL_COPIES_SHORT", folder);
		DataWriter.writeWithIndex(this.msgs, "RL_MSGS", folder);
		DataWriter.writeWithIndex(this.msgsShort, "RL_MSGS_SHORT", folder);
		return true;
	}

	// characteristic routing length
	private double crl;

	// maximum routing length
	private double mrl;

	// fraction of failed routings
	private double fr;

	// average number of copies stored at each node
	private double averageCopies;

	// average number of messages per routing
	private double avgMsgs;

	// timer
	private Timer timer;

	// list of all routes
	private ArrayList<Route> routes;

	public Value[] getValues(Value[] values) {
		Value crl = new Value("RL_CRL", this.crl);
		Value mrl = new Value("RL_MRL", this.mrl);
		Value fr = new Value("RL_FR", this.fr);
		Value avgCopies = new Value("RL_AVG_COPIES", this.averageCopies);
		Value avgMsgs = new Value("RL_AVG_MSGS", this.avgMsgs);
		Value rt = new Value("RL_RT", this.timer.rt());
		return new Value[] { crl, mrl, fr, avgCopies, avgMsgs, rt };
	}

	private void initEmpty() {
		this.lcrl = new double[] { 0.0 };
		this.lcrlShort = new double[] { 0.0 };
		this.rld = new double[] { 0.0 };
		this.crld = new double[] { 0.0 };
		this.prog = new double[] { 0.0 };
		this.copies = new double[] { 0.0 };
		this.copiesShort = new double[] { 0.0 };
		this.msgs = new double[] { 0.0 };
		this.msgsShort = new double[] { 0.0 };
		this.crl = 0.0;
		this.mrl = 0.0;
		this.fr = 0.0;
		this.averageCopies = 0.0;
		this.avgMsgs = 0.0;
		this.timer = new Timer();
		timer.end();
		this.routes = new ArrayList<Route>();
	}

	public Routing() {
		super("RL");
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		RoutingAlgorithm ra = n.routingAlgorithm();
		this.initEmpty();
		if (ra == null || !ra.applicable(g.nodes)) {
			return;
		}
		this.timer = new Timer();
		ra.init(g.nodes);
		Random rand = new Random(System.currentTimeMillis());
		this.routes = new ArrayList<Route>();
		int times = Config.getInt("RL_ROUTES_PER_NODE");

		for (int i = 0; i < g.nodes.length; i++) {
			for (int j = 0; j < times; j++) {
				Route r = ra.randomRoute(g.nodes, g.nodes[i], rand);
				this.routes.add(r);
			}
		}

		this.lcrl = this.lcrl(routes, g.nodes.length, times);
		Arrays.sort(this.lcrl);
		int lcrlShort = Config.getInt("RL_LCRL_SHORT_MAX_VALUES");
		this.lcrlShort = Util.avgArray(this.lcrl, lcrlShort);
		this.rld = this.rld(routes);
		this.crld = Util.cumulative(this.rld);
		if (g.nodes[0] instanceof IDNode) {
			this.prog = this.prog(routes);
		}
		if (g.nodes[0] instanceof RegistrationNode) {
			this.copies = this.copies(g.nodes);
			Arrays.sort(this.copies);
			int copiesShort = Config.getInt("RL_COPIES_SHORT_MAX_VALUES");
			this.copiesShort = Util.avgArray(this.copies, copiesShort);
			this.averageCopies = Util.avg(this.copies);
		}
		this.msgs = this.msgs(routes);
		Arrays.sort(this.msgs);
		int msgsShort = Config.getInt("RL_MSGS_SHORT_MAX_VALUES");
		this.msgsShort = Util.avgArray(this.msgs, msgsShort);
		this.crl = this.crl(routes);
		this.mrl = this.mrl(routes);
		this.fr = this.fr(routes);
		this.avgMsgs = Util.avg(this.msgs);
		this.timer.end();
	}

	private double[] lcrl(ArrayList<Route> routes, int nodes, int times) {
		double[] lcrl = new double[nodes];
		for (int i = 0; i < nodes; i++) {
			double sum = 0;
			double success = 0;
			for (int j = 0; j < times; j++) {
				int index = i * times + j;
				if (routes.get(index).success()) {
					sum += routes.get(index).path().size() - 1;
					success++;
				}
			}
			if (success == 0) {
				lcrl[i] = 0;
			} else {
				lcrl[i] = sum / success;
			}
		}
		return lcrl;
	}

	private double[] rld(ArrayList<Route> routes) {
		int[] rld = new int[(int) this.mrl(routes) + 1];
		for (int i = 0; i < routes.size(); i++) {
			if (routes.get(i).success()) {
				rld[routes.get(i).path().size() - 1]++;
			}
		}
		double[] d = Util.distribution(rld);
		double fr = this.fr(routes);
		for (int i = 0; i < d.length; i++) {
			d[i] *= 1.0 - fr;
		}
		return d;
	}

	private double[] prog(ArrayList<Route> routes) {
		if (!(routes.get(0) instanceof IDRoute)) {
			return new double[] { 0.0 };
		}
		double[] prog = new double[(int) this.mrl(routes) + 1];
		double[] count = new double[prog.length];
		for (int i = 0; i < routes.size(); i++) {
			if (routes.get(i).success()) {
				double[] p = this.getProgression((IDRoute) routes.get(i));
				for (int j = 0; j < p.length; j++) {
					prog[j] += p[j];
					count[j]++;
				}
			}
		}
		for (int i = 0; i < prog.length; i++) {
			prog[i] /= count[i];
		}
		return prog;
	}

	private double[] copies(Node[] nodes) {
		double[] copies = new double[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			copies[i] = ((RegistrationNode) nodes[i]).registeredItems();
		}
		return copies;
	}

	private double[] msgs(ArrayList<Route> routes) {
		ArrayList<Double> msgs = new ArrayList<Double>(routes.size());
		for (int i = 0; i < routes.size(); i++) {
			if (routes.get(i).success()) {
				msgs.add((double) routes.get(i).messages());
			}
		}
		return Util.toDoubleArray(msgs);
	}

	private double crl(ArrayList<Route> routes) {
		ArrayList<Integer> rl = new ArrayList<Integer>(routes.size());
		for (int i = 0; i < routes.size(); i++) {
			if (routes.get(i).success()) {
				rl.add(routes.get(i).path().size() - 1);
			}
		}
		return Util.avg(Util.toIntegerArray(rl));
	}

	private double mrl(ArrayList<Route> routes) {
		double mrl = 0;
		for (int i = 0; i < routes.size(); i++) {
			double rl = routes.get(i).path().size() - 1;
			if (routes.get(i).success() && rl > mrl) {
				mrl = rl;
			}
		}
		return mrl;
	}

	private double fr(ArrayList<Route> routes) {
		double fr = 0;
		for (int i = 0; i < routes.size(); i++) {
			if (!routes.get(i).success()) {
				fr++;
			}
		}
		return fr / (double) routes.size();
	}

	private double[] getProgression(IDRoute route) {
		Node[] path = Util.toNodeArray(route.path());
		double[] p = new double[path.length];
		IDNode dest = (IDNode) path[path.length - 1];
		double total = ((IDNode) path[0]).dist(dest);
		for (int i = 0; i < p.length; i++) {
			if (total == 0) {
				p[i] = 0;
			} else {
				p[i] = ((IDNode) path[i]).dist(route.dest()) / total;
			}
		}
		return p;
	}
}

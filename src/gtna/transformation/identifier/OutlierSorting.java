package gtna.transformation.identifier;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.routing.node.IDNode;
import gtna.routing.node.RingNode;
import gtna.routing.node.identifier.RingID;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.Random;
import java.util.Vector;

// TODO finalize and make description
public class OutlierSorting extends TransformationImpl implements
		Transformation {
	// allowed distance to best Position
	// approx. 50 * 1 / n
	private double k = 0.005;
	// variance of random distance to node it adjusts to
	// approx. 1 / n
	private double delta = 0.0001;
	// norm of best position estimation
	// 1: 1-Norm (Summe)
	// -1: by clustering (anpassung an den mit den meisten gemeinsamen nachbarn)
	private int norm;
	// maximal number of iterations (in graph size)
	// => iterations = iterations * n
	private int iterations;
	// nodes of graph to sort
	// private NodeImpl[] curNodes;
	// saving bestPositions
	// zwischenspeicherung der besten IDs
	// private double[] bestPosition;
	private RingID[] bestPosition;

	// public OutlierSorting() {
	// super("OUTLIER_SORTING", new String[] {}, new String[] {});
	// this.k = -1;
	// this.delta = -1;
	// this.norm = 1;
	// this.iterations = -1;
	// }

	// public OutlierSorting(double n) {
	// super("OUTLIER_SORTING", new String[] {}, new String[] {});
	// this.k = 50.0 / n;
	// this.delta = 1.0 / n;
	// this.norm = 1;
	// this.iterations = 1000;
	// }

	public OutlierSorting(int norm) {
		super("OUTLIER_SORTING", new String[] { "NORM" },
				new String[] { norm == 1 ? "1" : "c" });
		this.k = -1;
		this.delta = -1;
		this.norm = norm;
		this.iterations = -1;
	}

	public OutlierSorting(double k, double delta, int norm, int maxIter) {
		super("OUTLIER_SORTING", new String[] { "K", "DELTA", "NORM",
				"ITERATIONS" }, new String[] { "" + k, "" + delta, "" + norm,
				"" + maxIter });
		this.k = k;
		this.delta = delta;
		this.norm = norm;
		this.iterations = maxIter;
	}

	public boolean applicable(Graph g) {
		return g.nodes[0] instanceof RingNode;
	}

	public Graph transform(Graph g) {
		if (this.k == -1) {
			this.k = 50.0 / g.nodes.length;
			this.delta = 1.0 / g.nodes.length;
			this.iterations = 1000;
		}
		this.sort(g.nodes);
		g.computeDegrees();
		g.computeEdges();
		return g;
	}

	private void sort(NodeImpl[] nodes) {
		int count = 0;
		this.bestPosition = new RingID[nodes.length];
		Vector<IDNode> outliers = this.collectOutliers(nodes);
		IDNode curOutlier, curNeighbor;
		Random rand = new Random();
		while (count < this.iterations * nodes.length && outliers.size() > 0) {
			// get a random outlier and correct its position
			curOutlier = outliers.remove(rand.nextInt(outliers.size()));
			if (this.isOutlier(curOutlier)) {
				RingID cid = new RingID(
						bestPosition[curOutlier.index()].pos
								+ rand.nextDouble() * this.delta);
				((RingNode) curOutlier).setID(cid);
				count++;
				// collect new outlier created by change
				for (int i = 0; i < curOutlier.out().length; i++) {
					curNeighbor = (IDNode) curOutlier.out()[i];
					if (curNeighbor.dist(bestPosition[curNeighbor.index()]) <= k
							&& this.isOutlier(curNeighbor)) {
						outliers.add(curNeighbor);
					}
				}
			}
		}

	}

	/**
	 * best positon for cur in current constellation in the one norm
	 * 
	 * @param cur
	 * @return
	 */
	private double getBestPosition1(IDNode cur) {
		// in one norm: best position is either a neighbor's location
		double best = Double.MAX_VALUE;
		IDNode curNeighbor;
		double sum;
		double bestLoc = ((RingNode) cur).getID().pos;
		for (int j = 0; j < cur.out().length; j++) {
			curNeighbor = (IDNode) cur.out()[j];
			sum = 0;
			for (int m = 0; m < cur.out().length; m++) {
				sum += curNeighbor.dist(((RingNode) cur.out()[m]).getID());
			}
			if (sum < best) {
				best = sum;
				bestLoc = ((RingNode) curNeighbor).getID().pos;
			}
		}
		this.bestPosition[cur.index()] = new RingID(bestLoc);
		return bestLoc;
	}

	/**
	 * best positon for cur in current constellation in the one norm
	 * 
	 * @param cur
	 * @return
	 */
	private double getBestPositionC(IDNode cur) {
		// in one norm: best position is either a neighbor's location
		double best = -1;
		IDNode curNeighbor;
		int sum;
		double bestLoc = ((RingNode) cur).getID().pos;
		for (int j = 0; j < cur.out().length; j++) {
			curNeighbor = (IDNode) cur.out()[j];
			sum = 0;
			for (int m = 0; m < cur.out().length; m++) {
				if (curNeighbor.hasOut((NodeImpl) cur.out()[m])) {
					sum++;
				}
			}
			if (sum > best) {
				best = sum;
				bestLoc = ((RingNode) curNeighbor).getID().pos;
			}
		}
		this.bestPosition[cur.index()] = new RingID(bestLoc);
		return bestLoc;
	}

	private Vector<IDNode> collectOutliers(NodeImpl[] nodes) {
		Vector<IDNode> outliers = new Vector<IDNode>();
		IDNode cur;
		for (int i = 0; i < nodes.length; i++) {
			cur = (IDNode) nodes[i];
			if (this.isOutlier(cur)) {
				outliers.add(cur);
			}
		}
		return outliers;
	}

	private boolean isOutlier(IDNode cur) {
		double bestPos;
		switch (norm) {
		case 1:
			bestPos = this.getBestPosition1(cur);
			break;
		case -1:
			bestPos = this.getBestPositionC(cur);
			break;
		default:
			throw new IllegalArgumentException("Norm " + norm
					+ " not yet implemented");
		}
		if (cur.dist(new RingID(bestPos)) > k) {
			return true;
		}
		return false;
	}
}

package gtna.transformation.communities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.Log;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;
import gtna.transformation.communities.matrices.IMyEMatrix;
import gtna.transformation.communities.matrices.IMyQMatrix;
import gtna.transformation.communities.matrices.MyEMatrixInt;
import gtna.transformation.communities.matrices.MyEMatrixLong;
import gtna.transformation.communities.matrices.MyQEMatrixInt;
import gtna.transformation.communities.matrices.MyQEMatrixLong;
import gtna.transformation.communities.matrices.MyQMatrixInt;
import gtna.transformation.communities.matrices.MyQMatrixLong;
import gtna.util.Config;

/**
 * Encapsulates the community detection algorithm based on improving the
 * modularity in each step, described in
 * "Fast algorithm for detecting community structure in networks" by M. E. J.
 * Newman. The algorithm is implemented as a metric, so it can be used on any
 * graph loaded into the GTNA framework.
 * 
 * @author Philipp Neubrand
 * 
 */
public class CommunityDetectionDeltaQ extends TransformationImpl implements Transformation {
	// storage for the trace of the algorithm
	// stores which communities are merged in each step
	// two arrays are used as storage for performance purposes
	private int[] mergesI;
	private int[] mergesJ;


	/**
	 * Standard Constructor. Does call super with the suffix for this metric,
	 * "CDQ".
	 * 
	 */
	public CommunityDetectionDeltaQ() {
		super("COMMUNITY_DETECTION_DELTAQ", new String[] {}, new String[] {});
	}

	/**
	 * Does the work. Configuration of this method is done via the
	 * /configs/metric.cdq.properties file. For available options refer to ___
	 * 
	 */
	public int[] deltaQ(Graph g) {
		
		int nodes = g.getNodes().length;

		boolean forceSeparated = Config.getBoolean(key() + "_FORCESEPARATED");

		String internalFormat = Config.get(key() + "_INTERNALFORMAT");

		// Create the needed matrices. Note that due to generics being limited
		// to non-primitive types, using them is not possible. It would be
		// possible to box all the variables but since memory usage is a problem
		// anyway, this is not an option either.
		IMyEMatrix e = null;
		IMyQMatrix q = null;
		boolean useQE = !forceSeparated && isSymmetric(g);
		if ("long".equals(internalFormat)) {
			if (useQE) {
				e = MyQEMatrixLong.createFromGraph(g);
				q = (IMyQMatrix) e;
			} else {
				e = new MyEMatrixLong(g);
				q = new MyQMatrixLong((MyEMatrixLong) e);
			}
		} else {
			if (useQE) {
				e = MyQEMatrixInt.createFromGraph(g);
				q = (IMyQMatrix) e;
			} else {
				e = new MyEMatrixInt(g);
				q = new MyQMatrixInt((MyEMatrixInt) e);
			}
		}

		// Initialize the algorithm
		double mod = 0;
		int t = 0;
		int maxT = Config.getInt(key() + "_MAXITERATIONS");
		if (maxT == 0 || maxT > nodes - 1)
			maxT = nodes - 1;

		mergesI = new int[maxT];
		mergesJ = new int[maxT];

		// will hold the two communities that will be merged in the next step
		int[] nextMerge = new int[2];
		
		Double bestValue = - Double.MAX_VALUE;
		int bestIteration = 0;
		
		// main loop for the algorithm
		while (t <= maxT) {

			if (mod > bestValue) {
				// found a better distribution
				bestValue = mod;
				bestIteration = t;
			}


			// Find the next two communities to be merged
			q.getNextMerge(nextMerge);
			
			if(nextMerge[0] == -1){
				break;
			}

			// Merge the two communities
			e.merge(nextMerge[0], nextMerge[1]);
			// Update deltaQ based on the two communities that were merged
			q.update(nextMerge[0], nextMerge[1]);
			// Store the merge in the trace
			mergesI[t] = nextMerge[0];
			mergesJ[t] = nextMerge[1];

			// Add the change in modularity to the modularity
			mod += q.getLastDelta();
			t++;
		}


		// After determining the best possible community distribution this
		// distribution needs to be constructed now. To save memory, the
		// algorithm itself only creates a trace of which communities to merge
		// in which step. Now the communities are merged according to that trace
		// up to the best iteration. This is not included in the runtime of the
		// algorithm since this could be done while it is trying to find the
		// best possible community distribution. In this implementation it is
		// done afterwards to save memory.
		
		// Go through the trace of the algorithm and merge communities as
		// specified in the trace.
		int[] ret = new int[g.getNodes().length];
		for(int i = 0; i < g.getNodes().length; i++)
			ret[i] = i;
		
		for (int i = 0; i <= bestIteration; i++) {
			for(int j = 0; j < g.getNodes().length; j++){
				if(ret[j] == mergesJ[i])
					ret[j] = mergesI[i];		
			}
		}
		
		return ret;
	}

	/**
	 * Tests whether the adjacency matrix of a graph is symmetric. Might get
	 * moved to the Graph object itself (sooner or later). Since the graph is
	 * stored with a list of edges there is no other way but to brute force the
	 * symmetry check.
	 * 
	 * @param g
	 *            the graph to be tested
	 * @return true if the adjacency matrix is symmetric, false if not
	 */
	private boolean isSymmetric(Graph g) {
		Edge tbr = null;
		ArrayList<Edge> buffer = new ArrayList<Edge>();
		for (Edge edge : g.getEdges().getEdges()) {
			for (Edge aktTest : buffer) {
				if (aktTest.getDst() == edge.getSrc() && aktTest.getSrc() == edge.getDst()) {
					tbr = aktTest;
				}
			}
			if (tbr != null) {
				buffer.remove(tbr);
			} else {
				buffer.add(edge);
			}
			tbr = null;
		}
		
		return buffer.size() == 0;

	}




	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	@Override
	public Graph transform(Graph g) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        int[] labels = deltaQ(g);
        HashMap<Integer, Integer> labelCommunityMapping = mapLabelsToCommunities(labels);

		for (Node n : g.getNodes()) {
			map.put(n.getIndex(), labelCommunityMapping.get(labels[n.getIndex()]));
		}

		g.addProperty(g.getNextKey("COMMUNITIES"), new gtna.communities.Communities(map));
		return g;
	}
	
	 /**
     * Maps labels to communities. Labels are mapped in ascending order, communities are
     * indexed from 0 to N, where N is the number of communities.
     * @param labels array of labels
     * @return mapping of labels to communities
     */
    private HashMap<Integer, Integer> mapLabelsToCommunities(int[] labels){
        SortedSet<Integer> labelSet = new TreeSet<Integer>();
        for(int label : labels){
            labelSet.add(label);
        }
        HashMap<Integer, Integer> labelCommunityMapping = new HashMap<Integer, Integer>();
        int communityIndex = 0;
        for(int label : labelSet){
            labelCommunityMapping.put(label, communityIndex++);
        }
        return labelCommunityMapping;
    }

}

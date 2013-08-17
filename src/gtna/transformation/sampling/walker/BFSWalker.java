/* ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 *
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 *
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 *
 * GTNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GTNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * ---------------------------------------
 * RandomWalkWalker.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.sampling.walker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
<<<<<<< HEAD
<<<<<<< HEAD
import java.util.Map;
=======
>>>>>>> started BFS implementation. known bug:  java.lang.OutOfMemoryError: Java heap space @ resolveCandidates
=======
import java.util.Map;
>>>>>>> fixed bfs
import java.util.Random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.sampling.AWalker;
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.transformation.sampling.sample.NetworkSample;
=======
>>>>>>> started BFS implementation. known bug:  java.lang.OutOfMemoryError: Java heap space @ resolveCandidates
=======
import gtna.transformation.sampling.NetworkSample;
>>>>>>> fixed bfs

/**
 * @author Tim
 * 
 */
<<<<<<< HEAD
<<<<<<< HEAD
public class BFSWalker extends BFSBaseWalker {

    
=======
public class BFSWalker extends AWalker {

    List<Node> nextQ;
    private int restartcounter = 0;

>>>>>>> started BFS implementation. known bug:  java.lang.OutOfMemoryError: Java heap space @ resolveCandidates
=======
public class BFSWalker extends BFSBaseWalker {

    
>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small
    /**
     * @param walker
     */
    public BFSWalker() {
<<<<<<< HEAD
<<<<<<< HEAD
	super("BFS_WALKER");
    }

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.walker.BFSBaseWalker#chooseNodesToAddToQ(java.util.Collection)
	 */
	@Override
	protected Collection<Node> chooseNodesToAddToQ(Collection<Node> toFilter) {
		return toFilter; // all nodes are added.
	}

    
=======
	super("RANDOM_WALK_WALKER");
=======
	super("BFS_WALKER");
<<<<<<< HEAD
>>>>>>> fixed bfs
	nextQ = new LinkedList<Node>();
=======
>>>>>>> removed unnecessary fields in child classes
    }

	/* (non-Javadoc)
	 * @see gtna.transformation.sampling.walker.BFSBaseWalker#chooseNodesToAddToQ(java.util.Collection)
	 */
	@Override
	protected Collection<Node> chooseNodesToAddToQ(Collection<Node> toFilter) {
		return toFilter; // all nodes are added.
	}

<<<<<<< HEAD
>>>>>>> started BFS implementation. known bug:  java.lang.OutOfMemoryError: Java heap space @ resolveCandidates
=======
    
>>>>>>> added a BaseWalker for BFS-like Sampling algorithms as the differences between BFS, FF, RDS and SB are very small
}

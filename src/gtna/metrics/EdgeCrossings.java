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
 * EdgeCrossings.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.DIdentifierSpace;
import gtna.id.IdentifierSpace;
import gtna.id.Partition;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDPartitionSimple;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.id.ring.RingIdentifierSpace;
import gtna.id.ring.RingPartition;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Nico
 * 
 */
public class EdgeCrossings extends MetricImpl implements Metric {
	private Timer runtime;
	private double[] cd;
	private int maxCrossingNumber;
	private HashSet<String> handledEdges;
	private Distribution crossingDistribution;
	private Partition[] partitions;

	public EdgeCrossings() {
		super("EC");
	}

	public void computeData(Graph graph, Network nw, HashMap<String, Metric> m) {
		this.runtime = new Timer();

		Edge[] edges = graph.generateEdges();

		DIdentifierSpace idSpace = (DIdentifierSpace) graph.getProperty("ID_SPACE_0");
		int result = calculateCrossings(edges, idSpace, false);

		double[] finalCD = new double[maxCrossingNumber + 1];
		for (int i = 0; i < maxCrossingNumber + 1; i++) {
			finalCD[i] = cd[i] / edges.length;
		}
		this.crossingDistribution = new Distribution(finalCD);

		this.runtime.end();
	}

	public int calculateCrossings(Edge[] edges, IdentifierSpace idSpace, boolean useShortcuts) {
		int result = 0;
		cd = new double[edges.length];
		maxCrossingNumber = 0;
		this.runtime = new Timer();

		if (useShortcuts && idSpace instanceof RingIdentifierSpace) {
			result = calculateRingCrossings(edges, idSpace);
		} else {
			handledEdges = new HashSet<String>();
			partitions = idSpace.getPartitions();
			for (int outerCounter = 0; outerCounter < edges.length; outerCounter++) {
				int innerResult = 0;
				int innerStart = outerCounter + 1;
				if (!useShortcuts) {
					innerStart = 0;
				}
				for (int innerCounter = innerStart; innerCounter < edges.length; innerCounter++) {
					if (hasCrossing(edges[innerCounter], edges[outerCounter], idSpace, useShortcuts)) {
						innerResult++;
					}
				}
				cd[innerResult]++;
				maxCrossingNumber = Math.max(innerResult, maxCrossingNumber);
				result += innerResult;
			}
		}
		this.runtime.end();
//		System.out.println("Computed " + result + " crossings with " + edges.length + " edges in " + runtime.getMsec()
//				+ " msec");
		return result;
	}

	private int calculateRingCrossings(Edge[] edges, IdentifierSpace idSpace) {
		/*
		 * The following algorithm is an implementation of Six/Tollis work
		 */
		int i = 0;
		int numCross = 0;

		RingIdentifierSpace ridSpace = (RingIdentifierSpace) idSpace;
		partitions = (RingPartition[]) ridSpace.getPartitions();

		Arrays.sort(edges, new EdgeComparator((RingPartition[]) partitions));
		ArrayList<RingEdge> ringEdges = new ArrayList<RingEdge>();
		TreeSet<Double> partPositions = new TreeSet<Double>();

		for (Edge sE : edges) {
			partPositions.add(getPositionRing(sE.getSrc()));
		}

		RingEdge tempRingEdge, lastEdge;
		lastEdge = null;
		for (Edge sE : edges) {
			double srcPos = Math.min(getPositionRing(sE.getSrc()), getPositionRing(sE.getDst()));
			double dstPos = Math.max(getPositionRing(sE.getSrc()), getPositionRing(sE.getDst()));
			tempRingEdge = new RingEdge(srcPos, dstPos, sE.getSrc(), sE.getDst());
			if (!tempRingEdge.equals(lastEdge)) {
				ringEdges.add(tempRingEdge);
			}
			lastEdge = tempRingEdge;
		}

		TreeMap<Double, ArrayList<RingEdge>> startNode = new TreeMap<Double, ArrayList<RingEdge>>();
		TreeMap<Double, ArrayList<RingEdge>> targetNode = new TreeMap<Double, ArrayList<RingEdge>>();
		ArrayList<RingEdge> openEdges = new ArrayList<RingEdge>();

		Double[] posList = new Double[partitions.length];
		for (Partition rP : partitions) {
			posList[i] = ((RingPartition) rP).getStart().getPosition();
			startNode.put(posList[i], new ArrayList<RingEdge>());
			targetNode.put(posList[i], new ArrayList<RingEdge>());
			i++;
		}
		Arrays.sort(posList);

		ArrayList<RingEdge> tempList;
		for (RingEdge sE : ringEdges) {
			tempList = startNode.get(sE.src);
			tempList.add(sE);
			startNode.put(sE.src, tempList);

			tempList = targetNode.get(sE.dst);
			tempList.add(sE);
			targetNode.put(sE.dst, tempList);
		}

		for (i = 0; i < partitions.length; i++) {
			openEdges.removeAll(targetNode.get(posList[i]));
			for (RingEdge sE : targetNode.get(posList[i])) {
				for (RingEdge sOE : openEdges) {
					if (sOE.src > sE.src) {
						numCross++;
					}
				}
			}
			openEdges.addAll(startNode.get(posList[i]));
		}

		return numCross;
	}

	public int calculateCrossings(Graph g, Node n, IdentifierSpace idSpace) {
		int numCross = 0;
		Edge[] nodeEdges = n.generateAllEdges();
		Edge[] graphEdges = g.generateEdges();
		handledEdges = new HashSet<String>();
		for (Edge x : nodeEdges) {
			for (Edge y : graphEdges) {
				if (hasCrossing(x, y, idSpace, true))
					numCross++;
			}
		}
		return numCross;
	}

	public int calculateCrossings(Node n, Node m, IdentifierSpace idSpace) {
		int numCross = 0;
		Edge[] nEdges = n.generateAllEdges();
		Edge[] mEdges = m.generateAllEdges();
		handledEdges = new HashSet<String>();
		for (Edge nEdge : nEdges) {
			for (Edge mEdge : mEdges) {
				if (hasCrossing(nEdge, mEdge, idSpace, true))
					numCross++;
			}
		}
		return numCross;
	}

	private boolean hasCrossing(Edge x, Edge y, IdentifierSpace idSpace, Boolean useShortcut) {
		/*
		 * There cannot be a crossing between only one edge
		 */
		if (x.equals(y))
			return false;
		if ((x.getSrc() == y.getSrc()) || (x.getSrc() == y.getDst()) || (x.getDst() == y.getSrc())
				|| (x.getDst() == y.getDst()))
			return false;
		if (idSpace instanceof PlaneIdentifierSpaceSimple) {
			return hasCrossingPlane(x, y, useShortcut);
		} else if (idSpace instanceof RingIdentifierSpace) {
			return hasCrossingRing(x, y);
		} else if (idSpace instanceof MDIdentifierSpaceSimple) {
			int dim = ((MDIdentifierSpaceSimple) idSpace).getDimensions();
			if (dim == 2) {
				return hasCrossingMD(x, y, useShortcut);
			} else {
				throw new RuntimeException("Cannot calculate crossings in " + idSpace.getClass() + " with " + dim
						+ " dimensions");
			}
		} else {
			throw new RuntimeException("Cannot calculate crossings in " + idSpace.getClass());
		}
	}

	private PlaneEdge getPlaneEdgeFromPI(Edge x) {
		PlaneIdentifier startID = (PlaneIdentifier) ((PlanePartitionSimple) partitions[x.getSrc()])
				.getRepresentativeID();
		double startX = startID.getX();
		double startY = startID.getY();
		PlaneIdentifier endID = (PlaneIdentifier) ((PlanePartitionSimple) partitions[x.getDst()]).getRepresentativeID();
		double endX = endID.getX();
		double endY = endID.getY();
		return new PlaneEdge(startX, startY, endX, endY);
	}

	private boolean hasCrossingPlane(Edge x, Edge y, Boolean useShortcut) {
		return hasCrossing(getPlaneEdgeFromPI(x), getPlaneEdgeFromPI(y), useShortcut);
	}

	private PlaneEdge getPlaneEdgeFromMD(Edge x) {
		MDIdentifier startID = (MDIdentifier) ((MDPartitionSimple) partitions[x.getSrc()]).getRepresentativeID();
		if (startID.getCoordinates().length > 2) {
			throw new RuntimeException("Cannot calculate crossings  with " + startID.getCoordinates().length
					+ " dimensions");
		}
		double startX = startID.getCoordinate(0);
		double startY = startID.getCoordinate(1);
		MDIdentifier endID = (MDIdentifier) ((MDPartitionSimple) partitions[x.getDst()]).getRepresentativeID();
		if (endID.getCoordinates().length > 2) {
			throw new RuntimeException("Cannot calculate crossings with " + endID.getCoordinates().length
					+ " dimensions");
		}
		double endX = endID.getCoordinate(0);
		double endY = endID.getCoordinate(1);
		return new PlaneEdge(startX, startY, endX, endY);
	}

	private boolean hasCrossingMD(Edge x, Edge y, Boolean useShortcut) {
		return hasCrossing(getPlaneEdgeFromMD(x), getPlaneEdgeFromMD(y), useShortcut);
	}

	private boolean hasCrossing(PlaneEdge x, PlaneEdge y, Boolean useShortcut) {
		/*
		 * Have we already handled this edge?
		 */
		String edgeString = x + " " + y;
		if (y.startX < x.startX) {
			edgeString = y + " " + x;
		}
		if (useShortcut) {
			if (handledEdges.contains(edgeString)) {
				return false;
			}
			handledEdges.add(edgeString);
		}

		/*
		 * calculation according to
		 * http://www.ahristov.com/tutorial/geometry-games
		 * /intersection-lines.html
		 */
		double d = (x.startX - x.endX) * (y.startY - y.endY) - (x.startY - x.endY) * (y.startX - y.endX);
		if (d == 0) {
			return false;
		}

		double xi = ((y.startX - y.endX) * (x.startX * x.endY - x.startY * x.endX) - (x.startX - x.endX)
				* (y.startX * y.endY - y.startY * y.endX))
				/ d;
		double yi = ((y.startY - y.endY) * (x.startX * x.endY - x.startY * x.endX) - (x.startY - x.endY)
				* (y.startX * y.endY - y.startY * y.endX))
				/ d;
		/*
		 * xi/yi is a possible intersection point - check whether it lies on one
		 * of the edges
		 */
		if (isBetween(x.startX, x.endX, xi) && isBetween(x.startY, x.endY, yi) && isBetween(y.startX, y.endX, xi)
				&& isBetween(y.startY, y.endY, yi)) {
			return true;
		}
		return false;

	}

	private boolean isBetween(double rangeStart, double rangeEnd, double value) {
		if (rangeStart > rangeEnd) {
			return isBetween(rangeEnd, rangeStart, value);
		}
		return (rangeStart < value) && (rangeEnd > value);
	}

	private boolean hasCrossingRing(Edge x, Edge y) {
		double xStart = Math.min(getPositionRing(x.getSrc()), getPositionRing(x.getDst()));
		double xEnd = Math.max(getPositionRing(x.getSrc()), getPositionRing(x.getDst()));
		double yStart = Math.min(getPositionRing(y.getSrc()), getPositionRing(y.getDst()));
		double yEnd = Math.max(getPositionRing(y.getSrc()), getPositionRing(y.getDst()));

		String xString = xStart + "-" + xEnd;
		String yString = yStart + "-" + yEnd;
		String edgeString;
		if (xStart < yStart)
			edgeString = xString + "|" + yString;
		else
			edgeString = yString + "|" + xString;

		/*
		 * Have we already handled this edge?
		 */
		if (handledEdges.contains(edgeString)) {
			return false;
		}
		handledEdges.add(edgeString);

		if ((xStart < yStart && xEnd > yEnd) || (yStart < xStart && yEnd > xEnd) || (yStart > xEnd || xStart > yEnd)) {
			// System.out.println( "No crossing between " + edgeString );
			return false;
		}
		if ((xStart < yStart && xEnd < yEnd) || (xStart > yStart && xEnd > yEnd)) {
			// System.out.println("Got a crossing between " + edgeString);
			return true;
		}

		System.err.println("Unknown case " + edgeString);
		return false;
	}

	protected double getPositionRing(int i) {
		return ((RingPartition) partitions[i]).getStart().getPosition();
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.crossingDistribution.getDistribution(), "EC_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.crossingDistribution.getCdf(), "EC_DISTRIBUTION_CDF", folder);
		return success;
	}

	@Override
	public Value[] getValues() {
		Value ecAVG = new Value("EC_AVG", this.crossingDistribution.getAverage());
		return new Value[] { ecAVG };
	}

	private class PlaneEdge {
		double startX, startY, endX, endY;

		public PlaneEdge(double startX, double startY, double endX, double endY) {
			if (startX > endX) {
				this.startX = endX;
				this.endX = startX;
				this.startY = endY;
				this.endY = startY;
			} else {
				this.startX = startX;
				this.endX = endX;
				this.startY = startY;
				this.endY = endY;
			}
		}

		public String toString() {
			return "(" + startX + "|" + startY + ")->(" + endX + "|" + endY + ")";
		}
	}

	private class RingEdge {
		double src, dst;
		int graphSrc, graphDst;

		public RingEdge(double src, double dst, int graphSrc, int graphDst) {
			this.src = src;
			this.dst = dst;
			this.graphSrc = graphSrc;
			this.graphDst = graphDst;
		}

		public boolean equals(RingEdge r) {
			if (r == null)
				return false;
			return (r.src == this.src) && (r.dst == this.dst);
		}

		public String toString() {
			return this.src + "->" + this.dst;
		}
	}
}
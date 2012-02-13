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
 * GridPlacementModel.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.placementmodels.models;

import gtna.networks.model.placementmodels.PlacementModelImpl;
import gtna.networks.model.placementmodels.PlacementNotPossibleException;
import gtna.networks.model.placementmodels.Point;

/**
 * Places the nodes in a grid with <code>rows</code> rows and <code>cols</code>
 * columns. Will throw an exception if more nodes are to be placed than is space
 * in the grid.
 * 
 * @author Philipp Neubrand
 * 
 */
public class GridPlacementModel extends PlacementModelImpl {

	private int rows;
	private int cols;
	private double height;
	private double width;

	/**
	 * 
	 * @param width
	 *            The width of the field in which the nodes are to be
	 *            distributed.
	 * @param height
	 *            The height of the field in which the nodes are to be
	 *            distributed.
	 * @param cols
	 *            The number of columns in the grid.
	 * @param rows
	 *            The number of rows in the grid.
	 * @param inCenter
	 *            If set to <code>true</code> will place a node in the center of
	 *            the circle.
	 */
	public GridPlacementModel(double width, double height, int cols, int rows,
			boolean inCenter) {
		this.cols = cols;
		this.rows = rows;
		this.width = width;
		this.height = height;
		setInCenter(inCenter);
		setKey("GRID");
		setAdditionalConfigKeys(new String[] { "COLS", "ROWS", "WIDTH",
				"HEIGHT" });
		setAdditionalConfigValues(new String[] { Integer.toString(cols),
				Integer.toString(rows), Double.toString(width),
				Double.toString(height) });
	}

	/**
	 * Places <code>count</code> nodes in a grid with <code>rows</code> rows and
	 * <code>cols</code> columns. If <code>inCenter</code> was set when creating
	 * this PlacementModel, one node is placed right at <code>center</code>,
	 * with <code>count-1</code> being placed in the grid. The grid is filled
	 * from the bottom left, row by row. All nodes are guaranteed to be assigned
	 * coordinates between (0,0) and (maxX, maxY). If this is not possible, a
	 * <code>PlacementNotPossibleException</code> is thrown.
	 */
	@Override
	public Point[] place(int count, Point center, double maxX, double maxY) {
		if (getInCenter() && count > ((rows * cols) + 1) || !getInCenter()
				&& count > ((rows * cols)))
			throw new PlacementNotPossibleException("Can not place " + count
					+ " nodes in a Grid with only " + rows + " rows and "
					+ cols + " cols.");

		Point[] ret = new Point[count];
		double xoffset = width / cols;
		double yoffset = height / rows;
		Point p;
		for (int i = 0; i < count; i++) {
			p = new Point(center.getX() + (i % cols) * xoffset + xoffset / 2
					- width / 2, center.getY() - height / 2
					+ (Math.floor(i / cols)) * yoffset + yoffset / 2);
			// if it happens once, it will keep happening since there is no
			// random element involved in placing the node, so we throw an
			// exception without trying it again
			if (p.getX() < 0 || p.getX() > maxX || p.getY() < 0
					|| p.getY() > maxY)
				throw new PlacementNotPossibleException("Could not place node "
						+ i + " for settings: F=(" + width + ", " + height
						+ "), count=" + count + ", inCenter=" + getInCenter());
			ret[i] = p;

		}

		return ret;
	}
}

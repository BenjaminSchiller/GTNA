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
	 */
	public GridPlacementModel(double width, double height, int cols, int rows) {
		setWidth(width);
		setHeight(height);
		this.cols = cols;
		this.rows = rows;
		setKey("GRID");
		setAdditionalConfigKeys(new String[] { "COLS", "ROWS" });
		setAdditionalConfigValues(new String[] { Integer.toString(cols),
				Integer.toString(rows) });
	}

	/**
	 * Places the nodes in a grid with <code>rows</code> rows and
	 * <code>cols</code> columns. Will throw an exception if more nodes are to
	 * be placed than is space in the grid.
	 */
	@Override
	public Point[] place(int count) {
		if (count > rows * cols)
			throw new PlacementNotPossibleException("Can not place " + count
					+ " nodes in a Grid with only " + rows + " rows and "
					+ cols + " cols.");

		Point[] ret = new Point[count];
		double xoffset = getWidth() / cols;
		double yoffset = getHeight() / rows;

		for (int i = 0; i < count; i++) {
			ret[i] = new Point((i % cols) * xoffset, (Math.floor(i / cols))
					* yoffset);

		}

		return ret;
	}

}

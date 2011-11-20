package gtna.networks.model.placementmodels;

import gtna.graph.Graph;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class GridModel extends AbstractPlacementModel implements Network {
	private int cols;
	private int rows;
	private int width;
	private double x;
	private double y;
	private int height;
	
	public GridModel(int nodes, double x, double y, int width, int height, int cols, int rows, double range, double modx, double mody, boolean wraparound, ConnectionType ct, RoutingAlgorithm ra, Transformation[] t) {
		super("GRIDMODEL", nodes, range, new String[] {"centerx", "centery", "cols", "rows"}, new String[] {Double.toString(x), Double.toString(y), Integer.toString(cols), Integer.toString(rows)}, ct, ra, t);
		this.cols = cols;
		this.rows = rows;
		
		this.setNodes(cols * rows);
		if(y < height)
			y = height;
		this.y = y;
		this.width = width;
		this.height = height;
		setCoords(new PlaneIdentifierSpaceSimple(null, modx, mody, wraparound));
	}

	public Graph generate() {
		getCoords().setPartitions(Placement.placeByGridModel(x, y, width, height, cols, rows, getCoords()));

		return finish();
	}

}

package gtna.networks.model.placementmodels;

import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class GridHotspotModel extends AbstractHotspotModel implements Network {
	private double overallWidth;
	private double overallHeight;
	private int cols;
	private int rows;
	private double x;
	private double y;

	public GridHotspotModel(int spots, int nodes, double x, double y, double overallWidth, double overallHeight, int cols, int rows, int hotspotWidth, int hotspotHeight, double hotspotSigma, boolean hotspotInCenter, double range, double modx, double mody, boolean wraparound, ConnectionType ct, RoutingAlgorithm ra, Transformation[] t) {
		super("GRID", spots, nodes, hotspotWidth, hotspotHeight, hotspotSigma, hotspotInCenter, range, modx, mody, wraparound, new String[] {"centerx", "centery", "overallWidth", "overallHeight", "cols", "rows"}, new String[] {Double.toString(x), Double.toString(y), Double.toString(overallWidth), Double.toString(overallHeight), Integer.toString(cols), Integer.toString(rows)}, ct, ra, t);
		if(x < overallWidth)
			x = overallWidth;
		this.x = x;
		if(y < overallHeight)
			y = overallHeight;
		this.y = y;
		this.overallWidth = overallWidth;
		this.overallHeight = overallHeight;
		setCoords(new PlaneIdentifierSpaceSimple(null, modx, mody, wraparound));
	}

	@Override
	protected PlanePartitionSimple[] getHotspots(PlaneIdentifierSpaceSimple idspace) {
		return Placement.placeByGridModel(x, y, overallWidth, overallHeight, cols, rows, idspace);
	}

}

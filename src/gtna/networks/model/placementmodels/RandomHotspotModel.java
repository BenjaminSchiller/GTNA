package gtna.networks.model.placementmodels;

import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class RandomHotspotModel extends AbstractHotspotModel implements Network {
	private double overallWidth;
	private double overallHeight;
	private boolean overallInCenter;
	private double x;
	private double y;

	public RandomHotspotModel(int spots, int nodes, double x, double y, double overallWidth, double overallHeight, boolean overallInCenter, int hotspotWidth, int hotspotHeight, double hotspotSigma, boolean hotspotInCenter, double range, double modx, double mody, boolean wraparound, ConnectionType ct, RoutingAlgorithm ra, Transformation[] t) {
		super("RANDOM", spots, nodes, hotspotWidth, hotspotHeight, hotspotSigma, hotspotInCenter, range, modx, mody, wraparound, new String[] {"centerx", "centery", "overallWidth", "overallHeight", "overallInCenter"}, new String[] {Double.toString(x), Double.toString(y), Double.toString(overallWidth), Double.toString(overallHeight), Boolean.toString(overallInCenter)}, ct, ra, t);
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
		return Placement.placeByRandomModel(x, y, overallWidth, overallHeight, spots, overallInCenter, idspace);
	}

}

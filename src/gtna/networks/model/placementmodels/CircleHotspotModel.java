package gtna.networks.model.placementmodels;

import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class CircleHotspotModel extends AbstractHotspotModel implements Network {
	private double radius;
	private double x;
	private double y;
	private DistributionType oalpha;
	private DistributionType od;

	public CircleHotspotModel(int spots, int nodes, double x, double y, double radius, DistributionType oalpha, DistributionType od, int width, int height, double sigma, boolean inCenter, double range, double modx, double mody, boolean wraparound, ConnectionType ct, RoutingAlgorithm ra, Transformation[] t) {
		super("CIRCLE", spots, nodes, width, height, sigma, inCenter, range, modx, mody, wraparound, new String[] {"centerx", "centery", "radius", "inCenter"}, new String[] {Double.toString(x), Double.toString(y), Double.toString(sigma), Boolean.toString(inCenter)}, ct, ra, t);
		if(x < width)
			x = width;
		this.x = x;
		if(y < height)
			y = height;
		this.y = y;
		this.oalpha = oalpha;
		this.od = od;
		this.radius = radius;
		setCoords(new PlaneIdentifierSpaceSimple(null, modx, mody, wraparound));
	}

	@Override
	protected PlanePartitionSimple[] getHotspots(PlaneIdentifierSpaceSimple idspace) {
		return Placement.placeByCircleModel(x, y, radius, spots, oalpha, od, idspace);
	}

}

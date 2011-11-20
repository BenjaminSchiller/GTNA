package gtna.networks.model.placementmodels;

import gtna.graph.Graph;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class CircleModel extends AbstractPlacementModel implements Network {
	private double radius;
	private double x;
	private double y;
	private DistributionType oalpha;
	private DistributionType od;
	
	public CircleModel(int nodes, double x, double y, double radius, int count, double range, DistributionType oalpha, DistributionType od, double modx, double mody, boolean wraparound, ConnectionType ct, RoutingAlgorithm ra, Transformation[] t) {
		super("CIRCLEMODEL", nodes, range, new String[] {"centerx", "centery", "radius"}, new String[] {Double.toString(x), Double.toString(y), Double.toString(radius)}, ct, ra, t);
		this.setNodes(count);
		
		this.oalpha = oalpha;
		this.od = od;
		if(x < radius)
			x = radius;
		this.x = x;
		if(y < radius)
			y = radius;
		this.y = y;
		setCoords(new PlaneIdentifierSpaceSimple(null, modx, mody, wraparound));
	}

	public Graph generate() {
		getCoords().setPartitions(Placement.placeByCircleModel(x, y, radius, nodes(), od, oalpha, getCoords()));

		return finish();
	}

}

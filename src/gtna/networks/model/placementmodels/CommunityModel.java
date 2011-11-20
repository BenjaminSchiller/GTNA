package gtna.networks.model.placementmodels;

import gtna.graph.Graph;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class CommunityModel extends AbstractPlacementModel implements Network {
	private double sigma;
	private boolean inCenter;
	private int width;
	private double x;
	private double y;
	private int height;
	
	public CommunityModel(int nodes, double x, double y, int width, int height, double sigma, boolean inCenter, double range, double modx, double mody, boolean wraparound, ConnectionType ct, RoutingAlgorithm ra, Transformation[] t) {
		super("COMMUNITYMODEL", nodes, range, new String[] {"centerx", "centery", "sigma", "inCenter"}, new String[] {Double.toString(x), Double.toString(y), Double.toString(sigma), Boolean.toString(inCenter)}, ct, ra, t);
		this.sigma = sigma;
		if(x < width)
			x = width;
		this.x = x;
		if(y < height)
			y = height;
		this.y = y;
		this.inCenter = inCenter;
		this.width = width;
		this.height = height;
		setCoords(new PlaneIdentifierSpaceSimple(null, modx, mody, wraparound));
	}

	public Graph generate() {
		getCoords().setPartitions(Placement.placeByCommunityModel(x, y, width, height, nodes(), sigma, inCenter, getCoords()));

		return finish();
	}

}

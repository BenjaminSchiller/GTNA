package gtna.networks.model.placementmodels;

import gtna.graph.Graph;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class RandomModel extends AbstractPlacementModel implements Network {
	private boolean inCenter;
	private int width;
	private double x;
	private double y;
	private int height;
	
	public RandomModel(int nodes, double x, double y, int width, int height, double sigma, boolean inCenter, double range, double modx, double mody, boolean wraparound, ConnectionType ct, RoutingAlgorithm ra, Transformation[] t) {
		super("RANDOMMODEL", nodes, range, new String[] {"centerx", "centery", "inCenter"}, new String[] {Double.toString(x), Double.toString(y), Boolean.toString(inCenter)}, ct, ra, t);
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
		getCoords().setPartitions(Placement.placeByRandomModel(x, y, width, height, nodes(), inCenter, getCoords()));

		return finish();
	}

}

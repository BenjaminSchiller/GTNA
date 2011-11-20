package gtna.networks.model.placementmodels;


import gtna.graph.Graph;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public abstract class AbstractHotspotModel extends AbstractPlacementModel implements Network {
	private double sigma;
	private boolean inCenter;
	private int width;
	private int height;
	private int cur = 0;
	protected int spots;
	
	public AbstractHotspotModel(String prefix, int spots, int nodes, int width, int height, double sigma, boolean inCenter, double range, double modx, double mody, boolean wraparound, String[] keys, String[] vals, ConnectionType ct, RoutingAlgorithm ra, Transformation[] t) {		
		super(prefix+"HOTSPOTMODEL", nodes * spots, range, addKeys(keys), addValues(vals, spots, width, height, sigma, inCenter), ct, ra, t);
		this.sigma = sigma;
		this.inCenter = inCenter;
		this.width = width;
		this.height = height;
		this.spots = spots;
		setCoords(new PlaneIdentifierSpaceSimple(null, modx, mody, wraparound));
	}

	private static String[] addValues(String[] vals, int spots, int width, int height, double sigma, boolean inCenter) {
		String[] ret = new String[vals.length+5];
		ret[0] = String.valueOf(spots);
		ret[1] = Double.toString(width);
		ret[2] = Double.toString(height);
		ret[3] = Double.toString(sigma);
		ret[4] = Boolean.toString(inCenter);
		
		for(int i = 0; i < vals.length; i++){
			ret[i+5] = vals[i];
		}
		
		return ret;
	}

	/**
	 * @param keys
	 * @return
	 */
	private static String[] addKeys(String[] keys) {
		String[] ret = new String[keys.length+5];
		ret[0] = "spots";
		ret[1] = "width";
		ret[2] = "height";
		ret[3] = "sigma";
		ret[4] = "inCenter";
		
		for(int i = 0; i < keys.length; i++){
			ret[i+5] = keys[i];
		}
		
		return ret;
	}

	public Graph generate() {
		PlanePartitionSimple[] hotspots = getHotspots(getCoords());
		double x = 0;
		double y = 0;
		
		PlanePartitionSimple[] ret = new PlanePartitionSimple[nodes()];
		for(int i = 0; i < hotspots.length; i++){
			if(hotspots[i].getId().getX() < width)
				x = width;
			else
				x = hotspots[i].getId().getX();
			if(hotspots[i].getId().getY() < height)
				y = height;
			else
				y = hotspots[i].getId().getY();
			
			addAll(ret, Placement.placeByCommunityModel(x, y, width, height, nodes(), sigma, inCenter, getCoords()));
			
		}
		
		this.getCoords().setPartitions(ret);

		return finish();
	}

	private void addAll(PlanePartitionSimple[] ret,
			PlanePartitionSimple[] temp) {
		for(int i = 0; i < temp.length; i++){
			ret[cur  + i] = temp[i];
		}
		cur += temp.length;
	}

	/**
	 * @param idspace
	 * @return
	 */
	protected abstract PlanePartitionSimple[] getHotspots(PlaneIdentifierSpaceSimple idspace);
	

}

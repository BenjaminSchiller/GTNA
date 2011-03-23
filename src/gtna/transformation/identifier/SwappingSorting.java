package gtna.transformation.identifier;


/**
 * original FreeNet sorting
 * 
 * @author Stef
 * 
 */

public class SwappingSorting{

//public class SwappingSorting extends TransformationImpl implements
//		Transformation {
//
//	// three versions of swapping:
//	// 0 = select partner randomly
//	// 1 = select partner by random walk of length 6
//	// 2 = only neighbors
//	private int version;
//	// nr of iterations (in size of graph)
//	private int iterations;
//	// nodes of network that is sorted
//	private NodeImpl[] curNodes;
//	private Random rand;
//
//	public SwappingSorting(int version, int maxIter) {
//		super("SWAPPING", new String[] { "VERSION", "ITERATIONS" },
//				new String[] { "" + version, "" + maxIter });
//		this.version = version;
//		this.iterations = maxIter;
//		rand = new Random();
//	}
//
//	public void init(NodeImpl[] nodes) {
//		curNodes = nodes;
//
//	}
//
//	public boolean isApllicable(NodeImpl node) {
//		return node instanceof FreeNetNode;
//	}
//
//	public void sort() {
//		FreeNetNode initiator, partner;
//		CircleID placeHolder;
//		for (int i = 0; i < iterations * this.curNodes.length; i++) {
//			initiator = (FreeNetNode) curNodes[rand.nextInt(curNodes.length)];
//			switch (version) {
//			case 0:
//				partner = getPartnerRandomly();
//				break;
//			case 1:
//				partner = getPartnerByRandomWalk(initiator);
//				break;
//			case 2:
//				partner = getPartnerNeighbor(initiator);
//				break;
//			default:
//				throw new IllegalArgumentException(
//						"This version of choosing swapping partners is unknown!");
//			}
//
//			// compute switching coefficient
//			NodeImpl[] friends = initiator.out();
//			double before = 1;
//			double after = 1;
//			for (int j = 0; j < friends.length; j++) {
//				before = before
//						* initiator.dist(((FreeNetNode) friends[j]).id(),
//								initiator);
//				if (!friends[j].equals(partner)) {
//					after = after
//							* partner.dist(((FreeNetNode) friends[j]).id(),
//									partner);
//				} else {
//					after = after * initiator.dist(partner.id(), initiator);
//				}
//			}
//			friends = partner.out();
//			for (int j = 0; j < friends.length; j++) {
//				before = before
//						* partner
//								.dist(((FreeNetNode) friends[j]).id(), partner);
//				if (!friends[j].equals(partner)) {
//					after = after
//							* initiator.dist(((FreeNetNode) friends[j]).id(),
//									initiator);
//				} else {
//					after = after * initiator.dist(partner.id(), initiator);
//				}
//			}
//
//			// decide if a switch is performed
//			if (rand.nextDouble() < before / after) {
//				placeHolder = initiator.myId;
//				initiator.changeId(partner.myId);
//				partner.changeId(placeHolder);
//			}
//
//		}
//
//	}
//
//	private FreeNetNode getPartnerRandomly() {
//		return (FreeNetNode) this.curNodes[rand.nextInt(curNodes.length)];
//	}
//
//	private FreeNetNode getPartnerByRandomWalk(FreeNetNode cur) {
//		for (int i = 0; i < 6; i++) {
//			cur = (FreeNetNode) cur.out()[rand.nextInt(cur.out().length)];
//		}
//		return cur;
//	}
//
//	private FreeNetNode getPartnerNeighbor(FreeNetNode cur) {
//		return (FreeNetNode) cur.out()[rand.nextInt(cur.out().length)];
//	}
//
//	@Override
//	public int getCount() {
//		return iterations;
//	}
}

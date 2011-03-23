package gtna.routing;

import java.util.ArrayList;

import gtna.graph.Node;
import gtna.routing.node.identifier.Identifier;

public class IDRouteImpl extends RouteImpl implements IDRoute {
	private Identifier dest;

	public IDRouteImpl(ArrayList<Node> path, boolean success, int messages,
			Identifier dest) {
		super(path, success, messages);
		this.dest = dest;
	}

	public IDRouteImpl(Identifier dest) {
		super();
		this.dest = dest;
	}

	public Identifier dest() {
		return this.dest;
	}

}

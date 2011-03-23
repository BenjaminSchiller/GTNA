package gtna.graph;

public interface Node {

	public abstract int route(Node to);

	public abstract double[] routeProg(Node to);

	public abstract void init(NodeImpl[] in, NodeImpl[] out);

	public abstract Node[] in();

	public abstract Node[] out();

	public abstract int index();

	public abstract void setIndex(int index);

	public abstract void setIn(NodeImpl[] in);

	public abstract void setOut(NodeImpl[] out);

	public abstract boolean hasIn(NodeImpl n);

	public abstract boolean hasOut(NodeImpl n);

	public abstract void addIn(NodeImpl n);

	public abstract void addOut(NodeImpl n);

	public abstract void removeIn(NodeImpl n);

	public abstract void removeOut(NodeImpl n);

	public abstract String toString();

}
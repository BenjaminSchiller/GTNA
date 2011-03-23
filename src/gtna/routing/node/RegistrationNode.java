package gtna.routing.node;

import gtna.routing.node.identifier.Identifier;

/**
 * Interface for nodes that support the registration of identifiers (can be seen
 * as the storing a copy or a pointer to the original location of the associated
 * content). When an identifier X is registered at a node, contains(X) is
 * supposed to return true to indicate that this node maintains a copy of the
 * specified content or its location.
 * 
 * @author benni
 * 
 */
public interface RegistrationNode extends IDNode {
	/**
	 * Registers the given identifier at this node. A call register(X) should
	 * result in the this.contains(X) == true.
	 * 
	 * @param id
	 *            identifier to register
	 */
	public void register(Identifier id);

	/**
	 * Returns the number of identifiers/items registered at this node.
	 * 
	 * @return number of registered identifiers/items
	 */
	public int registeredItems();
}

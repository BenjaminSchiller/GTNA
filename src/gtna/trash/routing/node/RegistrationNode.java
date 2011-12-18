///*
// * ===========================================================
// * GTNA : Graph-Theoretic Network Analyzer
// * ===========================================================
// * 
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors
// * 
// * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
// * 
// * GTNA is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// * 
// * GTNA is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// * 
// * ---------------------------------------
// * RegistrationNode.java
// * ---------------------------------------
// * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
// * and Contributors 
// * 
// * Original Author: Benjamin Schiller;
// * Contributors:    -;
// * 
// * Changes since 2011-05-17
// * ---------------------------------------
//*/
//package gtna.trash.routing.node;
//
//import gtna.trash.routing.node.identifier.Identifier;
//
///**
// * Interface for nodes that support the registration of identifiers (can be seen
// * as the storing a copy or a pointer to the original location of the associated
// * content). When an identifier X is registered at a node, contains(X) is
// * supposed to return true to indicate that this node maintains a copy of the
// * specified content or its location.
// * 
// * @author benni
// * 
// */
//@Deprecated
//public interface RegistrationNode extends IDNode {
//	/**
//	 * Registers the given identifier at this node. A call register(X) should
//	 * result in the this.contains(X) == true.
//	 * 
//	 * @param id
//	 *            identifier to register
//	 */
//	public void register(Identifier id);
//
//	/**
//	 * Returns the number of identifiers/items registered at this node.
//	 * 
//	 * @return number of registered identifiers/items
//	 */
//	public int registeredItems();
//}

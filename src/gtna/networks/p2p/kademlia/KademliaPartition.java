/* ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 *
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 *
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 *
 * GTNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GTNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * ---------------------------------------
 * KademliaPartition.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.p2p.kademlia;

import java.math.BigInteger;

import gtna.id.BIPartition;
import gtna.id.Identifier;
import gtna.id.Partition;

/**
 * @author benni
 *
 */
public class KademliaPartition implements BIPartition {
	private KademliaIdentifier id;
	
	public KademliaPartition(KademliaIdentifier id){
		this.id = id;
	}

	@Override
	public BigInteger distance(Identifier<BigInteger> id) {
		return this.id.distance(id);
	}

	@Override
	public boolean equals(Partition<BigInteger> p) {
		return this.id.equals(id.getId());
	}

	@Override
	public boolean contains(Identifier<BigInteger> id) {
		return this.id.equals((KademliaIdentifier) id);
	}

	@Override
	public Identifier<BigInteger> getRepresentativeID() {
		return this.id;
	}
	
	/**
	 * @return the id
	 */
	public KademliaIdentifier getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(KademliaIdentifier id) {
		this.id = id;
	}

	public String toString(){
		String temp = this.id.getId().toString(2);
		while(temp.length() < this.id.getIdSpace().getBits()){
			temp = "0" + temp;
		}
		return "KademliaPartition: " + temp;
	}

}

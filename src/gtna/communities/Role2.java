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
 * Role2.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni; 
 * Contributors: florian;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.communities;

import gtna.util.Config;

import java.util.ArrayList;

public class Role2 {

	public static final byte COMMON = 1;

	public static final byte BRIDGE = 2;

	public static final byte STAR = 3;

	public static final byte HUB_COMMON = 4;

	public static final byte HUB_BRIDGE = 5;

	public static final byte HUB_STAR = 6;

	public static final byte C = Role2.COMMON;

	public static final byte B = Role2.BRIDGE;

	public static final byte S = Role2.STAR;

	public static final byte HC = Role2.HUB_COMMON;

	public static final byte HB = Role2.HUB_BRIDGE;

	public static final byte HS = Role2.HUB_STAR;

	private byte type;

	private int[] nodes;

	public Role2(byte type) {
		this(type, new int[] {});
	}

	public Role2(byte type, int[] nodes) {
		this.type = type;
		this.nodes = nodes;
	}

	public Role2(byte type, ArrayList<Integer> nodes) {
		this.type = type;
		this.nodes = new int[nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			this.nodes[i] = nodes.get(i);
		}
	}

	public Role2(String string) {
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		String temp1[] = string.split(sep1);
                              
                if(temp1[0].equals("C")){
                    this.type = Role2.C;
                } 
                else if(temp1[0].equals("B")){
                    this.type = Role2.B;                    
                }
                else if(temp1[0].equals("S")){
                    this.type = Role2.S;                    
                }                
                else if(temp1[0].equals("HC")){
                    this.type = Role2.HC;                    
                }
                else if(temp1[0].equals("HB")){
                    this.type = Role2.HB;                    
                }                
                else if(temp1[0].equals("HS")){
                    this.type = Role2.HS;                    
                }
                
		if (temp1.length < 2 || temp1[1].length() == 0) {
			this.nodes = new int[] {};
		} else {
			String[] temp2 = temp1[1].split(sep2);
			this.nodes = new int[temp2.length];
			for (int i = 0; i < temp2.length; i++) {
				this.nodes[i] = Integer.parseInt(temp2[i]);
			}
		}
	}

	public String toString() {
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		StringBuffer buff = new StringBuffer();
                switch(this.type){
                    case Role2.C:
                        buff.append("C");
                        break;                      
                    case Role2.B:
                        buff.append("B");
                        break;
                    case Role2.S:
                        buff.append("S");
                        break;
                    case Role2.HC:
                        buff.append("HC");
                        break;
                    case Role2.HB:
                        buff.append("HB");
                        break;
                    case Role2.HS:
                        buff.append("HS");
                        break;                        
                }
                buff.append(sep1);
                
		if (this.nodes.length == 0) {
			return buff.toString();
		}
		buff.append(this.nodes[0]);
		for (int i = 1; i < this.nodes.length; i++) {
			buff.append(sep2 + this.nodes[i]);
		}
		return buff.toString();
	}

	/**
	 * @return the type
	 */
	public byte getType() {
		return this.type;
	}

	/**
	 * @return the nodes
	 */
	public int[] getNodes() {
		return this.nodes;
	}
}

/*
 * ===========================================================
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
 * RoleFileReader.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.metrics.roles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @deprecated
 */
public class RoleFileReader {
	
	//constructor
	public RoleFileReader(){
		
	}
	
	//import role-distribution from role-metric output file
	public ArrayList<Integer>[] readRoles(String folder) throws IOException{
		
			String line;
			ArrayList<Integer>[] nodesOfRole = new ArrayList[7];
			for(int i=0;i<nodesOfRole.length;i++){
				nodesOfRole[i] = new ArrayList<Integer>();
			}
			
			FileReader FR = new FileReader(folder+"roles.txt");
			BufferedReader br = new BufferedReader(FR);
			for(int i=0;i<7;i++){
				line = br.readLine();
				if(line.length()>2){
					line = line.substring(1, line.length()-1);
					StringTokenizer ST = new StringTokenizer(line, ", ");
					while(ST.hasMoreTokens()){
						nodesOfRole[i].add(Integer.valueOf(ST.nextToken()));
					}
				}
			}
	        br.close();
	        FR.close();
	        return nodesOfRole;
		
	}
}

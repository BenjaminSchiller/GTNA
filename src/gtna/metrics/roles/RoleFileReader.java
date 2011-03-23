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

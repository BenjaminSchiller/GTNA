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
 * Placement.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.placementmodels;

import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;

import java.util.Random;

/**
 * @author Flipp
 *
 */
public class Placement {
	private static Random rnd = new Random();
	
	private static final int maxFails = 50;
	

	public static PlanePartitionSimple[] placeByCommunityModel(double x, double y, int width, int height, int count, double sigma, boolean inCenter, PlaneIdentifierSpaceSimple idspace){
		PlanePartitionSimple[] ret = new PlanePartitionSimple[count];
		
		int i = 0;
		double dx = 0;
		double dy = 0;
		int fails = 0;
		
		if(inCenter){
			ret[0] = new PlanePartitionSimple(new PlaneIdentifier(x, y, idspace));
			i++;
		}
		while(i < count){
			
			fails = 0;
			
			x = Double.MAX_VALUE;
			y = Double.MAX_VALUE;
			
			while(Math.abs(x) > width && Math.abs(y) > height && fails < maxFails){
				dx = rnd.nextGaussian() * sigma * width;
				dy = rnd.nextGaussian() * sigma * height;
				fails++;
			}
			if(fails == maxFails)
				throw new PlacementNotPossibleException("Could not place node " + i + " for settings: x='" + x + "' y='"+y+"' width='" + width + "' height='"+height+"' sigma='" + sigma + "'");
			
			ret[i] = new PlanePartitionSimple(new PlaneIdentifier(x+dx, y+dy, idspace));
			
			i++;			
		}
		
		return ret;
	}
	
	
	public static PlanePartitionSimple[] placeByRandomModel(double x, double y, double width, double height, int count, boolean inCenter, PlaneIdentifierSpaceSimple idspace){
		PlanePartitionSimple[] ret = new PlanePartitionSimple[count];
		double dx = 0;
		double dy = 0;
		int i = 0;
		
		if(inCenter){
			ret[0] = new PlanePartitionSimple(new PlaneIdentifier(x, y, idspace));
			i++;
		}
		
		while (i < count){
			x = width * (rnd.nextDouble() - 0.5);
			y = height * (rnd.nextDouble() - 0.5);
			
			ret[i] = new PlanePartitionSimple(new PlaneIdentifier(x+dx, y+dy, idspace));
		
			i++;
		}
		
		return ret;
	}
	
	public static  PlanePartitionSimple[] placeByGridModel(double x, double y, double width, double height, int col, int row, PlaneIdentifierSpaceSimple idspace){
		PlanePartitionSimple[] ret = new PlanePartitionSimple[col*row];
		double xoffset = width / col;
		double yoffset = height / row;
		
		for(int i = 0; i < col; i++){
			for(int j = 0; i < row; i++){
				ret[i] = new PlanePartitionSimple(new PlaneIdentifier(x+i * xoffset, y+j * yoffset, idspace));
			}
		}
		
		return ret;
	}
	
	public static  PlanePartitionSimple[] placeByCircleModel(double x, double y, double radius, int count, DistributionType oalpha, DistributionType od, PlaneIdentifierSpaceSimple idspace){
		PlanePartitionSimple[] ret = new PlanePartitionSimple[count];
		double gamma = (2 * Math.PI) / count;
		double alpha = 0;
		double d = 0;
		
		for(int i = 0; i < count; i++){
			switch(oalpha){
			case FIXED:
				alpha = (i + 0.5) * gamma;
				break;
			case UNIFORM:
				alpha = i * gamma + rnd.nextDouble() * gamma;
				break;
			case NORMAL:
				alpha = (i + 0.5) * gamma + rnd.nextGaussian() * (gamma / 6);
				
				break;
			}
			switch(od){
			case FIXED:
				d = radius;
				break;
			case UNIFORM:
				d = (int) Math.floor(rnd.nextDouble() * 2 * radius);
				break;
			case NORMAL:
				d = radius + (int) Math.floor(rnd.nextGaussian() * (radius / 3));
				break;
			}
			
			ret[i] = new PlanePartitionSimple(new PlaneIdentifier(x+d * Math.cos(alpha), y+d * Math.sin(alpha), idspace));
			
		}
		
		return ret;
	}
	
	

}

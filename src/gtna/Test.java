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
 * Test.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.metrics.basic.ClusteringCoefficient;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author stefanie
 *
 */
public class Test {
	
	public static void main(String[] args) {
		String folder;
		//String[] folders = {"ngram10-30_1K/" };
		String[] names = {"2gram", "3gram", "4gram",  "real"};
		String[] sig = {"30", "1083"};
		String[] size = {"1K", "5K"};
		String[] langs = {"deu", "eng", "est", "fra", "fas", "ind", "lit"};
		for (int i = 0; i < 2; i++){
			for (int h = 0; h < 2; h++){
		try {
			folder = size[i] + "-" + sig[h];
			BufferedWriter bw = new BufferedWriter(new FileWriter("trans"+folder+".txt"));
		ClusteringCoefficient cc = new ClusteringCoefficient();
		 for (int m = 0; m < langs.length; m++){
			 
			String[] files = (new File("ngramsAll/"+size[i]+"/"+sig[h]+"/"+langs[m])).list();
				String line = langs[m];
				for (int k = 0; k < names.length; k++){
			for (int j = 0; j < files.length; j++){
				if (files[j].contains(names[k])){
					System.out.println(files[j]);
				Network net = new ReadableFile(files[j], files[j], "ngramsAll/"+size[i]+"/"+sig[h]+"/"+langs[m]+"/"+files[j], null);
				cc.computeData(net.generate(), net, null);
				cc.getSingles();
				line = line + " & ";
				line = line + (double)(Math.round(cc.getSingles()[1].value*10000)/(double)10000);				
				}
			}
				
			}
				bw.write(line+"\\\\");
				bw.newLine();
				System.out.println(folder);	
		 }
		bw.flush();
		bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
		}
	}

}

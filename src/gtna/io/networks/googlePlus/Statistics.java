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
 * Statistics.java
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
package gtna.io.networks.googlePlus;

import gtna.util.Timer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author benni
 * 
 */
public class Statistics {
	public static double[] users(String graphFolder, String suffix,
			String folder, String name) throws IOException {
		String f = folder + name + "Users.txt";
		if (!(new File(f)).exists()) {
			Timer timer = new Timer("computing users");
			File[] files = Statistics.getFiles(graphFolder, suffix);
			double[] number = new double[files.length];
			for (int i = 0; i < files.length; i++) {
				IdList list = IdList.read(files[i].getAbsolutePath());
				number[i] = list.getList().size();
			}
			timer.end();
			Statistics.write(number, f);
			return number;
		} else {
			return Statistics.read(f);
		}
	}

	public static double[][] reUsers(String graphFolder, int maxOffset,
			String suffix, String folder, String name, boolean fraction)
			throws IOException {
		double[][] reUsers = new double[maxOffset][];
		for (int j = 0; j < reUsers.length; j++) {
			String fn = folder + "re" + name + "Users-" + j + ".txt";
			if (!(new File(fn)).exists()) {
				Timer timer = new Timer("computing reUsers-" + maxOffset + "-"
						+ fraction);
				File[] files = Statistics.getFiles(graphFolder, suffix);
				double[] number = new double[files.length - maxOffset];
				for (int i = 0; i < number.length; i++) {
					IdList l1 = IdList.read(files[i].getAbsolutePath());
					IdList l2 = IdList.read(files[i + maxOffset]
							.getAbsolutePath());
					IdList l3 = IdList.intersect(l1, l2, l2.getCid());
					double v = (double) l3.getList().size();
					if (fraction) {
						v /= (double) Math.min(l1.getList().size(), l2
								.getList().size());
					}
					number[i] = v;
				}
				timer.end();
				reUsers[j] = number;
				Statistics.write(reUsers[j], fn);
			} else {
				reUsers[j] = Statistics.read(fn);
			}
		}
		return reUsers;
	}

	private static File[] getFiles(String graphFolder, String suffix) {
		File folder = new File(graphFolder);
		File[] files = folder.listFiles(new FileNameFilter("", suffix));
		Arrays.sort(files, new FileIndexComparator("-", 0));
		return files;
	}

	public static void write(double[] values, String filename)
			throws IOException {
		(new File(filename)).getParentFile().mkdirs();
		System.out.println("=> " + filename);
		BufferedWriter fw = new BufferedWriter(new FileWriter(filename));
		for (double v : values) {
			fw.write(v + "\n");
		}
		fw.close();
	}

	public static double[] read(String filename) throws NumberFormatException,
			IOException {
		System.out.println("<= " + filename);
		BufferedReader br = new BufferedReader(new FileReader(filename));
		ArrayList<Double> list = new ArrayList<Double>();
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.length() == 0) {
				continue;
			}
			list.add(Double.parseDouble(line));
		}
		br.close();
		double[] array = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}
}

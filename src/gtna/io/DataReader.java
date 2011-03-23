package gtna.io;

import gtna.util.Config;
import gtna.util.Util;

import java.util.ArrayList;

public class DataReader {
	public static double[][] readDouble2D(String filename) {
		ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
		Filereader fr = new Filereader(filename);
		String line;
		int index = -1;
		while ((line = fr.readLine()) != null) {
			String[] elements = line.trim().split(DataWriter.DELIMITER);
			list.add(new ArrayList<Double>(elements.length));
			index++;
			for (int j = 0; j < elements.length; j++) {
				list.get(index).add(Double.valueOf(elements[j]));
			}
		}
		fr.close();
		return toDouble2D(list);
	}
	
	public static double[] readDouble(String data, String folder){
		String filename = folder + Config.get(data + "_DATA_FILENAME")
		+ Config.get("DATA_EXTENSION");
		return readDouble(filename);
	}

	public static double[] readDouble(String filename) {
		return Util.arrayFromIndex(readDouble2D(filename), 1);
	}

	private static double[][] toDouble2D(ArrayList<ArrayList<Double>> list) {
		double[][] array = new double[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			array[i] = new double[list.get(i).size()];
			for (int j = 0; j < list.get(i).size(); j++) {
				array[i][j] = list.get(i).get(j);
			}
		}
		return array;
	}
}

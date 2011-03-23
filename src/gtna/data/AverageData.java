package gtna.data;

import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.util.Config;

import java.util.ArrayList;

public class AverageData {
	public static void generate(String destFolder, String[] folders) {
		String[] data = Config.getData();
		for (int i = 0; i < data.length; i++) {
			ArrayList<double[][]> values = new ArrayList<double[][]>();
			for (int j = 0; j < folders.length; j++) {
				String filename = folders[j]
						+ Config.get(data[i] + "_DATA_FILENAME")
						+ Config.get("DATA_EXTENSION");
				double[][] currentValues = DataReader.readDouble2D(filename);
				for (int k = 0; k < currentValues.length; k++) {
					double x = currentValues[k][0];
					double value = currentValues[k][1];
					if (k >= values.size()) {
						values.add(init(folders.length));
					}
					values.get(k)[j][0] = x;
					values.get(k)[j][1] = value;
				}
			}
			double[][] avg = new double[values.size()][2];
			for (int j = 0; j < values.size(); j++) {
				double[][] currentValues = values.get(j);
				int counter = 0;
				double sum = 0;
				double x = 0;
				for (int k = 0; k < currentValues.length; k++) {
					if (!Double.isNaN(currentValues[k][1])) {
						x += currentValues[k][0];
						sum += currentValues[k][1];
						counter++;
					}
				}
				if (counter > 0) {
					avg[j][0] = (double) x / (double) counter;
					avg[j][1] = (double) sum / (double) counter;
				} else {
					avg[j][0] = Double.NaN;
					avg[j][1] = Double.NaN;
				}
			}
			DataWriter.writeWithoutIndex(data[i], destFolder, avg);
		}
	}

	private static double[][] init(int length) {
		double[][] array = new double[length][2];
		for (int i = 0; i < length; i++) {
			array[i][0] = Double.NaN;
			array[i][1] = Double.NaN;
		}
		return array;
	}
}

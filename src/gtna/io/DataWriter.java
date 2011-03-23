package gtna.io;

import gtna.util.Config;

public class DataWriter {
	public static final String DELIMITER = "	";

	public static boolean write(String data, String folder, double[] values) {
		String filename = folder + Config.get(data + "_DATA_FILENAME")
				+ Config.get("DATA_EXTENSION");
		Filewriter fw = new Filewriter(filename);
		for (int i = 0; i < values.length; i++) {
			String line = i + DELIMITER + values[i];
			fw.writeln(line);
		}
		return fw.close();
	}

	public static boolean write(String data, String folder, double[][] values) {
		String filename = folder + Config.get(data + "_DATA_FILENAME")
				+ Config.get("DATA_EXTENSION");
		Filewriter fw = new Filewriter(filename);
		for (int i = 0; i < values.length; i++) {
			StringBuffer line = new StringBuffer(i + "");
			for (int j = 0; j < values[i].length; j++) {
				line.append(DELIMITER + values[i][j]);
			}
			fw.writeln(line.toString());
		}
		return fw.close();
	}

	public static boolean writeWithoutIndex(String data, String folder,
			double[][] values) {
		String filename = folder + Config.get(data + "_DATA_FILENAME")
				+ Config.get("DATA_EXTENSION");
		Filewriter fw = new Filewriter(filename);
		for (int i = 0; i < values.length; i++) {
			StringBuffer line = new StringBuffer();
			for (int j = 0; j < values[i].length; j++) {
				if (j > 0) {
					line.append(DELIMITER + values[i][j]);
				} else {
					line.append(values[i][j]);
				}
			}
			fw.writeln(line.toString());
		}
		return fw.close();
	}

	public static boolean write(String data, String folder, int[] values) {
		String filename = folder + Config.get(data + "_DATA_FILENAME")
				+ Config.get("DATA_EXTENSION");
		Filewriter fw = new Filewriter(filename);
		for (int i = 0; i < values.length; i++) {
			String line = i + DELIMITER + values[i];
			fw.writeln(line);
		}
		return fw.close();
	}

	public static boolean write(double[][] values, String dest) {
		Filewriter fw = new Filewriter(dest);
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				if (j == 0) {
					fw.write("" + values[i][j]);
				} else {
					fw.write(DELIMITER + values[i][j]);
				}
			}
			fw.writeln("");
		}
		return fw.close();
	}

	public static boolean write(double[] values, String dest) {
		Filewriter fw = new Filewriter(dest);
		for (int i = 0; i < values.length; i++) {
			fw.writeln(i + DELIMITER + values[i]);
		}
		return fw.close();
	}
}

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
 * Config.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-03 : adding containsKey method (BS)
 */
package gtna.util;

import gtna.metrics.Metric;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

public class Config extends PropertiesHolder {
	private static Properties properties;

	private static HashMap<String, String> overwrite;

	private static String defaultConfigFolder = "./config/";

	public static String get(String key) {
		String temp = null;
		if (overwrite != null && (temp = overwrite.get(key)) != null) {
			return temp;
		}
		if (properties == null) {
			try {
				init();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return properties.getProperty(key);
	}

	public static Properties getProperties() {
		if (Config.properties == null) {
			try {
				Config.init();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Config.properties;
	}

	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}

	public static int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	public static double getDouble(String key) {
		return Double.parseDouble(get(key));
	}

	public static float getFloat(String key) {
		return Float.parseFloat(get(key));
	}

	public static void appendToList(String key, String value) {
		String oldValue = Config.get(key);
		if (oldValue == null || oldValue.length() == 0) {
			Config.overwrite(key, value);
		} else if (!oldValue.contains(value)) {
			Config.overwrite(key,
					oldValue + Config.get("CONFIG_LIST_SEPARATOR") + value);
		}
	}

	public static void overwrite(String key, String value) {
		try {
			if (properties == null) {
				try {
					init();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			overwrite.put(key, value);
		} catch (NullPointerException e) {
			overwrite = new HashMap<String, String>();
			overwrite.put(key, value);
		}
	}

	public static void reset(String key) {
		if (overwrite != null) {
			if (overwrite.containsKey(key)) {
				overwrite.remove(key);
			}
		}
	}

	public static void resetAll() {
		overwrite = new HashMap<String, String>();
	}

	// public static void addFile(String file) throws IOException {
	// if (properties == null) {
	// // System.out.println("initializing with " + file);
	// properties = new java.util.Properties();
	// FileInputStream in = new FileInputStream(file);
	// properties.load(in);
	// } else {
	// // System.out.println("adding " + file);
	// Properties temp = new java.util.Properties();
	// FileInputStream in = new FileInputStream(file);
	// temp.load(in);
	// properties.putAll(temp);
	// }
	// }

	public static void initWithFiles(String[] file) throws IOException {
		properties = null;
		overwrite = null;
		for (int i = 0; i < file.length; i++) {
			addFile(file[i]);
		}
	}

	public static void initWithFolders(String[] folders) throws IOException {
		Vector<String> v = new Vector<String>();
		for (int i = 0; i < folders.length; i++) {
			File folder = new File(folders[i]);
			File[] list = folder.listFiles();
			for (int j = 0; j < list.length; j++) {
				if (list[j].isFile()
						&& list[j].getAbsolutePath().endsWith(".properties")) {
					v.add(list[j].getAbsolutePath());
				}
			}
		}
		initWithFiles(Util.toStringArray(v));
	}

	// public static void initWithFile(String file) throws IOException {
	// initWithFiles(new String[] { file });
	// }

	// public static void initWithFolder(String folder) throws IOException {
	// initWithFolders(new String[] { folder });
	// }

	public static void init() throws IOException {
		Vector<File> folders = new Vector<File>();
		folders.add(new File(defaultConfigFolder));

		try {
			Path pPath = Paths.get(Config.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI());
			if (pPath.getFileName().toString().endsWith(".jar")) {
				folders.add(pPath.toFile());
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		loadFromProperties(initFromFolders(folders));

		// initWithFolder(defaultConfigFolder);
		// Vector<String> v = new Vector<String>();
		// File folder = new File(defaultConfigFolder);
		// File[] list = folder.listFiles();
		// v.add(folder.getAbsolutePath());
		// for (int j = 0; j < list.length; j++) {
		// if (list[j].isDirectory() && !list[j].getName().startsWith(".")) {
		// v.add(list[j].getAbsolutePath());
		// }
		// }
		// initWithFolders(Util.toStringArray(v));
	}

	public static void loadFromProperties(Properties in) {
		if (properties == null) {
			properties = new java.util.Properties();
		}
		properties.putAll(in);
	}

	public static boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	public static String[] getData(Metric[] metrics) {
		int counter = 0;
		for (int i = 0; i < metrics.length; i++) {
			counter += metrics[i].getDataKeys().length;
		}
		String[] data = new String[counter];
		int index = 0;
		for (int i = 0; i < metrics.length; i++) {
			String[] keys = metrics[i].getDataKeys();
			for (int j = 0; j < keys.length; j++) {
				data[index++] = keys[j];
			}
		}
		return data;
	}

	public static String[][] allKeys(String from, Metric[] metrics) {
		// Metric[] metrics = Config.getMetrics();
		String[][] keys = new String[metrics.length][];
		for (int i = 0; i < metrics.length; i++) {
			keys[i] = Config.keys(metrics[i].getKey() + from);
		}
		return keys;
	}

	public static String[] keys(String from) {
		String[] keys = Config.get(from).split(
				Config.get("CONFIG_LIST_SEPARATOR"));
		for (int i = 0; i < keys.length; i++) {
			keys[i] = keys[i].trim();
		}
		if (keys.length == 1 && keys[0].length() == 0) {
			return new String[] {};
		}
		return keys;
	}
}

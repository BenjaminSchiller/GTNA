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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
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
*/
package gtna.util;

import gtna.metrics.Metric;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

public class Config {
	private static Properties properties;

	private static HashMap<String, String> override;

	private static String defaultConfigFolder = "./config/";

	public static String get(String key) {
		String temp = null;
		if (override != null && (temp = override.get(key)) != null) {
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

	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}

	public static int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	public static double getDouble(String key) {
		return Double.parseDouble(get(key));
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
			override.put(key, value);
		} catch (NullPointerException e) {
			override = new HashMap<String, String>();
			override.put(key, value);
		}
	}

	public static void reset(String key) {
		if (override != null) {
			if (override.containsKey(key)) {
				override.remove(key);
			}
		}
	}

	public static void resetAll() {
		override = new HashMap<String, String>();
	}

	public static void addFile(String file) throws IOException {
		if (properties == null) {
			// System.out.println("initializing with " + file);
			properties = new java.util.Properties();
			FileInputStream in = new FileInputStream(file);
			properties.load(in);
		} else {
			// System.out.println("adding " + file);
			Properties temp = new java.util.Properties();
			FileInputStream in = new FileInputStream(file);
			temp.load(in);
			properties.putAll(temp);
		}
	}

	public static void initWithFiles(String[] file) throws IOException {
		properties = null;
		override = null;
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

	public static void initWithFile(String file) throws IOException {
		initWithFiles(new String[] { file });
	}

	public static void initWithFolder(String folder) throws IOException {
		initWithFolders(new String[] { folder });
	}

	private static void init() throws IOException {
		// initWithFolder(defaultConfigFolder);
		Vector<String> v = new Vector<String>();
		File folder = new File(defaultConfigFolder);
		File[] list = folder.listFiles();
		v.add(folder.getAbsolutePath());
		for (int j = 0; j < list.length; j++) {
			if (list[j].isDirectory() && !list[j].getName().startsWith(".")) {
				v.add(list[j].getAbsolutePath());
			}
		}
		initWithFolders(Util.toStringArray(v));
	}

	public static boolean containsMetric(String key) {
		String[] names = Config.get("METRICS").split(
				Config.get("CONFIG_LIST_SEPARATOR"));
		for (int i = 0; i < names.length; i++) {
			if (key.equals(names[i].trim())) {
				return true;
			}
		}
		return false;
	}

	public static Metric[] getMetrics() {
		String[] names = Config.get("METRICS").split(
				Config.get("CONFIG_LIST_SEPARATOR"));
		Metric[] metrics = new Metric[names.length];
		for (int i = 0; i < names.length; i++) {
			try {
				metrics[i] = (Metric) ClassLoader.getSystemClassLoader()
						.loadClass(Config.get(names[i].trim() + "_CLASS"))
						.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return metrics;
	}

	public static boolean containsData(String key) {
		String[] data = getData();
		for (int i = 0; i < data.length; i++) {
			if (key.equals(data[i])) {
				return true;
			}
		}
		return false;
	}

	public static String[] getData() {
		Metric[] metrics = getMetrics();
		int counter = 0;
		for (int i = 0; i < metrics.length; i++) {
			counter += metrics[i].dataKeys().length;
		}
		String[] data = new String[counter];
		int index = 0;
		for (int i = 0; i < metrics.length; i++) {
			String[] keys = metrics[i].dataKeys();
			for (int j = 0; j < keys.length; j++) {
				data[index++] = keys[j];
			}
		}
		return data;
	}

	public static String[][] allKeys(String from) {
		Metric[] metrics = Config.getMetrics();
		String[][] keys = new String[metrics.length][];
		for (int i = 0; i < metrics.length; i++) {
			keys[i] = Config.keys(metrics[i].key() + from);
		}
		return keys;
	}

	public static String[] keys(String from) {
		String[] keys = Config.get(from).split(Config.get("CONFIG_LIST_SEPARATOR"));
		for (int i = 0; i < keys.length; i++) {
			keys[i] = keys[i].trim();
		}
		if (keys.length == 1 && keys[0].length() == 0) {
			return new String[] {};
		}
		return keys;
	}
}

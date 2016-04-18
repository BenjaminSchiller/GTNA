package gtna.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public abstract class PropertiesHolder {
	public static Properties initWithFile(String file) throws IOException {
		return initWithFiles(new String[] { file }, new Vector<InputStream>());
	}

	public static Properties initWithFolder(String folder) throws IOException {
		Vector<String> temp = new Vector<String>();
		temp.add(folder);
		return initWithFolders(temp, new Vector<URL>());
	}

	public static Properties initFromFolders(Vector<File> folders)
			throws IOException {
		Vector<String> v = new Vector<String>();
		Vector<URL> jarPositions = new Vector<URL>();
		for (File folder : folders) {
			if (folder.isDirectory()) {
				File[] list = folder.listFiles();
				v.add(folder.getAbsolutePath());
				for (int j = 0; j < list.length; j++) {
					if (list[j].isDirectory()
							&& !list[j].getName().startsWith(".")) {
						v.add(list[j].getAbsolutePath());
					}
				}
			} else if (folder.isFile() && folder.toString().endsWith(".jar")) {
				jarPositions.add(folder.toURI().toURL());
			}
		}
		return initWithFolders(v, jarPositions);
	}

	public static Properties initFromFolder(File folder) throws IOException {
		Vector<File> temp = new Vector<File>();
		temp.add(folder);
		return initFromFolders(temp);
	}

	public static Properties initFromFolder(String folderName)
			throws IOException {
		File folder = new File(folderName);
		return initFromFolder(folder);
	}

	public static Properties initWithFolders(Vector<String> folders,
			Vector<URL> jarPositions) throws IOException {
		Vector<String> v = new Vector<String>();
		for (String folderName : folders) {
			File folder = new File(folderName);
			File[] list = folder.listFiles();
			for (int j = 0; j < list.length; j++) {
				if (list[j].isFile()
						&& list[j].getAbsolutePath().endsWith(".properties")) {
					v.add(list[j].getAbsolutePath());
				}
			}
		}

		Vector<InputStream> jarPropertyPositions = new Vector<InputStream>();
		for (URL pos : jarPositions) {
			JarInputStream zip = new JarInputStream(pos.openStream());
			ZipEntry e;
			InputStream is;
			while ((e = zip.getNextEntry()) != null) {
				String name = e.getName();
				if (name.endsWith(".properties")) {
					is = PropertiesHolder.class.getResourceAsStream("/" + name);
					jarPropertyPositions.add(is);
				}
			}
			zip.close();
		}

		return initWithFiles(toStringArray(v), jarPropertyPositions);
	}

	public static Properties initWithFiles(String[] file,
			Vector<InputStream> jarPropertyPosition) throws IOException {
		Properties res = new Properties();
		for (InputStream is : jarPropertyPosition) {
			res.putAll(addFromStream(is));
		}
		for (int i = 0; i < file.length; i++) {
			res.putAll(addFile(file[i]));
		}
		return res;
	}

	public static Properties addFromStream(InputStream in) throws IOException {
		Properties temp = new java.util.Properties();
		temp.load(in);
		return temp;
	}

	public static Properties addFile(String file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		return addFromStream(in);
	}

	public static String[] toStringArray(Vector<String> v) {
		String[] array = new String[v.size()];
		Iterator<String> iter = v.listIterator();
		int index = 0;
		while (iter.hasNext()) {
			array[index++] = iter.next();
		}
		return array;
	}

}

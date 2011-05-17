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
 * Filewriter.java
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
package gtna.io;

import gtna.util.Config;

import java.io.FileWriter;
import java.io.IOException;

public class Filewriter {
	public static final String COMMENT = "# ";

	private String filename;

	private FileWriter fw;

	public Filewriter(String filename) {
		this.filename = filename;
		generateFolders(filename);
		try {
			this.fw = new FileWriter(this.filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean write(String data) {
		try {
			this.fw.write(data);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean writeln(String line) {
		return this.write(line + "\n");
	}
	
	public boolean writeln(double line){
		return this.writeln(line + "");
	}
	
	public boolean writeln(int line){
		return this.writeln(line + "");
	}
	
	public boolean writeln(){
		return this.writeln("");
	}

	public boolean writeComment(String comment) {
		return this.writeln(COMMENT + comment);
	}

	public boolean close() {
		try {
			this.fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void generateFolders(String dest) {
		String[] temp = dest.split(Config.get("FILESYSTEM_FOLDER_DELIMITER"));
		String folders = "";
		for (int i = 0; i < temp.length - 1; i++) {
			folders += temp[i] + Config.get("FILESYSTEM_FOLDER_DELIMITER");
		}
		(new java.io.File(folders)).mkdirs();
	}
	
	public String filename(){
		return this.filename;
	}
}

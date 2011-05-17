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
 * Filereader.java
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

import java.io.FileNotFoundException;
import java.io.IOException;

public class Filereader {
	private String filename;

	private java.io.BufferedReader br;

	public Filereader(String filename) {
		this.filename = filename;
		try {
			this.br = new java.io.BufferedReader(new java.io.FileReader(
					this.filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String readLine() {
		try {
			String line = this.br.readLine();
			if (line != null) {
				line = line.trim();
			}
			while (line != null
					&& (line.startsWith(Filewriter.COMMENT) || line.length() == 0)) {
				line = this.br.readLine();
				if (line != null) {
					line = line.trim();
				}
			}
			return line;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean close() {
		try {
			this.br.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}

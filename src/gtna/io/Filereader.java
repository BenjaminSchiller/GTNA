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

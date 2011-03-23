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

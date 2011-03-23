package gtna.util;

import gtna.io.Output;

public class Stats {
	private long startTime;

	private long endTime;

	private long totalTime;

	private long totalMemory;

	public Stats() {
		this.startTime = System.currentTimeMillis();
	}

	public void end() {
		this.endTime = System.currentTimeMillis();
		this.totalTime = this.endTime - this.startTime;
		this.totalMemory = Runtime.getRuntime().totalMemory();
		String out = Config.get("STATS");
		out = out.replace("%MSEC", this.totalTime + "");
		out = out.replace("%SEC", (this.totalTime / 1000) + "");
		out = out.replace("%MEM", (this.totalMemory / (1024 * 1024)) + "");
		Output.writeln(out);
		// if (this.totalTime > 10000) {
		// String filename = "/System/Library/Sounds/Glass.aiff";
		// InputStream in;
		// try {
		// in = new FileInputStream(filename);
		// AudioStream as = new AudioStream(in);
		// AudioPlayer.player.start(as);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
	}
}

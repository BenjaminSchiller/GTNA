package gtna.util;

import gtna.io.Output;

public class Timer {
	private long start;

	private long end = -1;

	String name;

	public Timer() {
		this.start = System.currentTimeMillis();
		this.name = null;
	}

	public Timer(String name) {
		this.start = System.currentTimeMillis();
		this.name = name;
		Output.write(name);
	}

	public void end() {
		end("");
	}

	public Timer(double duration) {
		this.start = 0;
		if (Config.get("TIMER_TYPE").equals("sec")) {
			this.end = 1000 * (long) duration;
		} else if (Config.get("TIMER_TYPE").equals("msec")) {
			this.end = (long) duration;
		} else {
			this.end = -1;
		}
	}

	public void end(String msg) {
		this.end = System.currentTimeMillis();
		if (this.name != null) {
			String out = Config.get("TIMER_END");
			out = out.replace("%MSG", msg);
			out = out.replace("%MSEC", msec() + "");
			out = out.replace("%SEC", sec() + "");
			Output.writeln(out);
		}
	}

	public long msec() {
		if (this.end == -1) {
			return System.currentTimeMillis() - this.start;
		} else {
			return this.end - this.start;
		}
	}

	public long sec() {
		return this.msec() / 1000;
	}

	public double rt() {
		if (Config.get("TIMER_TYPE").equals("sec")) {
			return (double) this.msec() / (double) 1000;
		} else if (Config.get("TIMER_TYPE").equals("msec")) {
			return this.msec();
		} else {
			return -1;
		}
	}
}

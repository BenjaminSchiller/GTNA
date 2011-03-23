package gtna.io;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log {
	public static final String loggerName = "gtna";

	public static final byte L_TRACE = 1;

	public static final byte L_DEBUG = 2;

	public static final byte L_INFO = 3;

	public static final byte L_WARN = 4;

	public static final byte L_ERROR = 5;

	public static final byte L_FATAL = 6;

	public static final byte L_OFF = 7;

	public static final byte L_ALL = 8;

	private static Logger logger = Logger.getLogger(loggerName);

	public static void log(byte level, String msg) {
		switch (level) {
		case L_TRACE:
			trace(msg);
			break;
		case L_DEBUG:
			debug(msg);
			break;
		case L_INFO:
			info(msg);
			break;
		case L_WARN:
			warn(msg);
			break;
		case L_ERROR:
			error(msg);
			break;
		case L_FATAL:
			fatal(msg);
			break;
		}
	}

	public static void setLogLevel(byte level) {
		switch (level) {
		case L_TRACE:
			logger.setLevel(Level.TRACE);
			break;
		case L_DEBUG:
			logger.setLevel(Level.DEBUG);
			break;
		case L_INFO:
			logger.setLevel(Level.INFO);
			break;
		case L_WARN:
			logger.setLevel(Level.WARN);
			break;
		case L_ERROR:
			logger.setLevel(Level.ERROR);
			break;
		case L_FATAL:
			logger.setLevel(Level.FATAL);
			break;
		case L_OFF:
			logger.setLevel(Level.OFF);
			break;
		case L_ALL:
			logger.setLevel(Level.ALL);
			break;
		}
	}

	public static void trace(String msg) {
		logger.trace(msg);
	}

	public static void debug(String msg) {
		logger.debug(msg);
	}

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void warn(String msg) {
		logger.warn(msg);
	}

	public static void error(String msg) {
		logger.error(msg);
	}

	public static void fatal(String msg) {
		logger.fatal(msg);
	}
}

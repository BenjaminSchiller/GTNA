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
 * Log.java
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

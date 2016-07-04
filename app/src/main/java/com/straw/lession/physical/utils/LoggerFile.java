package com.straw.lession.physical.utils;

public class LoggerFile {


	/**
	 * 打印日志的抽象类,子类根据是否�?要打印日志自己填充功�?
	 *
	 *
	 */
	public static abstract class Log4jWrapper {

		public abstract void trace(Object message);

		public abstract void trace(Object message, Throwable t);

		public abstract void debug(Object message);

		public abstract void debug(Object message, Throwable t);

		public abstract void info(Object message);

		public abstract void info(Object message, Throwable t);

		public abstract void warn(Object message);

		public abstract void warn(Object message, Throwable t);

		public abstract void warn(Throwable t);

		public abstract void error(Object message);

		public abstract void error(Object message, Throwable t);

		public abstract void error(Throwable t);

		public abstract void fatal(Object message);

		public abstract void fatal(Object message, Throwable t);

		public abstract void fatal(Throwable t);
	}

	public static Log4jWrapper getLog4j(String str) {
        return new LogNotToFile(str);
	}

}

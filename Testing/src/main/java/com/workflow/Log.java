package com.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

	private static final Logger LOG = LoggerFactory.getLogger("workflow");

	public static Logger getLogger() {
		return LOG;
	}
}

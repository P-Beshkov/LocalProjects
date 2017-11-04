package com.workflow;

import java.util.HashMap;
import java.util.Map;

public class ProcessorContext {
	public Map<String, Object> params;

	public ProcessorContext() {
		params = new HashMap<>();
	}
}

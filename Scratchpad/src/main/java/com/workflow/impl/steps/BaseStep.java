package com.workflow.impl.steps;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.workflow.Context;
import com.workflow.Step;

public abstract class BaseStep implements Step {
	protected static final Logger LOG = LoggerFactory.getLogger("Scratchpad");
	protected boolean abortOperation = false;
	protected List<Throwable> errors;
	protected int order;
	protected String name;

	@Override
	public void execute(Context context) {
		LOG.info("Executing Step[{}]", name);
	}

	@Override
	public boolean abortOperation() {
		return abortOperation;
	}

	@Override
	public List<String> getDependencies() {
		return null;
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public boolean isAsync() {
		return false;
	}
}

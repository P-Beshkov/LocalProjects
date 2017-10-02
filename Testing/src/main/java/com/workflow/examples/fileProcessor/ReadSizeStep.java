package com.workflow.examples.fileProcessor;

import com.workflow.BaseStep;

public class ReadSizeStep extends BaseStep {

	private static final String NAME = "ReadSize";

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public boolean isAsync() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		return NAME;
	}

}

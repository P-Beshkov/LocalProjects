package com.workflow.impl.steps;

import java.util.Arrays;
import java.util.List;

public class CopyImageStep extends BaseStep {

	public static String NAME = "CopyImage";

	@Override
	public List<String> getDependencies() {
		return Arrays.asList(ReadExtensionStep.NAME);
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	@Override
	public String getName() {
		return NAME;
	}

}

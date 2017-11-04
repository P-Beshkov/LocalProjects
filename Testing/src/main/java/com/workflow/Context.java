package com.workflow;

import java.util.HashMap;
import java.util.Map;

import com.workflow.entities.Execution;
import com.workflow.entities.StepEntity;

public class Context {
	public Map<String, Object> operationParams;
	public Execution execution;
	public Map<String, StepEntity> stepsInfo;
	public ProcessorContext executionContext;

	public Context() {
		operationParams = new HashMap<>();
	}

	public StepEntity getStepInfo(String stepName) {
		return stepsInfo.get(stepName);
	}

	public Object getProcessorParam(String paramName) {
		return executionContext.params.get(paramName);
	}
}

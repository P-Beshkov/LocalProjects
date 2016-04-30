package com.workflow;

import java.util.Map;

import com.workflow.entities.Execution;
import com.workflow.entities.StepEntity;

public class Context {
	public Map<String, Object> inputParams;
	public Execution execution;
	public Map<String, StepEntity> stepsInfo;

	public StepEntity getStepInfo(String stepName) {
		return stepsInfo.get(stepName);
	}
}

package com.workflow.entities;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import com.workflow.ExecutionStatus;

@Entity
public class Execution {

	@Id
	private ObjectId id;

	private String operationName;

	private ExecutionStatus status;

	@Property(concreteClass = HashMap.class)
	private Map<String, Object> inputParameters;

	private Date timeStarted;

	private long duration;

	private List<StepEntity> steps;

	private Exception error;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(MessageFormat.format("Operation={0}, status={1}, inputParams={2}, startDate={3}, "
				+ "duration={4}, error={5}", operationName, status, inputParameters, timeStarted, duration, error))
				.append(System.lineSeparator());
		for (StepEntity stepInfo : steps) {
			if (stepInfo == null) {
				builder.append("Step null.").append(System.lineSeparator());
			} else {
				builder.append(stepInfo.toString()).append(System.lineSeparator());
			}
		}
		builder.delete(builder.length() - 2, builder.length());
		return builder.toString();
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Map<String, Object> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(Map<String, Object> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public Date getTimeStarted() {
		return timeStarted;
	}

	public void setTimeStarted(Date timeStarted) {
		this.timeStarted = timeStarted;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public List<StepEntity> getSteps() {
		return steps;
	}

	public void setSteps(List<StepEntity> steps) {
		this.steps = steps;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception e) {
		this.error = e;
	}

	public ExecutionStatus getStatus() {
		return status;
	}

	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}
}

package com.workflow.entities;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import org.mongodb.morphia.annotations.Embedded;

import com.workflow.StepStatus;

@Embedded
public class StepEntity {

	private String name;

	private StepStatus status;

	private Date dateStarted;

	private long duration;

	private Map<String, Object> result;

	private long dependecyDuration;

	private String failedDepenedencyName;

	@Override
	public String toString() {
		return MessageFormat.format("Step[name={0}, status={1}, dateStarted={2}, duration={3}, depDuration={4}, "
				+ "failedDependencyName={5}]", name, status, dateStarted, duration, dependecyDuration,
				failedDepenedencyName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StepStatus getSatus() {
		return status;
	}

	public void setStatus(StepStatus satus) {
		this.status = satus;
	}

	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public long getDependecyDuration() {
		return dependecyDuration;
	}

	public void setDependecyDuration(long dependecyDuration) {
		this.dependecyDuration = dependecyDuration;
	}

	public String getFailedDependency() {
		return failedDepenedencyName;
	}

	public void setFailedDependency(String dependencyName) {
		this.failedDepenedencyName = dependencyName;
		status = StepStatus.DEPENDENCY_FAILED;
	}

}

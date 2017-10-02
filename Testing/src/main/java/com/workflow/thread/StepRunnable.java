package com.workflow.thread;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.workflow.Context;
import com.workflow.Step;
import com.workflow.StepStatus;
import com.workflow.entities.StepEntity;

class StepRunnable implements Runnable {

	private Step step;
	private Map<String, Future<?>> executingDependencies;
	private List<String> stepDependencies;
	private Context context;
	private StepEntity stepInfo;

	public StepRunnable(Step step, List<String> stepDependencies,
			Map<String, Future<?>> executingDependencies, Context context) {
		this.step = step;
		this.stepDependencies = stepDependencies;
		this.executingDependencies = executingDependencies;
		this.context = context;
		stepInfo = context.getStepInfo(step.getName());
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		stepInfo.setDateStarted(new Date());
		stepInfo.setName(step.getName());
		stepInfo.setStatus(StepStatus.RUNNING);
		if (stepDependencies != null && !stepDependencies.isEmpty()) {
			long dependecyWaitStart = System.currentTimeMillis();
			for (String dependencyName : stepDependencies) {
				Future<?> depFuture = executingDependencies.get(dependencyName);
				if (depFuture == null) {
					continue;
				}
				BaseProcessor.LOG.info("Operation[{}], Step[{}] waiting dependency[{}] to finish.",
						step.getName(), dependencyName);
				try {
					depFuture.get();
					BaseProcessor.LOG.info("Operation[{}], Step[{}] dependency finished.", step.getName());
				} catch (InterruptedException | ExecutionException e) {
					stepInfo.setFailedDependency(dependencyName);
					break;
				}
			}
			stepInfo.setDependecyDuration(System.currentTimeMillis() - dependecyWaitStart);
		}
		if (!stepInfo.getSatus().equals(StepStatus.DEPENDENCY_FAILED)) {
			BaseProcessor.LOG.info("Executing Step[{}]", step.getName());
			stepInfo.setStatus(StepStatus.RUNNING);
			try {
				step.execute(context);
				stepInfo.setStatus(StepStatus.FINISHED);
			} catch (Exception e) {
				stepInfo.setStatus(StepStatus.FAILED);
			}
			BaseProcessor.LOG.info("Step[{}] finished", step.getName());
			stepInfo.setDuration(System.currentTimeMillis() - startTime);
		}
	}
}
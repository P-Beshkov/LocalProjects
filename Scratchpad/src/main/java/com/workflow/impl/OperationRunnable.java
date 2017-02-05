package com.workflow.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.logwrapper.LogWrapper;
import com.workflow.Context;
import com.workflow.ExecutionStatus;
import com.workflow.Step;
import com.workflow.StepStatus;
import com.workflow.entities.Execution;
import com.workflow.entities.StepEntity;

class OperationRunnable implements Callable<Execution> {

	static final Logger LOG = LoggerFactory.getLogger("Scratchpad");
	private static ExecutorService stepsExecutor = Executors.newCachedThreadPool();
	private String operationName;
	private Context context;
	private List<Supplier<Step>> stepsSuppliers;
	private Execution execution;

	public OperationRunnable(List<Supplier<Step>> steps, Context context) {
		this.operationName = context.execution.getOperationName();
		this.context = context;
		this.stepsSuppliers = steps;
		execution = context.execution;
		execution.setInputParameters(context.inputParams);
		execution.setSteps(new ArrayList<>());
	}

	@Override
	public Execution call() throws Exception {
		long startTime = System.currentTimeMillis();
		execution.setStatus(ExecutionStatus.RUNNING);
		execution.setTimeStarted(new Date());
		LOG.info("Starting operation[{}], with input params: {}", operationName, context.inputParams);
		if (stepsSuppliers == null) {
			LOG.error("Operation[{}] doesn't have any steps, aborting.", operationName);
		}
		Map<String, Future<?>> executingDependencies = new HashMap<>();
		Map<String, List<String>> stepsDependencies = new HashMap<>();
		List<Step> steps = new ArrayList<>();
		if (validateDependencies(stepsDependencies, steps)) {
			for (Step step : steps) {
				try {
					BaseProcessor.LOG.info("Operation[{}], Step[{}] start", operationName, step.getName());
					StepEntity stepInfo = new StepEntity();
					execution.getSteps().add(stepInfo);
					context.stepsInfo.put(step.getName(), stepInfo);

					Future<?> future = stepsExecutor.submit(new StepRunnable(step,
							stepsDependencies.get(step.getName()), executingDependencies, context));
					if (step.isAsync()) {
						// Save the step, as it might be dependency.
						executingDependencies.put(step.getName(), future);
					} else {
						// Wait the step to finish
						future.get();
						if (stepInfo.getSatus().equals(StepStatus.FAILED)) {
							break;
						}
					}
				} catch (InterruptedException | ExecutionException e) {
					// Unexpected fail
					execution.setError(e);
					execution.setStatus(ExecutionStatus.UNEXPECTED_FAIL);
					LOG.error("", e);
					break;
				}
			}
			for (Future<?> step : executingDependencies.values()) {
				// Waiting async steps to finish
				try {
					if (execution.getStatus().equals(ExecutionStatus.UNEXPECTED_FAIL)) {
						step.cancel(true);
					} else {
						step.get();
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO add addError()
					execution.setError(e);
					execution.setStatus(ExecutionStatus.UNEXPECTED_FAIL);
					LOG.error("", e);
				}
			}
		}
		BaseProcessor.LOG.info("Operation[{}] finished", operationName);
		execution.setDuration(System.currentTimeMillis() - startTime);
		LogWrapper.saveExecution(execution);
		return execution;
	}

	private boolean validateDependencies(Map<String, List<String>> stepsDependencies, List<Step> steps) {
		for (int j = 0; j < stepsSuppliers.size(); j++) {
			// Get step instance
			Step step = stepsSuppliers.get(j).get();
			steps.add(step);
			List<String> dependencies = step.getDependencies();
			if (dependencies.isEmpty()) {
				continue;
			}
			Set<String> dependenciesNotFound = new HashSet<>(dependencies);
			for (int i = 0; i < j; i++) {
				dependenciesNotFound.remove(steps.get(i).getName());
			}
			if (dependenciesNotFound.size() != 0) {
				// Dependency not available in previous tasks
				LOG.error("Error in dependency configuration, dependecies[{}] not found in previously.",
						dependenciesNotFound);
				execution.setStatus(ExecutionStatus.CONFIGURATION_ERROR);
				return false;
			}
			stepsDependencies.put(step.getName(), dependencies);
		}
		return true;
	}

}
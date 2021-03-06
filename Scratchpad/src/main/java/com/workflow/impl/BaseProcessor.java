package com.workflow.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.workflow.Context;
import com.workflow.Operations;
import com.workflow.Processor;
import com.workflow.Step;
import com.workflow.entities.Execution;

public class BaseProcessor implements Processor {
	static final Logger LOG = LoggerFactory.getLogger("Scratchpad");
	Map<String, List<Supplier<Step>>> operationsSteps;
	private ExecutorService operationsExecutor = Executors.newCachedThreadPool();
<<<<<<< HEAD
=======
	private Future<?> operationFuture;
	private Context executionContext = new Context();
>>>>>>> 745a03311c82745299c713790eb21ccbb80b36d9

	public BaseProcessor() {
		operationsSteps = new HashMap<>();
	}

	@Override
	public Operations getOperations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Execution> process(String operationName, Map<String, Object> inputParams) {
		Context context = new Context();
		context.inputParams = inputParams;
		context.stepsInfo = new HashMap<>();
<<<<<<< HEAD
		context.execution = new Execution(operationName);
		return operationsExecutor.submit(new OperationRunnable(operationsSteps.get(operationName), context));
=======
		context.execution = new Execution();
		context.executionContext = executionContext;
		return operationsExecutor.submit(new OperationRunnable(operationName, operationsSteps.get(operationName),
				context));
>>>>>>> 745a03311c82745299c713790eb21ccbb80b36d9
	}

	@Override
	public void await() throws InterruptedException {
		operationsExecutor.awaitTermination(1, TimeUnit.MINUTES);
	}

	@Override
	public void addOperation(String operationName, List<Supplier<Step>> steps) {
		operationsSteps.put(operationName, steps);
	}

	public Context getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(Context executionContext) {
		this.executionContext = executionContext;
	}

}

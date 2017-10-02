package com.workflow.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import com.workflow.Context;
import com.workflow.Log;
import com.workflow.Processor;
import com.workflow.ProcessorContext;
import com.workflow.Step;
import com.workflow.entities.Execution;

public class BaseProcessor implements Processor {
	public static final Logger LOG = Log.getLogger();
	Map<String, List<Supplier<Step>>> operationsSteps;
	private ExecutorService operationsExecutor = Executors.newCachedThreadPool();
	private ProcessorContext processorContext = new ProcessorContext();
	private LinkedBlockingQueue<Pair<String, Map<String, Object>>> tasksSupplier;
	private boolean isRunning;

	public BaseProcessor(LinkedBlockingQueue<Pair<String, Map<String, Object>>> tasksSupplier) {

		operationsSteps = new HashMap<>();
		this.tasksSupplier = tasksSupplier;
	}

	@Override
	public Set<String> getOperations() {
		return operationsSteps.keySet();
	}

	@Override
	public void await() throws InterruptedException {
		operationsExecutor.awaitTermination(1, TimeUnit.MINUTES);
	}

	@Override
	public void addOperation(String operationName, List<Supplier<Step>> steps) {
		operationsSteps.put(operationName, steps);
	}

	public void setProcessorParam(String string, Object isStarted) {
		processorContext.params.put(string, isStarted);
	}

	@Override
	public void run() {
		isRunning = true;
		try {
			while (isRunning) {
				Pair<String, Map<String, Object>> task = tasksSupplier.take();
				Context context = new Context();
				context.operationParams = task.getValue();
				context.stepsInfo = new HashMap<>();
				context.execution = new Execution();
				context.executionContext = processorContext;
				operationsExecutor
						.submit(new OperationRunnable(task.getKey(), operationsSteps.get(task.getKey()), context));
			}
			operationsExecutor.awaitTermination(1, TimeUnit.MINUTES);
			synchronized (this) {
				notifyAll();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		isRunning = false;
	}

}

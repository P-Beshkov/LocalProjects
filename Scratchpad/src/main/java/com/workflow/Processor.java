package com.workflow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import com.workflow.entities.Execution;

public interface Processor {

	Operations getOperations();

	void await() throws InterruptedException;

	Future<Execution> process(String operationName, Map<String, Object> inputParams);

	void addOperation(String operationName, List<Supplier<Step>> steps);
}

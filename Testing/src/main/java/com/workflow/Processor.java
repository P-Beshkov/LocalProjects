package com.workflow;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public interface Processor extends Runnable {

	Set<String> getOperations();

	void await() throws InterruptedException;

	void addOperation(String operationName, List<Supplier<Step>> steps);

	void stop();
}

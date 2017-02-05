package com.workflow.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import org.junit.AfterClass;
import org.junit.Test;

import com.logwrapper.LogWrapper;
import com.workflow.Processor;
import com.workflow.Step;
import com.workflow.entities.Execution;
import com.workflow.impl.steps.CopyImageStep;
import com.workflow.impl.steps.ReadExtensionStep;
import com.workflow.impl.steps.ReadSizeStep;
import com.workflow.impl.steps.ValidateStep;

public class ImageProcessorTest {
	public static final String exportImage = "exportImage";

	@AfterClass
	public static void tearDown() {
		LogWrapper.close();
	}

	@Test
	public void exportImage() throws InterruptedException, ExecutionException {
		Processor processor = new BaseProcessor();
		initProcessor(processor);

		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("path", "D:\\Other\\Pictures\\Yanet.jpg");
		Future<Execution> future = processor.process(exportImage, inputParams);
		Execution execution = future.get();
		System.out.println(execution);
	}

	// @Test
	public void printExecutions() {
		LogWrapper.printExecutions();
	}

	private void initProcessor(Processor processor) {
		List<Supplier<Step>> exportOperations = new ArrayList<>();
		exportOperations.add(ReadExtensionStep::new);
		exportOperations.add(ReadSizeStep::new);
		exportOperations.add(CopyImageStep::new);
		exportOperations.add(ValidateStep::new);
		processor.addOperation(exportImage, exportOperations);
	}

}

package com.workflow.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.AfterClass;
import org.junit.Test;

import com.workflow.Processor;
import com.workflow.Step;
import com.workflow.examples.fileProcessor.CopyImageStep;
import com.workflow.examples.fileProcessor.ReadExtensionStep;
import com.workflow.examples.fileProcessor.ReadSizeStep;
import com.workflow.examples.fileProcessor.ValidateStep;
import com.workflow.thread.BaseProcessor;

import logwrapper.LogWrapper;

public class FileProcessorTest {
	public static final String exportImage = "exportImage";
	private static Processor imageProcessor;

	@AfterClass
	public static void tearDown() throws InterruptedException {
		LogWrapper.close();
		imageProcessor.stop();
		synchronized (imageProcessor) {
			imageProcessor.wait();
		}
	}

	@Test
	public void exportImage() throws InterruptedException, ExecutionException {
		LinkedBlockingQueue<Pair<String, Map<String, Object>>> tasksProvider = new LinkedBlockingQueue<>();
		imageProcessor = new BaseProcessor(tasksProvider);
		initProcessor(imageProcessor);
		Thread processor = new Thread(imageProcessor);
		processor.start();
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("path", "testimage.jpeg");
		tasksProvider.add(new ImmutablePair<String, Map<String, Object>>(exportImage, inputParams));
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

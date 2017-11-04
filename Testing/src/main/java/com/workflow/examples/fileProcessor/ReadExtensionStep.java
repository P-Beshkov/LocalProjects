package com.workflow.examples.fileProcessor;

import java.util.HashMap;
import java.util.Map;

import com.workflow.BaseStep;
import com.workflow.Context;

public class ReadExtensionStep extends BaseStep {

	public static final String NAME = "ReadExtension";

	public ReadExtensionStep() {
	}

	@Override
	public void execute(Context context) {
		String path = (String) context.operationParams.get("path");
		if (path == null) {
			abortOperation = true;
			return;
		}
		if (path.length() < 10) {
			errors.add(new IllegalArgumentException("Path lenght is short."));
		}
		String extension = path.substring(path.lastIndexOf("."));
		Map<String, Object> result = new HashMap<>();
		result.put("extension", extension);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	@Override
	public String getName() {
		return NAME;
	}

}

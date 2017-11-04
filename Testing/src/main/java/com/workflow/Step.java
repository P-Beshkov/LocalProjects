package com.workflow;

import java.util.List;

public interface Step {

	void execute(Context context) throws Exception;

	int getOrder();

	String getName();

	boolean abortOperation();

	boolean isAsync();

	List<String> getDependencies();
}

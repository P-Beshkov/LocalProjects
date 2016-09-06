package main;

import behavior.observer.TestObserver;
import structural.adapter.site.RunAdapterExample;
import structural.bridge.TestBridgePattern;
import structural.facade.TestFacade;

public class Starter {

	public static void main(String[] args) {
		RunAdapterExample.run();
		// TestFacade.test();
		// TestBridgePattern.test();
		// TestObserver.test();
	}

}

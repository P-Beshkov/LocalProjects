package performance;

public class Worker implements Runnable {

	private boolean isEnded;
	private int counter;
	@Override
	public void run() {
		while (!isEnded) {
			System.out.println("Working: " + counter++);
		}
	}

	private int doWork(boolean isEnded) {
		int result = 0;
		for (int i = 0; i < 100; i++) {
			result += i;
		}
		if (isEnded) {
			System.out.println("Last batch of work");
		}

		return result;
	}

	public void endWork() {

		isEnded = true;
		System.out.println("End work: " + counter);
	}

}

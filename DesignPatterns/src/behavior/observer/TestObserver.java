package behavior.observer;

import java.util.ArrayList;

public class TestObserver {

	public static void test() {
		Subject subject = new CommentaryObject(new ArrayList<Observer>(), "Soccer Match [2014AUG24]");
		Observer observer = new SMSUsers(subject, "Adam Warner [New York]");
		subject.subscribeObserver(observer);		
		//observer.subscribe();
				
		Observer observer2 = new SMSUsers(subject, "Tim Ronney [London]");
		subject.subscribeObserver(observer2);
		//observer2.subscribe();
		
		Commentary cObject = ((Commentary)subject);
		cObject.setDesc("Welcome to live Soccer match");
		cObject.setDesc("Current score 0-0");
		
		System.out.println();
		
		observer2.unSubscribe();
		
		System.out.println();
		
		cObject.setDesc("It's a goal!!");
		cObject.setDesc("Current score 1-0");
		
		Observer observer3 = new SMSUsers(subject, "Marrie [Paris]");
		observer3.subscribe();
		
		System.out.println();
		
		cObject.setDesc("It's another goal!!");
		cObject.setDesc("Half-time score 2-0");		
	}

}

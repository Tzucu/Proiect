package testCase;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Testar {
	//parcurgem toate testele din clasa CaseTest si in cazul in care un test esueaza este afisat
	//la consola implicand rezultat final ca fiind esuat.
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(SuiteTest.class);
		for (Failure f : result.getFailures()){
			System.out.println(f.toString());
		}
		System.out.println(result.wasSuccessful());
	}
}

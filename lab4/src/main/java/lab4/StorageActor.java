package lab4;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageActor extends AbstractActor {
    private Map<String, ArrayList<TestData>> store = new HashMap<>();

    private void putTest(TestData testData) {
        String packageId = testData.getParentPackage().getPackageId();
        if (this.store.containsKey(packageId)) {
            this.store.get(packageId).add(testData);
            System.out.println(store);
        } else {
            ArrayList<TestData> tests = new ArrayList<>();
            tests.add(testData);
            this.store.put(packageId, tests);
            System.out.println(store);
        }
    }

    private Map<String, Boolean> makeResult(String packageId) {
        ArrayList<TestResult> testAnswers = new ArrayList<>();
        Map<String, Boolean> result = new HashMap<>();
        if (this.store.containsKey(packageId)) {
            for (TestData test : this.store.get(packageId)) {
                String actualResult = test.getActualResult();
                String expectedResult = test.getExpectedResult();
                TestResult testResult = new TestResult(expectedResult, actualResult, actualResult.equals(expectedResult));
                testAnswers.add(testResult);
                System.out.println(test.getTestName() + "\t" + actualResult.equals(expectedResult));
                result.put(test.getTestName(), actualResult.equals(expectedResult));
            }
            //return new Result(packageId, testAnswers);
            // } else {
            //    return new Result("No such package", testAnswers);
            //}
        }
        System.out.println(result);
        return result;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(TestData.class, test -> this.putTest(test))
                .match(String.class, id -> sender().tell(makeResult(id), self()))
                .build();
    }
}

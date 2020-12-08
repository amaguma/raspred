package lab4;

import akka.actor.AbstractActor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageActor extends AbstractActor {
    private Map<String, ArrayList<TestData>> store = new HashMap<>();

    private void putTest(TestData testData) {
        String packageId = testData.getParentPackage().getPackageId();
        if (this.store.containsKey(packageId)) {
            this.store.get(packageId).add(testData);
        } else {
            ArrayList<TestData> tests = new ArrayList<>();
            tests.add(testData);
            this.store.put(packageId, tests);
        }
    }
}

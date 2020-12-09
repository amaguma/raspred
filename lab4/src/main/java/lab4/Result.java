package lab4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {

    private final String PACKAGE_ID = "packageId";
    private final String TESTS = "tests";

    private String packageId;
    private List<TestResult> tests;

    public Result(String packageId,
                  List<TestResult> tests) {
        this.packageId = packageId;
        this.tests = tests;
    }
}

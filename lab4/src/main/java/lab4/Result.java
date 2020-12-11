package lab4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable {

    private final String PACKAGE_ID = "packageId";
    private final String TESTS = "tests";

    private String packageId;
    private ArrayList<TestResult> tests;

    @JsonCreator
    public Result(@JsonProperty(PACKAGE_ID) String packageId,
                  @JsonProperty(TESTS) ArrayList<TestResult> tests) {
        this.packageId = packageId;
        this.tests = tests;
    }
}

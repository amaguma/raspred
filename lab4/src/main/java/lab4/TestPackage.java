package lab4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TestPackage implements Serializable {

    private static final String PACKAGE_ID = "packageId";
    private static final String FUNCTION_NAME = "functionName";
    private static final String JS_SCRIPT = "jsScript";
    private static final String TESTS = "tests";

    private String packageId;
    private String code;
    private String functionName;
    private List<TestData> tests;

    public TestPackage (@JsonProperty(PACKAGE_ID) String packageId,
                        @JsonProperty(JS_SCRIPT) String code,
                        @JsonProperty(FUNCTION_NAME) String functionName,
                        @JsonProperty(TESTS) List<TestData> tests) {
        this.packageId = packageId;
        this.code = code;
        this.functionName = functionName;
        this.tests = tests;
    }

    public  String getPackageId() {
        return this.packageId;
    }

    public String getCode() {
        return this.code;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public List<TestData> getTests() {
        return this.tests;
    }
}

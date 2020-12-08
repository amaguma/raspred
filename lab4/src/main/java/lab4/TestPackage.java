package lab4;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TestPackage implements Serializable {

    private final String PACKAGE_ID = "packageId";
    private final String FUNCTION_NAME = "functionName";
    private final String JS_SCRIPT = "jsScript";
    private final String TESTS = "tests";

    private String packageId;
    private String code;
    private String functionName;
    private List<TestData> tests;

    public TestPackage (@JsonProperty(PACKAGE_ID) String packageId,
                        @JsonProperty(JS_SCRIPT) String code,
                        @JsonProperty(FUNCTION_NAME) String functionName,
                        @JsonProperty(TESTS) List<TestData> tests) {
        
    }
}

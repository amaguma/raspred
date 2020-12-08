package lab4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TestData implements Serializable {

    private final String TEST_NAME = "testName";
    private final String EXPECTED_RES = "expectedResult";
    private final String PARAMS = "params";

    private String testName;
    private String actualResult;
    private String expectedResult;
    private Object[] params;

    @JsonCreator
    public TestData(@JsonProperty("testName") String testName,
                    @JsonProperty("expectedResult") String expectedResult,
                    @JsonProperty("params") Object[] params) {
        this.testName = testName;
        this.expectedResult = expectedResult;
        this.params = params;
    }

}



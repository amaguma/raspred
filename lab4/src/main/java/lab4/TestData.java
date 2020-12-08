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
    public TestData(@JsonProperty(TEST_NAME) String testName,
                    @JsonProperty(EXPECTED_RES) String expectedResult,
                    @JsonProperty(PARAMS) Object[] params) {
        this.testName = testName;
        this.expectedResult = expectedResult;
        this.params = params;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }

    public String getActualResult() {
        return this.actualResult;
    }

    public String getTestName() {
        return this.testName;
    }

    public String getExpectedResult() {
        return this.expectedResult;
    }

    public Object[] getParams() {
        return this.params;
    }
}



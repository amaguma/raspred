package lab4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TestData implements Serializable {



    private String testName;
    private String actualResult;
    private String expectedResult;
    private Object params;

    @JsonCreator
    public TestData(@JsonProperty("testName") String)

}



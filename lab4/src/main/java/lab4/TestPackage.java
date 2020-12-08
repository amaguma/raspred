package lab4;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TestPackage implements Serializable {

    private final String PACKAGE_ID = "packageId";
    private final String 

    private String packageId;
    private String functionName;
    private String jsScript;
    private List<TestData> tests;

    public TestPackage (@JsonProperty())
}

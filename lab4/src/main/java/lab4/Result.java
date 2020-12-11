package lab4;


import java.io.Serializable;
import java.util.Map;

public class Result implements Serializable {
    
    private String packageId;
    private Map<String, Boolean> tests;

    public Result(String packageId, Map<String, Boolean> tests) {
        this.packageId = packageId;
        this.tests = tests;
    }
}

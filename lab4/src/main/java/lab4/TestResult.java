//package lab4;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//import java.io.Serializable;
//
//public class TestResult implements Serializable {
//    private final String EXPECTED = "expected";
//    private final String ACTUAL = "actual";
//    private final String SUCCES = "succes";
//
//    private String expectedResult;
//    private String actualResult;
//    private Boolean succes;
//
//    @JsonCreator
//    public TestResult(@JsonProperty(EXPECTED) String expectedResult,
//                      @JsonProperty(ACTUAL) String actualResult,
//                      @JsonProperty(SUCCES) Boolean succes) {
//        this.expectedResult = expectedResult;
//        this.actualResult = actualResult;
//        this.succes = succes;
//    }
//}

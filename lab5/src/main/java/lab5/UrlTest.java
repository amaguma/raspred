package lab5;

public class UrlTest {
    private final String url;
    private final Integer time;

    public UrlTest(String url, Integer time) {
        this.url = url;
        this.time = time;
    }

    public String getUrl() {
        return this.url;
    }

    public Integer getTime() {
        return this.time;
    }
}

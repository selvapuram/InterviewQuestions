package revolut.domain;

public class Server {

    private final String url;

    public Server(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public boolean isValid() {
        return this.url == null || this.url.isEmpty();
    }
}

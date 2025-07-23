package upvest;

public class Consumer {

    private final String id;

    public Consumer(String id) {
        this.id = id;
    }

    public void listen(String message) {
        System.out.println("consumer:" + id + message);
    }

}

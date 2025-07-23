package upvest;

import java.util.ArrayList;
import java.util.List;

public class Log {

    private List<String> messages;


    public Log(int size) {
        messages = new ArrayList<>(size);
    }
}

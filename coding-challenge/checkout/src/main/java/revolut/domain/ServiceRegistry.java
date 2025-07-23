package revolut.domain;

import revolut.exception.AddressAlreadyExistsException;
import revolut.exception.InvalidServerException;
import revolut.exception.ServerLimitReachedException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {

    private final Map<String, Server> servers = new ConcurrentHashMap<>();// in-memory to store the address:instance
    private final int MAX_LIMIT = 10;

    public void addServer(Server server) {

        if(server.isValid()) {
            throw new InvalidServerException("unable to add the server: " + server.getUrl());
        }

        if (servers.containsKey(server.getUrl())) {
            throw new AddressAlreadyExistsException("server already added in the registry: " + server.getUrl());
        }

        int serverCount = getServerCount();
        if (serverCount >= MAX_LIMIT) {
            throw new ServerLimitReachedException("server threshold limit is reached: " + serverCount);
        } else {
            servers.put(server.getUrl(), server);
        }

    }

    public boolean isEmpty() {
        return servers.isEmpty();
    }

    public void clear() {
        servers.clear();
    }

    public int getServerCount() {
        return servers.size();
    }
}

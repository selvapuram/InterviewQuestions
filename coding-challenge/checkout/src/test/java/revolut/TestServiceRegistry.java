package revolut;


import org.junit.Assert;
import org.junit.Test;
import revolut.domain.Server;
import revolut.domain.ServiceRegistry;
import revolut.exception.AddressAlreadyExistsException;
import revolut.exception.InvalidServerException;
import revolut.exception.ServerLimitReachedException;


/*
*
*
* Register instances

1. It should be possible to register an instance, identified by an address.(url)

2. Each address should be unique, it should not be possible to register the same address more than once

3. Service Registry should accept up to 10 addresses,
*
* SR -> address A -> instance A -> add the same instance -> throw an instance al
* */
public class TestServiceRegistry {

    private ServiceRegistry serviceRegistry = new ServiceRegistry();

    @Test
    public void shouldReturnOneWhenAddingInstanceToRegistryGivenAddressNotExists() {
        // arrange
        serviceRegistry.clear();
        String url = "https://example.com";
        Server server = new Server(url);

        // act
        serviceRegistry.addServer(server);

        // assert
        int expectedCount = 1;
        Assert.assertSame(expectedCount, serviceRegistry.getServerCount());
    }

    @Test(expected = AddressAlreadyExistsException.class)
    public void shouldThrowExceptionWhenAddingInstanceToRegistryGivenAddressAlreadyExists() {
        // arrange
        serviceRegistry.clear();
        String url = "https://example.com";
        Server server = new Server(url);
        serviceRegistry.addServer(server);

        //act
        url = "https://example.com";
        server = new Server(url);
        serviceRegistry.addServer(server);
    }

    @Test(expected = ServerLimitReachedException.class)
    public void shouldThrowExceptionWhenAddingInstanceToRegistryGivenTenServers() {
        serviceRegistry.clear();
        for(int i = 0; i < 11; i++) {
            String url = "https://example" +  i +".com";
            Server server = new Server(url);
            serviceRegistry.addServer(server);
        }
    }

    @Test(expected = AddressAlreadyExistsException.class)
    public void shouldThrowExceptionWhenAddingMultipleInstancesToRegistryGivenAddressAlreadyExists() {
        serviceRegistry.clear();
        for(int i = 0; i < 4; i++) {
            String url = "https://example" +  i +".com";
            Server server = new Server(url);
            serviceRegistry.addServer(server);
        }

        for(int i = 0; i < 5; i++) {
            String url = "https://example" +  i +".com";
            Server server = new Server(url);
            serviceRegistry.addServer(server);
        }
    }

    @Test(expected = InvalidServerException.class)
    public void shouldThrowExceptionWhenAddingInvalidInstanceToRegistryGivenNullServer() {
        // arrange
        serviceRegistry.clear();
        String url = null;
        Server server = new Server(url);

        // act
        serviceRegistry.addServer(server);

        // assert
        int expectedCount = 1;
        Assert.assertSame(expectedCount, serviceRegistry.getServerCount());
    }

}

package revolut;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class TestHelloWorld {

    @Test
    public void testSayHello() {
        HelloWorld tom = new HelloWorld();
        assertSame("Hello World", tom.sayHello());
    }
}

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

// TDD -> Red, Green, Refactor


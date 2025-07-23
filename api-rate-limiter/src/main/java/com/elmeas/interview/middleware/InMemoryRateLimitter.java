package com.elmeas.interview.middleware;

import com.elmeas.interview.exception.TooManyRequestsException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRateLimitter implements RateLimitter {

    private Map<String, AtomicInteger> inMemoryLookup = new ConcurrentHashMap<>();

    private static final Integer RATE_LIMIT_PER_USER = 100;


    @Override
    public void filter(String userId) throws TooManyRequestsException {
        AtomicInteger currentCount = inMemoryLookup.getOrDefault(userId, new AtomicInteger(0));

    }
}

package com.elmeas.interview.middleware;


import com.elmeas.interview.exception.TooManyRequestsException;

public interface RateLimitter {
    public void filter(String userId) throws TooManyRequestsException
}

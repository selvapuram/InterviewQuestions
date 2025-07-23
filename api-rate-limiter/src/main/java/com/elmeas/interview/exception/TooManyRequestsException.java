package com.elmeas.interview.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

public class TooManyRequestsException extends HttpClientErrorException {
    public TooManyRequestsException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }
}

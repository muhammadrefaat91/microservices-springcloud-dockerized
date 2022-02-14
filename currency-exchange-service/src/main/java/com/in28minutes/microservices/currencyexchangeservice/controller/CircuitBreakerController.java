package com.in28minutes.microservices.currencyexchangeservice.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {
    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
    //@Retry(name = "sample-api", fallbackMethod = "hardCodedResponse")
   @CircuitBreaker(name = "sample-api", fallbackMethod = "hardCodedResponse")
   // @RateLimiter(name = "default")
    public String sampleAPI() {
        logger.info("Sample API call received");

        new RestTemplate().getForEntity("localhost:8080", String.class);
        return "Sample API";
    }

    @GetMapping("/sample-api-ratelimiter")
    @RateLimiter(name = "default")
//    @Bulkhead(name = "default")
    public String sampleAPI2() {
         return "Sample API for rate limiter";
    }

    public String hardCodedResponse(Exception exception) {
        return "fallback-response";
    }
}

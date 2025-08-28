Currency Converter with Rate Service
Problem Statement

We are given a RateService interface that provides currency conversion rates between two currencies:

// Assume a rate service which gives the rate fromCurrency to toCurrency
// Example: EUR -> GBP = 0.01, EUR -> USD = 1.23
interface RateService {
    BigDecimal rate(Currency fromCurrency, Currency toCurrency);
}


We need to implement a Converter class that can convert a given amount from one currency to another:

class Converter {
    RateService rateService; // dependency injection

    public BigDecimal convert(BigDecimal amount, Currency fromCurrency, Currency toCurrency);
}

Requirements

Support at least 1000 requests/sec.

Ensure thread safety under concurrent access.

Minimize calls to RateService (could be expensive).

Handle rate caching with refresh.

Use pragmatic and production-ready concurrency patterns.

Solution Draft

The key challenge is efficiently handling concurrent conversion requests while avoiding repeated RateService calls.

Design Decisions

Concurrent Cache

Use ConcurrentHashMap<String, RateHolder> to store exchange rates.

Cache key format: "EUR->USD".

TTL-based Refresh

Each rate entry has a timestamp.

If stale (e.g., older than 10s), it is refreshed.

Concurrency Control

computeIfAbsent ensures single fetch for missing keys.

Double-checked locking prevents multiple refreshes.

Scalability

Only the thread hitting stale entry refreshes it; others keep using old value.

This keeps latency predictable.

Implementation
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.concurrent.*;

class Converter {
    private final RateService rateService;

    // cache key = "EUR->USD"
    private final Map<String, RateHolder> rateCache = new ConcurrentHashMap<>();
    private final long ttlMillis = 10_000; // refresh every 10s

    public Converter(RateService rateService) {
        this.rateService = rateService;
    }

    public BigDecimal convert(BigDecimal amount, Currency fromCurrency, Currency toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        String key = fromCurrency.getCurrencyCode() + "->" + toCurrency.getCurrencyCode();
        RateHolder holder = rateCache.computeIfAbsent(key, k -> fetchRate(fromCurrency, toCurrency));

        // check TTL expiration
        if (System.currentTimeMillis() - holder.timestamp > ttlMillis) {
            synchronized (holder) {
                if (System.currentTimeMillis() - holder.timestamp > ttlMillis) {
                    holder.rate = rateService.rate(fromCurrency, toCurrency);
                    holder.timestamp = System.currentTimeMillis();
                }
            }
        }

        return amount.multiply(holder.rate);
    }

    private RateHolder fetchRate(Currency from, Currency to) {
        return new RateHolder(rateService.rate(from, to), System.currentTimeMillis());
    }

    private static class RateHolder {
        volatile BigDecimal rate;
        volatile long timestamp;

        RateHolder(BigDecimal rate, long timestamp) {
            this.rate = rate;
            this.timestamp = timestamp;
        }
    }
}

Possible Follow-Ups

Async Refresh: Use ScheduledExecutorService to refresh rates in the background.

Inverse Lookup Optimization: If USD->EUR is missing but EUR->USD exists, compute 1/rate.

Fallback Handling: If RateService fails, serve last known rate.

Distributed Cache: For multi-node deployments, move cache to Redis or Hazelcast.

Prefetch Hot Pairs: Warm up cache for commonly used currency pairs.

✅ This solution balances throughput (1000 req/sec), concurrency, and freshness in a pragmatic way.

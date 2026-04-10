# In-Memory Pub-Sub: One-Page Interview Sheet

## Problem
Design and implement an in-memory pub-sub component that:
- publishes a string message to any number of subscribers
- simulates subscriber work with a 1 second delay
- ensures subscribers do not block each other while processing
- preserves FIFO order per subscriber

---

## Design
Use **one buffered channel per subscriber** and **one worker goroutine per subscriber**.

### Core idea
- `Subscribe(id)` creates a subscriber-specific mailbox.
- `Publish(msg)` fans out the message into every subscriber mailbox.
- Each subscriber consumes its own mailbox serially.

### Why this works
- **Fan-out:** every subscriber gets every message.
- **Isolation:** each subscriber processes independently.
- **FIFO:** each subscriber reads from a single queue in order.

---

## Clean mental model
- Broker responsibility: maintain subscribers and fan out messages.
- Subscriber responsibility: read messages one by one and do work.

This is the cleanest baseline because it is correct, simple, and easy to explain.

---

## Trade-off
The main trade-off is slow consumers.

If one subscriber falls behind and its mailbox fills up:
- `Publish` may block on that subscriber
- no messages are lost by default
- backpressure is applied to the publisher

That is often acceptable for a baseline interview solution, as long as you say it clearly.

---

## Alternatives
### 1. Goroutine per send
`go func() { ch <- msg }()`

**Pros**
- publish loop appears non-blocking

**Cons**
- can break FIFO for the same subscriber
- creates many goroutines under load
- harder to reason about

**Verdict:** avoid for this problem.

### 2. Non-blocking send with drop
Use `select { case ch <- msg: default: }`

**Pros**
- publisher does not block on slow subscribers
- bounded resource use

**Cons**
- messages can be dropped
- weaker delivery guarantee

**Verdict:** good only when best-effort delivery is acceptable.

### 3. Evict slow subscribers
Disconnect subscribers whose queues stay full.

**Pros**
- protects system health
- keeps latency bounded

**Cons**
- subscriber may miss future messages
- more lifecycle complexity

---

## Strong 30-second explanation
> I use one buffered channel per subscriber and one goroutine per subscriber. Publishing fans out by sending the message into each subscriber’s mailbox. That preserves FIFO per subscriber because each subscriber consumes serially from its own queue. Subscribers process independently, so one subscriber’s 1 second work does not block another’s processing. The main trade-off is that a slow subscriber can eventually backpressure `Publish` if its buffer fills.

---

## Common follow-up probes and strong answers

### Why not one shared channel?
Because subscribers would compete for messages, turning it into work distribution instead of pub-sub.

### Why not goroutine per send?
Because it can reorder messages for the same subscriber and create unnecessary goroutine overhead.

### Does this fully prevent blocking?
It prevents subscribers from blocking each other while processing, but a slow subscriber can still block publishing when its queue fills.

### Why buffered channels?
They absorb short bursts and decouple publishing from immediate readiness of the subscriber.

### Complexity?
- `Publish`: **O(n)** where `n` is the number of subscribers
- Memory: proportional to total subscriber queue capacity

---

## What the tests validate
The included Go tests check:
- fan-out to all subscribers
- FIFO per subscriber
- duplicate subscriber rejection
- publishing after close fails
- subscribing after close fails

Run with:

```bash
go test ./...
```

package pubsub

import (
	"fmt"
	"sync"
)

type subscriber struct {
	id string
	ch chan string
}

type PubSub struct {
	mu          sync.RWMutex
	subscribers map[string]*subscriber
	closed      bool
}

func NewPubSub() *PubSub {
	return &PubSub{
		subscribers: make(map[string]*subscriber),
	}
}

// Subscribe registers a subscriber and returns its personal mailbox.
// The mailbox is receive-only for callers.
func (ps *PubSub) Subscribe(id string) (<-chan string, error) {
	ps.mu.Lock()
	defer ps.mu.Unlock()

	if ps.closed {
		return nil, fmt.Errorf("pubsub is closed")
	}
	if _, exists := ps.subscribers[id]; exists {
		return nil, fmt.Errorf("subscriber %q already exists", id)
	}

	sub := &subscriber{
		id: id,
		ch: make(chan string, 16),
	}
	ps.subscribers[id] = sub
	return sub.ch, nil
}

// Publish fans out a message to all subscribers.
// FIFO is preserved per subscriber because each subscriber has a single queue.
func (ps *PubSub) Publish(msg string) error {
	ps.mu.RLock()
	if ps.closed {
		ps.mu.RUnlock()
		return fmt.Errorf("pubsub is closed")
	}

	snapshot := make([]*subscriber, 0, len(ps.subscribers))
	for _, sub := range ps.subscribers {
		snapshot = append(snapshot, sub)
	}
	ps.mu.RUnlock()

	for _, sub := range snapshot {
		sub.ch <- msg
	}
	return nil
}

func (ps *PubSub) Close() {
	ps.mu.Lock()
	defer ps.mu.Unlock()

	if ps.closed {
		return
	}
	ps.closed = true
	for _, sub := range ps.subscribers {
		close(sub.ch)
	}
}

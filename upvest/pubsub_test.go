package pubsub

import (
	"reflect"
	"testing"
	"time"
)

func receiveN(t *testing.T, ch <-chan string, n int, timeout time.Duration) []string {
	t.Helper()
	got := make([]string, 0, n)
	deadline := time.After(timeout)
	for len(got) < n {
		select {
		case msg, ok := <-ch:
			if !ok {
				t.Fatalf("channel closed early: got %v, want %d messages", got, n)
			}
			got = append(got, msg)
		case <-deadline:
			t.Fatalf("timed out waiting for %d messages; got %v", n, got)
		}
	}
	return got
}

func TestPublishFanoutAndFIFOPerSubscriber(t *testing.T) {
	ps := NewPubSub()
	defer ps.Close()

	a, err := ps.Subscribe("A")
	if err != nil {
		t.Fatalf("subscribe A: %v", err)
	}
	b, err := ps.Subscribe("B")
	if err != nil {
		t.Fatalf("subscribe B: %v", err)
	}
	c, err := ps.Subscribe("C")
	if err != nil {
		t.Fatalf("subscribe C: %v", err)
	}

	want := []string{"msg-1", "msg-2", "msg-3"}
	for _, msg := range want {
		if err := ps.Publish(msg); err != nil {
			t.Fatalf("publish %q: %v", msg, err)
		}
	}

	for name, ch := range map[string]<-chan string{"A": a, "B": b, "C": c} {
		got := receiveN(t, ch, len(want), time.Second)
		if !reflect.DeepEqual(got, want) {
			t.Fatalf("subscriber %s got %v, want %v", name, got, want)
		}
	}
}

func TestDuplicateSubscriberRejected(t *testing.T) {
	ps := NewPubSub()
	defer ps.Close()

	if _, err := ps.Subscribe("A"); err != nil {
		t.Fatalf("first subscribe failed: %v", err)
	}
	if _, err := ps.Subscribe("A"); err == nil {
		t.Fatal("expected duplicate subscriber error, got nil")
	}
}

func TestPublishAfterCloseFails(t *testing.T) {
	ps := NewPubSub()
	ps.Close()

	if err := ps.Publish("msg"); err == nil {
		t.Fatal("expected publish after close to fail, got nil")
	}
}

func TestSubscribeAfterCloseFails(t *testing.T) {
	ps := NewPubSub()
	ps.Close()

	if _, err := ps.Subscribe("A"); err == nil {
		t.Fatal("expected subscribe after close to fail, got nil")
	}
}

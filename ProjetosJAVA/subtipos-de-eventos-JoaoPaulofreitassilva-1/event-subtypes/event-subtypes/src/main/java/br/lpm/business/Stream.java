package br.lpm.business;

public class Stream {
    private static final int MAX_EVENTS = 100;
    private Event[] events;
    private int head;
    private int tail;
    private int size;

    public Stream() {
        this.events = new Event[MAX_EVENTS];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    public static int getMaxEvents() {
        return MAX_EVENTS;
    }

    public void publish(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("O evento não pode ser nulo.");
        }
        if (size < MAX_EVENTS) {
            events[tail] = event;
            tail = (tail + 1) % MAX_EVENTS;
            size++;
        } else {
            throw new IllegalStateException("Stream atingiu o limite máximo de eventos.");
        }
    }

    public Event consume() {
        if (size == 0) {
            throw new IllegalStateException("Stream está vazio.");
        }
        Event event = events[head];
        events[head] = null;
        head = (head + 1) % MAX_EVENTS;
        size--;
        return event;
    }

    public Event get() {
        if (size == 0) {
            throw new IllegalStateException("Stream está vazio.");
        }
        return events[head];
    }

    public void removeAll() {
        for (int i = 0; i < MAX_EVENTS; i++) {
            events[i] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

    public int size() {
        return size;
    }
}

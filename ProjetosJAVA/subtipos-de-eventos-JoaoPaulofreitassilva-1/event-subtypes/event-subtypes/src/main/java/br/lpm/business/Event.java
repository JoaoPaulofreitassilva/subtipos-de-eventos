package br.lpm.business;

import java.time.LocalDateTime;

public class Event {
    private LocalDateTime timestamp;
    private Content eventBody;

    public Event(Content eventBody) {
        validateEventBody(eventBody);
        this.timestamp = LocalDateTime.now();
        this.eventBody = eventBody;
    }

    private void validateEventBody(Content eventBody) {
        if (eventBody == null) {
            throw new IllegalArgumentException("O conteúdo do evento não deve ser nulo.");
        }
        if (!(eventBody instanceof Ticket || eventBody instanceof Sale)) {
            throw new IllegalArgumentException("Tipo de conteúdo não suportado.");
        }
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Content getEventBody() {
        return eventBody;
    }

    @Override
    public String toString() {
        return "Event[timestamp=" + timestamp + ", eventBody=" + eventBody + "]";
    }
}
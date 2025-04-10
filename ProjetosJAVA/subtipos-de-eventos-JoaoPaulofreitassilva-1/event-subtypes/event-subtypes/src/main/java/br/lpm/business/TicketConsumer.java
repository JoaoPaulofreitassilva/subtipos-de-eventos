package br.lpm.business;

public class TicketConsumer extends EventConsumer {
    
    @Override
    protected boolean isValidEventType(Event event) {
        return event.getEventBody() instanceof Ticket;
    }

    @Override
    protected float getValue(Event event) {
        return ((Ticket) event.getEventBody()).getValue();
    }

    @Override
    public float percentEvent(String event) {
        if (consumedCount == 0) return 0f;

        int count = 0;
        for (int i = 0; i < consumedCount; i++) {
            Ticket ticket = (Ticket) consumedEvents[i].getEventBody();
            if (ticket.getEvent().equalsIgnoreCase(event)) {
                count++;
            }
        }
        return (count * 100f) / consumedCount;
    }

    @Override
    public String modeEvent() {
        if (consumedCount == 0) return null;

        String mode = null;
        int maxFrequency = 0;

        for (int i = 0; i < consumedCount; i++) {
            Ticket ticket = (Ticket) consumedEvents[i].getEventBody();
            String currentEvent = ticket.getEvent();
            int frequency = 0;

            for (int j = 0; j < consumedCount; j++) {
                Ticket other = (Ticket) consumedEvents[j].getEventBody();
                if (other.getEvent().equalsIgnoreCase(currentEvent)) {
                    frequency++;
                }
            }

            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                mode = currentEvent;
            }
        }
        return mode;
    }
}
package br.lpm.business;

public abstract class EventConsumer {
    protected Event[] consumedEvents;
    protected int consumedCount;
    private static final int MAX_CONSUMED = 100;

    public EventConsumer() {
        this.consumedEvents = new Event[MAX_CONSUMED];
        this.consumedCount = 0;
    }

    public void consumeStream(Stream stream) {
        if (stream.size() > 0) {
            if (consumedCount < MAX_CONSUMED) {
                Event event = stream.consume();
                if (isValidEventType(event)) {
                    consumedEvents[consumedCount++] = event;
                } else {
                    throw new IllegalArgumentException("Tipo de evento inválido para este consumidor.");
                }
            } else {
                throw new IllegalStateException("Limite máximo de eventos consumidos atingido.");
            }
        } else {
            throw new IllegalStateException("Stream está vazio.");
        }
    }

    protected abstract boolean isValidEventType(Event event);
    protected abstract float getValue(Event event);

    public float maxValue() {
        if (consumedCount == 0) return 0f;
        
        float max = getValue(consumedEvents[0]);
        for (int i = 1; i < consumedCount; i++) {
            float current = getValue(consumedEvents[i]);
            if (current > max) max = current;
        }
        return max;
    }

    public float minValue() {
        if (consumedCount == 0) return 0f;
        
        float min = getValue(consumedEvents[0]);
        for (int i = 1; i < consumedCount; i++) {
            float current = getValue(consumedEvents[i]);
            if (current < min) min = current;
        }
        return min;
    }

    public float avgValue() {
        if (consumedCount == 0) return 0f;
        
        float sum = 0;
        for (int i = 0; i < consumedCount; i++) {
            sum += getValue(consumedEvents[i]);
        }
        return sum / consumedCount;
    }

    public abstract float percentEvent(String event);
    public abstract String modeEvent();
}

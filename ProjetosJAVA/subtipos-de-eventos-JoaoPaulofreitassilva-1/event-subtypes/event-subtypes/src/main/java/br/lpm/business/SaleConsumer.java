package br.lpm.business;

public class SaleConsumer extends EventConsumer {
    
    @Override
    protected boolean isValidEventType(Event event) {
        return event.getEventBody() instanceof Sale;
    }

    @Override
    protected float getValue(Event event) {
        return ((Sale) event.getEventBody()).getValue();
    }

    @Override
    public float percentEvent(String event) {
        if (consumedCount == 0) return 0f;

        int count = 0;
        for (int i = 0; i < consumedCount; i++) {
            Sale sale = (Sale) consumedEvents[i].getEventBody();
            if (sale.getDescription().equalsIgnoreCase(event)) {
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
            Sale sale = (Sale) consumedEvents[i].getEventBody();
            String currentDescription = sale.getDescription();
            int frequency = 0;

            for (int j = 0; j < consumedCount; j++) {
                Sale other = (Sale) consumedEvents[j].getEventBody();
                if (other.getDescription().equalsIgnoreCase(currentDescription)) {
                    frequency++;
                }
            }

            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                mode = currentDescription;
            }
        }
        return mode;
    }
}
package br.lpm.business;

import java.time.LocalDateTime;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class StreamMonitor {
    private final Stream stream;
    private int eventCount;
    private String[] eventTypes;
    private int[] eventTypeCount;
    private LocalDateTime startTime;
    private Event[] processedEvents;
    private static final int MAX_EVENT_TYPES = 10;
    private static final int MAX_PROCESSED = 1000;
    private int typeCount;

    public StreamMonitor(Stream stream) {
        this.stream = stream;
        this.eventCount = 0;
        this.eventTypes = new String[MAX_EVENT_TYPES];
        this.eventTypeCount = new int[MAX_EVENT_TYPES];
        this.processedEvents = new Event[MAX_PROCESSED];
        this.typeCount = 0;
    }

    public void startMonitoring() {
        this.startTime = LocalDateTime.now();
    }

    public void stopMonitoring() {
        while (stream.size() > 0 && eventCount < MAX_PROCESSED) {
            processEvent(stream.consume());
        }
    }

    private void processEvent(Event event) {
        if (eventCount < MAX_PROCESSED) {
            processedEvents[eventCount] = event;
            updateEventTypeCount(event);
            eventCount++;
        }
    }

    private void updateEventTypeCount(Event event) {
        String type = event.getEventBody().getClass().getSimpleName();
        for (int i = 0; i < typeCount; i++) {
            if (eventTypes[i].equals(type)) {
                eventTypeCount[i]++;
                return;
            }
        }
        if (typeCount < MAX_EVENT_TYPES) {
            eventTypes[typeCount] = type;
            eventTypeCount[typeCount] = 1;
            typeCount++;
        }
    }

    public int getEventCount() {
        return eventCount;
    }

    public int getEventTypeCount(String eventType) {
        for (int i = 0; i < typeCount; i++) {
            if (eventTypes[i].equals(eventType)) {
                return eventTypeCount[i];
            }
        }
        return 0;
    }

    public String getMostFrequentEventType() {
        if (typeCount == 0) return null;
        
        int maxCount = eventTypeCount[0];
        String mostFrequent = eventTypes[0];
        
        for (int i = 1; i < typeCount; i++) {
            if (eventTypeCount[i] > maxCount) {
                maxCount = eventTypeCount[i];
                mostFrequent = eventTypes[i];
            }
        }
        return mostFrequent;
    }

    public float getAverageEventValue() {
        if (eventCount == 0) return 0f;
        
        float sum = 0f;
        for (int i = 0; i < eventCount; i++) {
            Content content = processedEvents[i].getEventBody();
            if (content instanceof Ticket) {
                sum += ((Ticket) content).getValue();
            } else if (content instanceof Sale) {
                sum += ((Sale) content).getValue();
            }  
        }
        return sum / eventCount;
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Relatório de Monitoramento ===\n");
        report.append("Início do monitoramento: ").append(startTime).append("\n");
        report.append("Total de eventos: ").append(eventCount).append("\n");
        report.append("Tipo mais frequente: ").append(getMostFrequentEventType()).append("\n");
        report.append("Valor médio: ").append(String.format("%.2f", getAverageEventValue())).append("\n");
        report.append("Distribuição por tipo:\n");
        
        for (int i = 0; i < typeCount; i++) {
            report.append(" - ").append(eventTypes[i])
                  .append(": ").append(eventTypeCount[i]).append("\n");
        }
        return report.toString();
    }

    public JFreeChart plotEventDistribution() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < typeCount; i++) {
            dataset.addValue(eventTypeCount[i], "Eventos", eventTypes[i]);
        }

        return ChartFactory.createBarChart(
            "Distribuição de Eventos",
            "Tipo de Evento",
            "Quantidade",
            dataset
        );
    }
}
package br.lpm.business;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class EventConsumerTest {
    
    private Stream stream;
    private TestConsumer consumer;
    private Event saleEvent;
    private Event ticketEvent;
    private LocalDate futureDate;  
    
    private class TestConsumer extends EventConsumer {
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
                if (sale.getDescription().equalsIgnoreCase(event)) count++;  // Mudança: equalsIgnoreCase
            }
            return (count * 100f) / consumedCount;
        }
        
        @Override
        public String modeEvent() {
            if (consumedCount == 0) return null;
            String mode = null;
            int maxCount = 0;
            
            for (int i = 0; i < consumedCount; i++) {
                Sale current = (Sale) consumedEvents[i].getEventBody();
                int count = 0;
                
                for (int j = 0; j < consumedCount; j++) {
                    Sale other = (Sale) consumedEvents[j].getEventBody();
                    if (current.getDescription().equalsIgnoreCase(other.getDescription())) {  // Mudança: equalsIgnoreCase
                        count++;
                    }
                }
                
                if (count > maxCount) {
                    maxCount = count;
                    mode = current.getDescription();
                }
            }
            return mode;
        }
    }
    
    @BeforeEach
    void setUp() {
        stream = new Stream();
        consumer = new TestConsumer();
        futureDate = LocalDate.now().plusDays(1);  // Inicialização da data futura
        
        Sale sale = new Sale("Venda Normal", 100f, futureDate, "Vendedor");  // Mudança: nome válido
        saleEvent = new Event(sale);
        
        Ticket ticket = new Ticket("Show Musical", 50f, futureDate, "Teatro");  // Mudança: nome válido
        ticketEvent = new Event(ticket);
    }
    
    @Test
    void shouldConsumeValidEvent() {
        stream.publish(saleEvent);
        consumer.consumeStream(stream);
        assertEquals(1, consumer.consumedCount);
    }
    
    @Test
    void shouldRejectInvalidEvent() {
        stream.publish(ticketEvent);
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> consumer.consumeStream(stream));
        assertEquals("Tipo de evento inválido para este consumidor.", exception.getMessage());
    }
    
    @Test
    void shouldCalculateMaxValue() {
        stream.publish(new Event(new Sale("Venda Normal", 100f, futureDate, "Vendedor")));
        stream.publish(new Event(new Sale("Venda Especial", 200f, futureDate, "Vendedor")));
        
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(200f, consumer.maxValue(), 0.01f);
    }
    
    @Test
    void shouldCalculateMinValue() {
        stream.publish(new Event(new Sale("Venda Normal", 100f, futureDate, "Vendedor")));
        stream.publish(new Event(new Sale("Venda Especial", 200f, futureDate, "Vendedor")));
        
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(100f, consumer.minValue(), 0.01f);
    }
    
    @Test
    void shouldCalculateAverageValue() {
        stream.publish(new Event(new Sale("Venda Normal", 100f, futureDate, "Vendedor")));
        stream.publish(new Event(new Sale("Venda Especial", 200f, futureDate, "Vendedor")));
        
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(150f, consumer.avgValue(), 0.01f);
    }
    
    @Test
    void shouldCalculatePercentEvent() {
        stream.publish(new Event(new Sale("Venda Normal", 100f, futureDate, "Vendedor")));
        stream.publish(new Event(new Sale("Venda Normal", 200f, futureDate, "Vendedor")));
        stream.publish(new Event(new Sale("Venda Especial", 300f, futureDate, "Vendedor")));
        
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(66.67f, consumer.percentEvent("Venda Normal"), 0.01f);
    }
    
    @Test
    void shouldFindModeEvent() {
        stream.publish(new Event(new Sale("Venda Normal", 100f, futureDate, "Vendedor")));
        stream.publish(new Event(new Sale("Venda Normal", 200f, futureDate, "Vendedor")));
        stream.publish(new Event(new Sale("Venda Especial", 300f, futureDate, "Vendedor")));
        
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals("Venda Normal", consumer.modeEvent());
    }
}
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.lpm.business.EventConsumer;
import br.lpm.business.Stream;
import br.lpm.business.Ticket;

public class EventConsumerTest {
    private EventConsumer consumer;
    private Stream stream;

    @BeforeEach
    void setUp() {
        consumer = new EventConsumer();
        stream = new Stream();
        
        stream.publish(new Ticket("Carnaval", 100.0f, LocalDate.of(2025, 2, 25), "Local A"));
        stream.publish(new Ticket("Páscoa", 50.0f, LocalDate.of(2025, 4, 20), "Local B"));
        stream.publish(new Ticket("Carnaval", 150.0f, LocalDate.of(2025, 2, 26), "Local C"));
    }

    @Test
void testConsumeStream() {
    int tamanhoInicial = stream.size();
    consumer.consumeStream(stream);
    
    assertEquals(tamanhoInicial - 1, stream.size(), "O stream diminuiu em 1");
    assertEquals(1, consumer.percentEvent("Carnaval"), "O evento foi registrado");
}

    @Test
    void testConsumeStreamEmpty() {
        // Consome todos os eventos primeiro
        stream.removeAll();
        
        // Tenta consumir do stream vazio
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> consumer.consumeStream(stream)
        );
        assertEquals("O stream está vazio!", exception.getMessage());
    }

    @Test
    void testMaxValue() {
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        assertEquals(150.0f, consumer.maxValue(), 0.01);
    }

    @Test
    void testMinValue() {
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        assertEquals(50.0f, consumer.minValue(), 0.01);
    }

    @Test
    void testAvgValue() {
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(75.0, consumer.avgValue(), 0.01);
    }

    @Test
    void testPercentEvent() {
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(66.67, consumer.percentEvent("Carnaval"), 0.01);
    }

    @Test
    void testModeEvent() {
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        assertEquals("Carnaval", consumer.modeEvent());
    }

    @Test
    void testMaxConsumedLimit() {
        
        for (int i = 0; i < 10; i++) {
            stream.publish(new Ticket("Evento" + i, 100.0f, LocalDate.now(), "Local"));
        }
        
        for (int i = 0; i < 10; i++) {
            consumer.consumeStream(stream);
        }

        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> consumer.consumeStream(stream)
        );
        assertEquals("Capacidade máxima foi atingida.", exception.getMessage());
    }

    @Test
    void testEmptyConsumerStatistics() {
        assertEquals(0.0f, consumer.maxValue());
        assertEquals(0.0f, consumer.minValue());
        assertEquals(0.0, consumer.avgValue());
        assertEquals(0.0, consumer.percentEvent("Qualquer"));
        assertNull(consumer.modeEvent());
    }
}

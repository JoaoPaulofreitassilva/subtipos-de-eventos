import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.lpm.business.*;

public class StreamTest {

    private Stream stream;
    private Event event1;
    private Event event2;
    private LocalDate futureDate;

    @BeforeEach
    public void setup() {
        stream = new Stream();
        futureDate = LocalDate.now().plusDays(1);
        
        Ticket ticket = new Ticket("Show Musical", 100f, futureDate, "Teatro Municipal");
        event1 = new Event(ticket);
        
        Sale sale = new Sale("Venda Normal", 200f, futureDate, "Vendedor Um");
        event2 = new Event(sale);
    }

    @Test
    public void testPublish_AddEvent() {
        stream.publish(event1);
        assertEquals(1, stream.size());
        assertEquals(event1, stream.get());
    }

    @Test
    public void testPublish_ExceptionFull() {
        for (int i = 0; i < Stream.getMaxEvents(); i++) {
            Ticket ticket = new Ticket("Show " + i, 10f * (i + 1), futureDate, "Local " + i);
            stream.publish(new Event(ticket));
        }
        assertThrows(IllegalStateException.class, 
            () -> stream.publish(event1), 
            "Stream deveria estar cheio");
    }

    @Test
    public void testConsume_RemovesAndReturnsHead() {
        stream.publish(event1);
        stream.publish(event2);

        Event consumed = stream.consume();
        assertEquals(event1, consumed);
        assertEquals(1, stream.size());
        assertEquals(event2, stream.get());
    }

    @Test
    public void testConsume_ThrowsExceptionWhenEmpty() {
        assertThrows(IllegalStateException.class, 
            () -> stream.consume(), 
            "Stream vazio deveria lançar exceção");
    }

    @Test
    public void testGet_ReturnsHeadWithoutRemove() {
        stream.publish(event1);
        Event head = stream.get();

        assertEquals(event1, head);
        assertEquals(1, stream.size(), "Tamanho do stream deveria permanecer o mesmo");
    }

    @Test
    public void testGet_ThrowsExceptionWhenEmpty() {
        assertThrows(IllegalStateException.class, 
            () -> stream.get(), 
            "Stream vazio deveria lançar exceção ao tentar get");
    }

    @Test
    public void testRemoveAll_ClearsStream() {
        stream.publish(event1);
        stream.publish(event2);

        stream.removeAll();
        assertEquals(0, stream.size());
        assertThrows(IllegalStateException.class, 
            () -> stream.get(), 
            "Stream deveria estar vazio após removeAll");
    }

    @Test
    public void testSize_ReturnsCorrectNumberOfEvents() {
        assertEquals(0, stream.size());
        
        stream.publish(event1);
        assertEquals(1, stream.size());
        
        stream.publish(event2);
        assertEquals(2, stream.size());
        
        stream.consume();
        assertEquals(1, stream.size());
    }

    @Test
    public void testPublish_NullEvent() {
        assertThrows(IllegalArgumentException.class, 
            () -> stream.publish(null), 
            "Publicar evento nulo deveria lançar exceção");
    }

    @Test
    public void testPublish_DifferentEventTypes() {
        stream.publish(event1);  // Ticket event
        stream.publish(event2);  // Sale event
        assertEquals(2, stream.size());
        assertEquals(event1, stream.get());
    }
}
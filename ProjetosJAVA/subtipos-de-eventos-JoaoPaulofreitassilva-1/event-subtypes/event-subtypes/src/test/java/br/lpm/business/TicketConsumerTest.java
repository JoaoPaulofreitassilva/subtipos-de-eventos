package br.lpm.business;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class TicketConsumerTest {
    
    private Stream stream;
    private TicketConsumer consumer;
    private LocalDate futureDate;
    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;
    private Event ticketEvent;
    private Event saleEvent;

    @BeforeEach
    void setUp() {
        stream = new Stream();
        consumer = new TicketConsumer();
        futureDate = LocalDate.now().plusDays(1);
        
        // Criar ingressos para teste
        ticket1 = new Ticket("Show Rock", 100.0f, futureDate, "Arena Show");
        ticket2 = new Ticket("Show Rock", 200.0f, futureDate, "Arena Show");
        ticket3 = new Ticket("Teatro", 150.0f, futureDate, "Teatro Municipal");
        
        // Criar eventos para teste de tipo
        ticketEvent = new Event(ticket1);
        saleEvent = new Event(new Sale("Venda Normal", 50.0f, futureDate, "Vendedor"));
    }

    @Test
    void shouldValidateTicketEventType() {
        assertTrue(consumer.isValidEventType(ticketEvent), 
            "Deve aceitar eventos do tipo Ticket");
        assertFalse(consumer.isValidEventType(saleEvent), 
            "Deve rejeitar eventos que não são Ticket");
    }

    @Test
    void shouldGetCorrectValue() {
        assertEquals(100.0f, consumer.getValue(ticketEvent), 0.01f, 
            "Deve retornar o valor correto do ingresso");
    }

    @Test
    void shouldCalculatePercentageOfEventType() {
        // Publicar eventos no stream
        stream.publish(new Event(ticket1));  // Show Rock
        stream.publish(new Event(ticket2));  // Show Rock
        stream.publish(new Event(ticket3));  // Teatro
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        // Verificar percentuais
        assertEquals(66.67f, consumer.percentEvent("Show Rock"), 0.01f, 
            "Deve calcular corretamente o percentual de 'Show Rock'");
        assertEquals(33.33f, consumer.percentEvent("Teatro"), 0.01f,
            "Deve calcular corretamente o percentual de 'Teatro'");
    }

    @Test
    void shouldReturnZeroPercentForEmptyConsumer() {
        assertEquals(0f, consumer.percentEvent("Show Rock"), 
            "Deve retornar 0% quando não há eventos consumidos");
    }

    @Test
    void shouldFindModeEventType() {
        // Publicar eventos no stream
        stream.publish(new Event(ticket1));  // Show Rock
        stream.publish(new Event(ticket2));  // Show Rock
        stream.publish(new Event(ticket3));  // Teatro
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals("Show Rock", consumer.modeEvent(), 
            "Deve encontrar o tipo de evento mais frequente");
    }

    @Test
    void shouldReturnNullModeForEmptyConsumer() {
        assertNull(consumer.modeEvent(), 
            "Deve retornar null quando não há eventos consumidos");
    }

    @Test
    void shouldCalculateMaxValue() {
        // Publicar eventos no stream
        stream.publish(new Event(ticket1));  // 100.0f
        stream.publish(new Event(ticket2));  // 200.0f
        stream.publish(new Event(ticket3));  // 150.0f
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(200.0f, consumer.maxValue(), 0.01f,
            "Deve encontrar o maior valor entre os ingressos");
    }

    @Test
    void shouldCalculateMinValue() {
        // Publicar eventos no stream
        stream.publish(new Event(ticket1));  // 100.0f
        stream.publish(new Event(ticket2));  // 200.0f
        stream.publish(new Event(ticket3));  // 150.0f
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(100.0f, consumer.minValue(), 0.01f,
            "Deve encontrar o menor valor entre os ingressos");
    }

    @Test
    void shouldCalculateAverageValue() {
        // Publicar eventos no stream
        stream.publish(new Event(ticket1));  // 100.0f
        stream.publish(new Event(ticket2));  // 200.0f
        stream.publish(new Event(ticket3));  // 150.0f
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(150.0f, consumer.avgValue(), 0.01f,
            "Deve calcular corretamente a média dos valores");
    }

    @Test
    void shouldThrowExceptionForInvalidType() {
        stream.publish(saleEvent);
        
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> consumer.consumeStream(stream),
            "Deve lançar exceção ao tentar consumir tipo inválido");
        
        assertEquals("Tipo de evento inválido para este consumidor.", 
            exception.getMessage());
    }
}
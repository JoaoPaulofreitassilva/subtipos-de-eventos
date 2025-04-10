package br.lpm.business;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class SaleConsumerTest {
    
    private Stream stream;
    private SaleConsumer consumer;
    private LocalDate futureDate;
    private Sale sale1;
    private Sale sale2;
    private Sale sale3;
    private Event saleEvent;
    private Event ticketEvent;

    @BeforeEach
    void setUp() {
        stream = new Stream();
        consumer = new SaleConsumer();
        futureDate = LocalDate.now().plusDays(1);
        
        // Criar vendas para teste
        sale1 = new Sale("Venda Normal", 100.0f, futureDate, "Vendedor Um");
        sale2 = new Sale("Venda Normal", 200.0f, futureDate, "Vendedor Dois");
        sale3 = new Sale("Venda Especial", 300.0f, futureDate, "Vendedor Tres");
        
        // Criar eventos para teste de tipo
        saleEvent = new Event(sale1);
        ticketEvent = new Event(new Ticket("Show", 50.0f, futureDate, "Teatro"));
    }

    @Test
    void shouldValidateSaleEventType() {
        assertTrue(consumer.isValidEventType(saleEvent), "Deve aceitar eventos do tipo Sale");
        assertFalse(consumer.isValidEventType(ticketEvent), "Deve rejeitar eventos que não são Sale");
    }

    @Test
    void shouldGetCorrectValue() {
        assertEquals(100.0f, consumer.getValue(saleEvent), 0.01f, "Deve retornar o valor correto da venda");
    }

    @Test
    void shouldCalculatePercentageOfEventType() {
        // Publicar eventos no stream
        stream.publish(new Event(sale1));  // Venda Normal
        stream.publish(new Event(sale2));  // Venda Normal
        stream.publish(new Event(sale3));  // Venda Especial
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        // Verificar percentuais
        assertEquals(66.67f, consumer.percentEvent("Venda Normal"), 0.01f, 
            "Deve calcular corretamente o percentual de 'Venda Normal'");
        assertEquals(33.33f, consumer.percentEvent("Venda Especial"), 0.01f,
            "Deve calcular corretamente o percentual de 'Venda Especial'");
    }

    @Test
    void shouldReturnZeroPercentForEmptyConsumer() {
        assertEquals(0f, consumer.percentEvent("Venda Normal"), 
            "Deve retornar 0% quando não há eventos consumidos");
    }

    @Test
    void shouldFindModeEventType() {
        // Publicar eventos no stream
        stream.publish(new Event(sale1));  // Venda Normal
        stream.publish(new Event(sale2));  // Venda Normal
        stream.publish(new Event(sale3));  // Venda Especial
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals("Venda Normal", consumer.modeEvent(), 
            "Deve encontrar o tipo de venda mais frequente");
    }

    @Test
    void shouldReturnNullModeForEmptyConsumer() {
        assertNull(consumer.modeEvent(), 
            "Deve retornar null quando não há eventos consumidos");
    }

    @Test
    void shouldCalculateMaxValue() {
        // Publicar eventos no stream
        stream.publish(new Event(sale1));  // 100.0f
        stream.publish(new Event(sale2));  // 200.0f
        stream.publish(new Event(sale3));  // 300.0f
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(300.0f, consumer.maxValue(), 0.01f,
            "Deve encontrar o maior valor entre as vendas");
    }

    @Test
    void shouldCalculateMinValue() {
        // Publicar eventos no stream
        stream.publish(new Event(sale1));  // 100.0f
        stream.publish(new Event(sale2));  // 200.0f
        stream.publish(new Event(sale3));  // 300.0f
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(100.0f, consumer.minValue(), 0.01f,
            "Deve encontrar o menor valor entre as vendas");
    }

    @Test
    void shouldCalculateAverageValue() {
        // Publicar eventos no stream
        stream.publish(new Event(sale1));  // 100.0f
        stream.publish(new Event(sale2));  // 200.0f
        stream.publish(new Event(sale3));  // 300.0f
        
        // Consumir eventos
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        consumer.consumeStream(stream);
        
        assertEquals(200.0f, consumer.avgValue(), 0.01f,
            "Deve calcular corretamente a média dos valores");
    }
}
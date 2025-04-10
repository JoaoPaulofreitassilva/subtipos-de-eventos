import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.lpm.business.Ticket;

public class TicketTest {
  private Ticket ticket;

    @BeforeEach
    public void setup() throws Exception {
        ticket = new Ticket("Carnaval", 100f, LocalDate.of(2026, 02, 12), "Praça da liberdade");
    }

    @Test
    void testSetEvent() {
        // TDD básico
        ticket.setEvent("Carnaval");
        assertEquals("Carnaval", ticket.getEvent(), "Nome válido.");
        // TDD com erros
        assertThrows(IllegalArgumentException.class, () -> ticket.setEvent(null), 
                 "Nome nulo");
        assertThrows(IllegalArgumentException.class, () -> ticket.setEvent("@#$"), 
                 "Nome inválido");
    }

    @Test
    void testSetValue() {
        // TDD básico
        ticket.setValue(20f);
        assertEquals(20f, ticket.getValue(), 0.01f, "Valor válido.");
        // TDD com erros
        assertThrows(IllegalArgumentException.class, () -> ticket.setValue(0f),
                "Valor Inválido: o preço nao pode ser igual 0.");
        assertThrows(IllegalArgumentException.class, () -> ticket.setValue(-100f),
                "Valor Inválido: o preço nao pode ser menor que 0.");
    }

    @Test
    void testDate() {
        // TDD básico
        ticket.setDate(LocalDate.now().plusDays(1));
        assertEquals(LocalDate.now().plusDays(1), ticket.getDate(), "Data Válida.");
        // TDD com erros
        assertThrows(IllegalArgumentException.class, () -> ticket.setDate(LocalDate.now().minusDays(1)), "A data do evento não pode ser no passado");
        assertThrows(IllegalArgumentException.class, () -> ticket.setDate(LocalDate.now()), "A data do evento de ser no futuro");
    }

    @Test
    void testSetAddress() {
        // TDD basico
        ticket.setAddress("Praça da Liberdade");
        assertEquals("Praça da Liberdade", ticket.getAddress());
        // TDD com erros
        assertThrows(IllegalArgumentException.class, () -> ticket.setAddress(null), 
                 "Local nulo");
        assertThrows(IllegalArgumentException.class, () -> ticket.setAddress("@#$%"), 
                 "Local inválido");
    }   

}

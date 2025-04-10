package br.lpm.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SaleTest {
    private Sale sale;

    @BeforeEach
    public void setup() throws Exception {
        sale = new Sale("Venda Especial", 100f, LocalDate.of(2026, 02, 12), "Vendedor Silva");
    }

    @Test
    void testSetDescription() {
        // TDD básico
        sale.setDescription("Venda Promocional");
        assertEquals("Venda Promocional", sale.getDescription(), "Descrição válida.");
        // TDD com erros
        assertThrows(IllegalArgumentException.class, () -> sale.setDescription(null), 
                 "Descrição nula");
        assertThrows(IllegalArgumentException.class, () -> sale.setDescription("Venda123"), 
                 "Descrição inválida");
    }

    @Test
    void testSetValue() {
        // TDD básico
        sale.setValue(20f);
        assertEquals(20f, sale.getValue(), 0.01f, "Valor válido.");
        // TDD com erros
        assertThrows(IllegalArgumentException.class, () -> sale.setValue(0f),
                "Valor Inválido: o valor não pode ser igual 0.");
        assertThrows(IllegalArgumentException.class, () -> sale.setValue(-100f),
                "Valor Inválido: o valor não pode ser menor que 0.");
    }

    @Test
    void testDate() {
        // TDD básico
        sale.setDate(LocalDate.now().plusDays(1));
        assertEquals(LocalDate.now().plusDays(1), sale.getDate(), "Data Válida.");
        // TDD com erros
        assertThrows(IllegalArgumentException.class, () -> sale.setDate(LocalDate.now().minusDays(1)), 
                "A data da venda não pode ser no passado");
        assertThrows(IllegalArgumentException.class, () -> sale.setDate(LocalDate.now()), 
                "A data da venda deve ser no futuro");
    }

    @Test
    void testSetOrigin() {
        // TDD basico
        sale.setOrigin("Vendedor Santos");
        assertEquals("Vendedor Santos", sale.getOrigin());
        // TDD com erros
        assertThrows(IllegalArgumentException.class, () -> sale.setOrigin(null), 
                 "Origem nula");
        assertThrows(IllegalArgumentException.class, () -> sale.setOrigin("Vendedor123"), 
                 "Origem inválida");
    }
}
package br.lpm.main;

import java.time.LocalDate;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import br.lpm.business.Stream;
import br.lpm.business.Ticket;
import br.lpm.business.EventConsumer;

public class Main {
    public static Ticket[] tickets = new Ticket[6];
    public static Scanner scanner = new Scanner(System.in);
    public static Stream stream = new Stream();
    public static EventConsumer consumer = new EventConsumer();

    public static void main(String[] args) {
        inicializarDados();
        
        int opcao;
        do {
            exibirMenu();
            opcao = scanner.nextInt();
            processarOpcao(opcao);
        } while (opcao != 0);
        
        scanner.close();
    }

    private static void inicializarDados() {
        tickets[0] = new Ticket("Carnaval", 100f, LocalDate.of(2025, 12, 28), "Praça da liberdade");
        tickets[1] = new Ticket("Páscoa", 20f, LocalDate.of(2025, 3, 28), "BH Shopping");
        tickets[2] = new Ticket("Natal", 50f, LocalDate.of(2025, 12, 25), "Praça da Estação");
        tickets[3] = new Ticket("Carnaval", 120f, LocalDate.of(2025, 12, 28), "Praça da liberdade");
        tickets[4] = new Ticket("Ano Novo", 200f, LocalDate.of(2025, 12, 31), "Copacabana");
        tickets[5] = new Ticket("Aniversario", 200f, LocalDate.of(2025, 12, 31), "Copacabana");

    }

    private static void exibirMenu() {
        System.out.println("\n=== MENU DE TESTES ===");
        System.out.println("1. Publicar todos os eventos no Stream");
        System.out.println("2. Visualizar primeiro evento da fila");
        System.out.println("3. Consumir um evento");
        System.out.println("4. Mostrar tamanho atual do Stream");
        System.out.println("5. Remover todos os eventos do Stream");
        System.out.println("6. Consumir evento para o EventConsumer");
        System.out.println("7. Mostrar estatísticas dos eventos consumidos");
        System.out.println("8. Mostrar todos os eventos cadastrados");
        System.out.println("9. Exibir histograma de eventos");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void processarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 1:
                    publicarEventos();
                    break;
                case 2:
                    visualizarPrimeiroEvento();
                    break;
                case 3:
                    consumirEvento();
                    break;
                case 4:
                    mostrarTamanho();
                    break;
                case 5:
                    removerTodos();
                    break;
                case 6:
                    consumirParaEventConsumer();
                    break;
                case 7:
                    mostrarEstatisticas();
                    break;
                case 8:
                    mostrarEventosCadastrados();
                    break;
                case 9:
                    eventsHistogram();
                    break;
                case 0:
                    System.out.println("Encerrando programa...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void eventsHistogram() {
        if (stream.size() == 0) {
            System.out.println("O Stream está vazio.");
            return;
        }

        // Arrays para armazenar dados temporários
        Ticket[] tempTickets = new Ticket[Stream.getMaxEvents()];
        String[] eventNames = new String[Stream.getMaxEvents()];
        int[] eventCounts = new int[Stream.getMaxEvents()];
        int uniqueEvents = 0;
        int index = 0;

        // Consumir e contar eventos
        int originalSize = stream.size();
        for (int i = 0; i < originalSize; i++) {
            Ticket ticket = stream.consume();
            tempTickets[index++] = ticket;

            // Verificar se o evento já existe na contagem
            boolean found = false;
            for (int j = 0; j < uniqueEvents; j++) {
                if (eventNames[j].equals(ticket.getEvent())) {
                    eventCounts[j]++;
                    found = true;
                    break;
                }
            }

            // Se não encontrou, adicionar novo evento
            if (!found) {
                eventNames[uniqueEvents] = ticket.getEvent();
                eventCounts[uniqueEvents] = 1;
                uniqueEvents++;
            }
        }

        // Restaurar eventos no stream
        for (int i = 0; i < index; i++) {
            stream.publish(tempTickets[i]);
        }

        // Criar dataset para o gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < uniqueEvents; i++) {
            dataset.addValue(eventCounts[i], "Eventos", eventNames[i]);
        }

        // Criar o gráfico
        JFreeChart chart = ChartFactory.createBarChart(
            "Distribuição de Eventos no Stream",  // título
            "Eventos",                            // eixo x
            "Quantidade",                         // eixo y
            dataset
        );

        // Exibir o gráfico em uma janela
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Histograma de Eventos");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // Exibir contagem no console
        System.out.println("\n=== Distribuição de Eventos ===");
        for (int i = 0; i < uniqueEvents; i++) {
            System.out.println(eventNames[i] + ": " + eventCounts[i]);
        }
    }

    private static void publicarEventos() {
        System.out.println("\n=== Publicando Eventos ===");
        for (Ticket ticket : tickets) {
            if (ticket != null) {
                stream.publish(ticket);
                System.out.println("Evento publicado: " + ticket.getEvent());
            }
        }
    }

    private static void visualizarPrimeiroEvento() {
        Ticket primeiro = stream.get();
        System.out.println("\nPrimeiro evento na fila: " + primeiro.getEvent());
    }

    private static void consumirEvento() {
        Ticket consumido = stream.consume();
        System.out.println("\nEvento consumido: " + consumido.getEvent());
    }

    private static void mostrarTamanho() {
        System.out.println("\nTamanho atual do Stream: " + stream.size());
    }

    private static void removerTodos() {
        stream.removeAll();
        System.out.println("\nTodos os eventos foram removidos do Stream");
    }

    private static void consumirParaEventConsumer() {
        consumer.consumeStream(stream);
        System.out.println("\nEvento consumido pelo EventConsumer");
    }

    private static void mostrarEstatisticas() {
        System.out.println("\n=== Estatísticas dos Eventos Consumidos ===");
        System.out.println("Valor máximo: R$ " + consumer.maxValue());
        System.out.println("Valor mínimo: R$ " + consumer.minValue());
        System.out.println("Valor médio: R$ " + String.format("%.2f", consumer.avgValue()));
        System.out.println("Evento mais frequente: " + consumer.modeEvent());
        System.out.println("Porcentagem de Carnaval: " + 
            String.format("%.1f%%", consumer.percentEvent("Carnaval")));
    }

    private static void mostrarEventosCadastrados() {
        System.out.println("\n=== Eventos Cadastrados ===");
        for (int i = 0; i < tickets.length; i++) {
            if (tickets[i] != null) {
                System.out.println("\nEvento " + (i + 1));
                System.out.println("Evento: " + tickets[i].getEvent());
                System.out.println("Preço: R$ " + tickets[i].getValue());
                System.out.println("Data: " + tickets[i].getDate());
                System.out.println("Local: " + tickets[i].getAddress());
                System.out.println("-------------------------------");
            }
        }
    }
}
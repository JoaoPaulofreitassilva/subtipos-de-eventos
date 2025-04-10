package br.lpm.business;

import java.time.LocalDate;

public class Ticket implements Content {
    private String event;
    private float value;
    private LocalDate date;
    private String address;

    public Ticket(String event, float value, LocalDate date, String address) {
        setEvent(event);
        setValue(value);
        setDate(date);
        setAddress(address);
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        if (event == null || !event.matches("^[a-zA-Z0-9áéíóúàâêãõçü ]+$")) {
            throw new IllegalArgumentException("O nome do evento inválido.");
        } else {
            this.event = event;
        }

    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        if (value <= 0) {
            throw new IllegalArgumentException("O valor não pode ser menor ou igual a 0");
        } else {
            this.value = value;
        }

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Data nula para o evento.");
        } else if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())) {
            throw new IllegalArgumentException("A data deve ser no futuro.");
        } else {
            this.date = date;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || !address.matches("^[a-zA-Z0-9áéíóúàâêãõçü ]+$")) {
            throw new IllegalArgumentException("Local do evento inválido.");
        } else {
            this.address = address;
        }
    }
}

package br.lpm.business;

import java.time.LocalDate;

public class Sale implements Content {
    private String description;
    private float value;
    private LocalDate date;
    private String origin;

    public Sale(String description, float value, LocalDate date, String origin) {
        setDescription(description);
        setValue(value);
        setDate(date);
        setOrigin(origin);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || !description.matches("^[a-zA-Z ]+$")) {
            throw new IllegalArgumentException("Descrição inválida.");
        }
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        if (value <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null || !date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data deve ser futura.");
        }
        this.date = date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        if (origin == null || !origin.matches("^[a-zA-Z ]+$")) {
            throw new IllegalArgumentException("Origem inválida.");
        }
        this.origin = origin;
    }
}

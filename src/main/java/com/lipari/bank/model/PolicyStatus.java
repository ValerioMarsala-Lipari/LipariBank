package com.lipari.bank.model;

public enum PolicyStatus {
    ACTIVE("Attivo"),
    EXPIRED("Scaduto"),
    SUSPENDED("Sospeso"),
    CANCELLED("Cancellato");

    private final String label;

    PolicyStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
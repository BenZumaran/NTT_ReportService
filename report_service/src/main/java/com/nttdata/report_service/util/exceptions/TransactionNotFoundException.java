package com.nttdata.report_service.util.exceptions;

public class TransactionNotFoundException extends RuntimeException {

    private String id = "";

    public TransactionNotFoundException(String id) {
        super("Transaction with ID " + id + " not found.");
        this.id = id;
    }

    public TransactionNotFoundException() {
        super("No transactions found for the given criteria.");
    }

    public String getUserId() {
        return id;
    }
}

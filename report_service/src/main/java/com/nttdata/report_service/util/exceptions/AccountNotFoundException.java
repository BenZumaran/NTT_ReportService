package com.nttdata.report_service.util.exceptions;

public class AccountNotFoundException extends RuntimeException {

    private String id = "";

    public AccountNotFoundException(String id) {
        super("Account with ID " + id + " not found.");
        this.id = id;
    }

    public AccountNotFoundException() {
        super("Not found accounts for current request.");
    }

    public String getUserId() {
        return id;
    }
}

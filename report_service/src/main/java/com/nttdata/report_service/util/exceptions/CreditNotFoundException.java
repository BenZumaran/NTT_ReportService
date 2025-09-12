package com.nttdata.report_service.util.exceptions;

public class CreditNotFoundException extends RuntimeException {

    private String id = "";

    public CreditNotFoundException(String id) {
        super("Credit with ID " + id + " not found.");
        this.id = id;
    }

    public CreditNotFoundException() {
        super("Not found credits for current request.");
    }

    public String getUserId() {
        return id;
    }
}

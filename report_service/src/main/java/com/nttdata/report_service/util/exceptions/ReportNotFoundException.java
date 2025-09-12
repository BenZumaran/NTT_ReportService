package com.nttdata.report_service.util.exceptions;

public class ReportNotFoundException extends RuntimeException {

    public ReportNotFoundException() {
        super("No report found for the given criteria.");
    }

}

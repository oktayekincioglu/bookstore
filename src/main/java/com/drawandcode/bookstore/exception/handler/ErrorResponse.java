package com.drawandcode.bookstore.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int status;
    private final String message;
    private List<String> validationErrors;

    public ErrorResponse(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(final int status, final String message, List<String> validationErrors) {
        this.status = status;
        this.message = message;
        this.validationErrors = validationErrors;
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}

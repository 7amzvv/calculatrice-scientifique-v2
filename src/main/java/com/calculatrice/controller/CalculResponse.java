package com.calculatrice.controller;

public class CalculResponse {
    private Double result;
    private String error;

    public CalculResponse(Double result, String error) {
        this.result = result;
        this.error = error;
    }

    public Double getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
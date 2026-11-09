package com.calculatrice.controller;

public class CalculRequest {
    private String expression;

    public CalculRequest() {}

    public CalculRequest(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
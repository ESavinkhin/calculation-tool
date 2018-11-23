package ru.sberbank.tool;

public class CalculatorException extends RuntimeException {
    private final CalculatorErrors error;

    public CalculatorException(CalculatorErrors error, String message) {
        super(String.format("Error code [%s] message --- %s ", error.getCode(), message));
        this.error = error;
    }

    public CalculatorErrors getError() {
        return error;
    }
}

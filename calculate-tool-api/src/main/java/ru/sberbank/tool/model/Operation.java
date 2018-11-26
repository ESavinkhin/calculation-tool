package ru.sberbank.tool.model;

import ru.sberbank.tool.CalculatorErrors;

import java.util.Arrays;

/**
 * Created by Egor Savinkhin on 22/11/2018.
 * Property of Sberbank
 */
public enum Operation {
    SALE("s"), BUY("b");

    private String code;

    Operation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Operation invert() {
        return this == SALE ? BUY : SALE;
    }

    public static Operation getByCode(String code) {
        return Arrays.stream(values()).filter(t -> t.getCode().equals(code)).findFirst().orElseThrow(() -> CalculatorErrors.NOT_FOUND_OPERATION.error(code));
    }


}

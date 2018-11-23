package ru.sberbank.tool.model;

import ru.sberbank.tool.CalculatorErrors;

import java.util.Arrays;

/**
 * Created by Egor Savinkhin on 22/11/2018.
 * Property of Sberbank
 */
public enum StockType {
    A("A"), B("B"), C("C"), D("D");
    private String code;

    StockType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static StockType getByCode(String code) {
        return Arrays.stream(values()).filter(t->t.getCode().equals(code)).findFirst().orElseThrow(()-> CalculatorErrors.NOT_FOUND_STOCK.error(code));
    }
}

package ru.sberbank.tool;

import java.util.MissingFormatArgumentException;

/**
 * Список кодов ошибок
 */
public enum CalculatorErrors {
    // не найденна стратегия для мержа
    NOT_FOUND_STRATEGY("JTE-01", "message1"),
    //Ошибка
    MERGE_ERROR("JTE-02", "message2 %s  details [%s]"),
    // comment
    NOT_FOUND_STOCK("JTE-03", "not found stock %s"),
    // comment
    NOT_FOUND_OPERATION("JTE-04", "not found operation %s"),
    //comment
    NOT_IMPLEMENT("JTE-00", "Not implement")
    ;

    private String message;
    private String code;

    CalculatorErrors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CalculatorException error(String... args) {
        try {
            return args.length == 0 ? new CalculatorException(this, message) : new CalculatorException(this, String.format(message, (Object[]) args));
        } catch (MissingFormatArgumentException e) {
            e.printStackTrace();
            return  new CalculatorException(this, message);
        }
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}

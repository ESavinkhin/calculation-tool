package ru.sberbank.tool.services;

import ru.sberbank.tool.CalculatorErrors;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Order;

import java.util.List;
import java.util.stream.Stream;

/**
 * Реализация стратегии с исползованием imbedded h2 db, для слияний больших файлов
 */
public class H2MergeStrategy implements MergeStrategy {

    @Override
    public List<Client> merge(List<Client> clientStream, List<Order> orderStream) {
        throw CalculatorErrors.NOT_IMPLEMENT.error();
    }

    @Override
    public boolean isApplicable(long sizeClient, long sizeProduct) {
        throw CalculatorErrors.NOT_IMPLEMENT.error();
    }
}

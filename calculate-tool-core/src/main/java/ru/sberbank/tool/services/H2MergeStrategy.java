package ru.sberbank.tool.services;

import ru.sberbank.tool.CalculatorErrors;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Order;

import java.util.stream.Stream;

/**
 * Реализация стратегии с исползованием imbedded h2 db, для слияний больших файлов
 */
public class H2MergeStrategy implements MergeStrategy {

    @Override
    public Stream<Client> merge(Stream<Client> clientStream, Stream<Order> orderStream) {
        throw CalculatorErrors.NOT_IMPLEMENT.error();
    }

    @Override
    public boolean isApplicable(long sizeClient, long sizeProduct) {
        throw CalculatorErrors.NOT_IMPLEMENT.error();
    }
}

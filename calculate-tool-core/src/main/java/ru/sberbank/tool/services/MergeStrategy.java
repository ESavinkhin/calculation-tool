package ru.sberbank.tool.services;

import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Order;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Интерфес для реализации стартегий мержа клинетов и продуктов
 */
public interface MergeStrategy {
    /**
     * Соеденения клиентов с заказами
     * @param clientStream клиенты
     * @param orderStream заказы
     * @return stream клиентов
     */
    List<Client> merge(List<Client> clientStream, List<Order> orderStream);

    /**
     * Проверка применимости стратегии
     * в класическом  виде возврашаеся вес стратегии, затем из списка выбиратеся с наименьшем весом
     * @param sizeClient - размер  списка клиетнтов в килобайтх
     * @param sizeProduct - размер списка продуктов в килобайтх
     * @return  да/нет
     */
    boolean isApplicable(long sizeClient, long sizeProduct);
}

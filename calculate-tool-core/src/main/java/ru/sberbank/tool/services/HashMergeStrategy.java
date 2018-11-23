package ru.sberbank.tool.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Класс реализует стратегию hash join
 */
public class HashMergeStrategy implements MergeStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashMergeStrategy.class);
    //Максимальной пороговый рзамер списка заказов для примененеия hash join, в килобайтах
    private static final int MAX_THRESHOLD_SIZE_CLIENT = 1000;


    @Override
    public Stream<Client> merge(Stream<Client> clientStream, Stream<Order> orderStream) {
        LOGGER.debug("Use hash merge");
        Objects.requireNonNull(clientStream, "Client iterator is null");
        Objects.requireNonNull(orderStream, "Order iterator is null");
        Map<String, List<Order>> map = new HashMap<>();
        orderStream.forEach(order -> {
            map.putIfAbsent(order.getClientId(), new ArrayList<>());
            map.get(order.getClientId()).add(order);
        });
        return clientStream.peek(c -> c.getOrders().addAll(map.getOrDefault(c.getId(), Collections.emptyList())));
    }

    @Override
    public boolean isApplicable(long sizeClient, long sizeProduct) {
        return sizeProduct < MAX_THRESHOLD_SIZE_CLIENT;
    }
}

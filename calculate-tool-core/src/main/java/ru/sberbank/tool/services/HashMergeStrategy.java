package ru.sberbank.tool.services;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Operation;
import ru.sberbank.tool.model.Order;
import ru.sberbank.tool.model.StockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс реализует стратегию hash join
 */
public class HashMergeStrategy implements MergeStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashMergeStrategy.class);
    //Максимальной пороговый рзамер списка заказов для примененеия hash join, в килобайтах
    private static final int MAX_THRESHOLD_SIZE_CLIENT = 1000;

    @Override
    public List<Client> merge(List<Client> clients, List<Order> orders) {
        LOGGER.debug("Use hash merge");
        Objects.requireNonNull(clients, "Client iterator is null");
        Objects.requireNonNull(orders, "Order iterator is null");

        Map<String, Client> mapClients = clients.stream().collect(Collectors.toMap(Client::getId, Function.identity()));

        List<Order> orderForClient = orders.stream().filter(order -> mapClients.containsKey(order.getClientId())).collect(Collectors.toList());

        Map<Key, List<Order>> mapOrders = Maps.newHashMapWithExpectedSize(orderForClient.size());

        orderForClient.forEach(order -> {
            Key key = new Key(order.getStockType(), order.getOperation(), order.getPrice(), order.getCount());
            mapOrders.putIfAbsent(key, new ArrayList<>());
            mapOrders.get(key).add(order);

        });



        orderForClient.forEach(orderLeft -> {
            Key keyLeft =  new Key(
                    orderLeft.getStockType(),
                    orderLeft.getOperation(),
                    orderLeft.getPrice(),
                    orderLeft.getCount());

            Key keyRight = new Key(
                    orderLeft.getStockType(),
                    orderLeft.getOperation().invert(),
                    orderLeft.getPrice(),
                    orderLeft.getCount());

            List<Order> ordersRight = mapOrders.get(keyRight);
            List<Order> ordersLeft = mapOrders.get(keyLeft);
            if (Objects.nonNull(ordersRight) && ordersRight.size() > 0 && Objects.nonNull(ordersLeft) && ordersLeft.size() > 0) {
                Order orderRight = ordersRight.get(0);
                mapClients.get(orderRight.getClientId()).getOrders().add(orderRight);
                mapClients.get(orderLeft.getClientId()).getOrders().add(orderLeft);
                ordersLeft.remove(orderLeft);
                ordersRight.remove(0);
            }
        });
        return new ArrayList<>(clients);
    }

    @Override
    public boolean isApplicable(long sizeClient, long sizeProduct) {
        return sizeProduct < MAX_THRESHOLD_SIZE_CLIENT;
    }

    private static class Key {
        private final StockType stockType;
        private final Operation operation;
        private final Integer price;
        private final Integer count;

        Key(StockType stockType, Operation operation, Integer price, Integer count) {
            this.stockType = stockType;
            this.operation = operation;
            this.price = price;
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return stockType == key.stockType &&
                    operation == key.operation &&
                    Objects.equals(price, key.price) &&
                    Objects.equals(count, key.count);
        }

        @Override
        public int hashCode() {

            return Objects.hash(stockType, operation, price, count);
        }
    }
}

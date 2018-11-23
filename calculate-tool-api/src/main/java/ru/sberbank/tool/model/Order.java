package ru.sberbank.tool.model;

import java.util.Objects;

/**
 * Контейнер для заказа
 */
public class Order {
    private final String clientId;
    private final Operation operation;
    private final StockType stockType;
    private final int count;
    private final int price; //TODO: если будет несколько валют использовать паттерн Money

    public Order(String clientId, Operation operation, StockType stockType, int count, int price) {
        this.clientId = clientId;
        this.operation = operation;
        this.stockType = stockType;
        this.count = count;
        this.price = price;
    }

    public String getClientId() {
        return clientId;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getPrice() {
        return price;
    }

    public StockType getStockType() {
        return stockType;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return count == order.count &&
                price == order.price &&
                Objects.equals(clientId, order.clientId) &&
                operation == order.operation &&
                stockType == order.stockType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientId, operation, stockType, count, price);
    }

    @Override
    public String toString() {
        return "Order{" +
                "clientId='" + clientId + '\'' +
                ", operation=" + operation +
                ", stockType=" + stockType +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}

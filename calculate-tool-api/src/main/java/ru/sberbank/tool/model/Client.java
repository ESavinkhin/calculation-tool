package ru.sberbank.tool.model;

import java.util.*;

/**
 * Обьект клиента
 * TODO: User lombok
 */
public class Client {
    private final String id;
    private final int budget; //TODO: use pattern money
    private final Map<StockType, Integer> stocks;
    private final List<Order> orders = new ArrayList<>();


    public Client(String id, Integer budget, Map<StockType, Integer> stocks) {
        this.id = id;
        this.budget = budget;
        this.stocks = Collections.synchronizedMap(stocks);
    }

    public String getId() {
        return id;
    }

    public Map<StockType, Integer> getStocks() {
        return stocks;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getBudget() {
        return budget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return budget == client.budget &&
                Objects.equals(id, client.id) &&
                Objects.equals(stocks, client.stocks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, budget, stocks);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", budget=" + budget +
                ", stocks=" + stocks +
                '}';
    }
}

package ru.sberbank.tool.services;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Operation;
import ru.sberbank.tool.model.Order;
import ru.sberbank.tool.model.StockType;

import java.util.Objects;

/**
 * Created by Egor Savinkhin on 22/11/2018.
 * Property of Sberbank
 * Контейнер для утилитных методов
 */
final class Fixtures {
    private Fixtures() {
    }

    /**
     * Конвертация byte to kbyte
     *
     * @param b - byte
     * @return kbyte
     */
    static long byteToKbyte(long b) {
        return (long)Math.ceil((float)b / 1024);
    }

    /**
     * Создать клиента из (csv) строки
     *
     * @param line строка
     * @return клинет
     */
    static Client parseClient(String line) {
        Objects.requireNonNull(line, "Line is null");
        String[] items = line.split("\t");
        return new Client(
                items[0],
                Integer.parseInt(items[1]),
                Maps.immutableEnumMap(ImmutableMap.of(
                        StockType.A, Integer.parseInt(items[2]),
                        StockType.B, Integer.parseInt(items[3]),
                        StockType.C, Integer.parseInt(items[4]),
                        StockType.D, Integer.parseInt(items[5])
                )));
    }

    /**
     * Создать заказ из (csv) строки
     *
     * @param line строка
     * @return заказ
     */
    static Order parseOrder(String line) {
        Objects.requireNonNull(line, "Line is null");
        String[] items = line.split("\t");
        return new Order(
                items[0],
                Operation.getByCode(items[1]),
                StockType.getByCode(items[2]),
                Integer.parseInt(items[3]),
                Integer.parseInt(items[4])
        );
    }

    static String clientToString(Client client) {
        Objects.requireNonNull(client, "client is null");
        //last java support convert string concatenation to string builder
        return  client.getId() + "\t" +
                client.getBudget() + "\t" +
                client.getStocks().get(StockType.A) + "\t" +
                client.getStocks().get(StockType.B) +"\t" +
                client.getStocks().get(StockType.C) +"\t" +
                client.getStocks().get(StockType.D);
    }

}

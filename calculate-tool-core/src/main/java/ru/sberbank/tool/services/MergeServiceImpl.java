package ru.sberbank.tool.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sberbank.tool.CalculatorErrors;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Operation;
import ru.sberbank.tool.model.Order;
import ru.sberbank.tool.model.StockType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Egor Savinkhin on 22/11/2018.
 * Property of Sberbank
 * Реализация сервиса
 */
public class MergeServiceImpl implements CalculateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MergeServiceImpl.class);

    /**
     * Список стратегий
     */
    private final List<MergeStrategy> mergeStrategies = Arrays.asList(
            new HashMergeStrategy(),
            new FastMergeStrategy(),
            new H2MergeStrategy()
    );

    @Override
    public void calculateClient(File clientFile, File orderFile, File outputFile) {
        Objects.requireNonNull(clientFile, "Client file name is null");
        Objects.requireNonNull(orderFile, "Order file name  is null");
        Objects.requireNonNull(outputFile, "Output file name is null");
        LOGGER.debug("Try to merge file clientFile {} order {} output {}", clientFile.getName(), orderFile.getName(), outputFile.getName());
        checkExists(clientFile);
        checkExists(orderFile);
        checkExists(outputFile);
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath())) {
            List<Client> clients = mergeStrategies.stream()
                    .filter(s -> s.isApplicable(Fixtures.byteToKbyte(clientFile.length()), Fixtures.byteToKbyte(orderFile.length())))
                    .findFirst()
                    .orElseThrow(CalculatorErrors.NOT_FOUND_STRATEGY::error)
                    .merge(
                            Files.lines(clientFile.toPath()).map(Fixtures::parseClient).collect(Collectors.toList()),
                            Files.lines(orderFile.toPath()).map(Fixtures::parseOrder).collect(Collectors.toList())
                    );

            Iterator<Client> iter = clients.stream().map(MergeServiceImpl::calculate).iterator();

            while (iter.hasNext()) {
                writer.write(Fixtures.clientToString(iter.next()));
                writer.newLine();
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw CalculatorErrors.CALCULATOR_ERROR.error(e.getMessage(), e.toString());
        }
    }

    /**
     * Проверить существование файла
     * @param file - file
     */
    void checkExists(File file) {
        if (!file.exists()) {
            throw CalculatorErrors.NOT_EXISTS_FILE.error(file.getAbsolutePath());
        }
    }

    /**
     * Расчитать итоговое состояние клиента после обработки заявок
     *
     * @param client - клинет
     * @return клинет с новым состоянием
     */
    static Client calculate(Client client) {
        EnumMap<StockType, Pair> map = new EnumMap<>(StockType.class);
        client.getStocks().keySet().forEach(t -> map.putIfAbsent(t, new Pair()));
        //TODO: необходимо анализровать превыщение бюджета

        for (Order o : client.getOrders()) {
            Pair p = map.get(o.getStockType());
            if (o.getOperation() == Operation.BUY) {
                p.count += o.getCount();
                p.price -= o.getPrice() * o.getCount();
            } else {
                p.count -= o.getCount();
                p.price += o.getPrice() * o.getCount();
            }
        }

        Map<StockType, Integer> updatedStocks = new HashMap<>();
        for (Map.Entry<StockType, Integer> en : client.getStocks().entrySet()) {
            // вычисляем новое кол-во акций
            updatedStocks.putIfAbsent(en.getKey(), en.getValue() + map.get(en.getKey()).count);
        }

        int updatedBudget = client.getBudget()
                + map.values().stream().mapToInt(p -> p.price).sum();

        return new Client(
                client.getId(),
                updatedBudget,
                updatedStocks
        );
    }

    private static class Pair {
        int price = 0;
        int count = 0;

    }
}

package ru.sberbank.tool.services;

import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sberbank.tool.CalculatorErrors;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Operation;
import ru.sberbank.tool.model.StockType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public File calculateClient(File clientFile, File orderFile) {
        Objects.requireNonNull(clientFile, "Client file is null");
        Objects.requireNonNull(orderFile, "Order file is null");
        LOGGER.debug("Try to merge file clientFile {} order {}", clientFile.getName(), orderFile.getName());

        File outputFile = createOutputFile();
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath())) {

            Iterator<Client> iter = mergeStrategies.stream()
                    .filter(s -> s.isApplicable(Fixtures.byteToKbyte(clientFile.length()), Fixtures.byteToKbyte(orderFile.length())))
                    .findFirst()
                    .orElseThrow(CalculatorErrors.NOT_FOUND_STRATEGY::error)
                    .merge(
                            Files.lines(clientFile.toPath()).map(Fixtures::parseClient),
                            Files.lines(orderFile.toPath()).map(Fixtures::parseOrder)
                    ).map(MergeServiceImpl::calculate).iterator();

            while (iter.hasNext()) {
                writer.write(Fixtures.clientToString(iter.next()));
                writer.newLine();
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw CalculatorErrors.MERGE_ERROR.error(e.getMessage(), e.toString());
        }
        return outputFile;
    }

    private File createOutputFile() {
        try {
            return File.createTempFile("output", "result");
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw CalculatorErrors.MERGE_ERROR.error(e.getMessage());
        }
    }

    /**
     * Расчитать итоговое состояние клиента после обработки заявок
     *
     * @param client - клинет
     * @return клинет с новым состоянием
     */
    static Client calculate(Client client) {
        EnumMap<StockType, MutablePair<Integer, Integer>> map = new EnumMap<>(StockType.class);
        client.getStocks().keySet().forEach(t -> map.putIfAbsent(t, new MutablePair<Integer, Integer>(0, 0)));
        //TODO: необходимо анализровать превыщение бюджета
        /*
         * p.left - количество акций, по группам StockType
         * p.right - обьщая цена, по группам StockType
         */
        client.getOrders().forEach(o -> {
            MutablePair<Integer, Integer> p = map.get(o.getStockType());
            if (o.getOperation() == Operation.BUY) {
                p.left += o.getCount();
                p.right -= o.getPrice() * o.getCount();
            } else {
                p.left -= o.getCount();
                p.right += o.getPrice() * o.getCount();
            }

        });
        Map<StockType, Integer> updatedStocks = client.getStocks().entrySet().stream()
                .map(es -> new AbstractMap.SimpleEntry<>(
                        es.getKey(),
                        es.getValue() + map.get(es.getKey()).left)// вычисляем новое кол-во акций
                )
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));


        int updatedBudget = client.getBudget()
                + map.values().stream().mapToInt(p -> p.right).sum();

        return new Client(
                client.getId(),
                updatedBudget,
                updatedStocks
        );
    }

    /*private static class Pair {
        int price = 0;
        int count = 0;

    }*/
}

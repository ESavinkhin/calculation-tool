package ru.sberbank.tool.services;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Operation;
import ru.sberbank.tool.model.Order;
import ru.sberbank.tool.model.StockType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HashMergeStrategyTest {

    @Test
    public void merge() {
        List<Client> clientStream = Arrays.asList(
                new Client("c1", 100, Collections.emptyMap()),
                new Client("c2", 100, Collections.emptyMap()),
                new Client("c3", 100, Collections.emptyMap()),
                new Client("c4", 100, Collections.emptyMap())
        );

        List<Order> orderStream = Arrays.asList(
                new Order("c1", Operation.BUY, StockType.A, 1, 1),
                new Order("c1", Operation.BUY, StockType.B, 1, 2),
                new Order("c1", Operation.BUY, StockType.C, 1, 3),
                new Order("c1", Operation.BUY, StockType.C, 1, 4),

                new Order("c2", Operation.SALE, StockType.A, 1, 1),

                new Order("c4", Operation.SALE, StockType.C, 1, 3),
                new Order("c4", Operation.SALE, StockType.C, 1, 4),

                new Order("not_exists", Operation.BUY, StockType.B, 1, 7)
        );
        List<Client> result = new HashMergeStrategy().merge(clientStream, orderStream);
        Assert.assertNotNull(result);
        Map<String, Client> clients = result.stream().collect(Collectors.toMap(Client::getId, Function.identity()));
        Assert.assertEquals(4, clients.size());
        Assert.assertEquals(3, clients.get("c1").getOrders().size());
        Assert.assertEquals(1, clients.get("c2").getOrders().size());
        Assert.assertEquals(0, clients.get("c3").getOrders().size());
        Assert.assertEquals(2, clients.get("c4").getOrders().size());
        Assert.assertEquals(3, clients.get("c1").getOrders().stream().filter(o->o.getOperation() == Operation.BUY).count());
        Assert.assertEquals(2, clients.get("c4").getOrders().stream().filter(o->o.getOperation() == Operation.SALE).count());
        Assert.assertEquals(7, clients.get("c4").getOrders().stream().mapToInt(Order::getPrice).sum());
    }

    @Test
    public void isApplicable() {
        Assert.assertTrue(new HashMergeStrategy().isApplicable(19, 10));
        Assert.assertFalse(new HashMergeStrategy().isApplicable(19, 1000));
    }
}
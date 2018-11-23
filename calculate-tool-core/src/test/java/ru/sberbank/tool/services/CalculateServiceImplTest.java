package ru.sberbank.tool.services;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Operation;
import ru.sberbank.tool.model.Order;
import ru.sberbank.tool.model.StockType;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class CalculateServiceImplTest {
    @Test
    public void calculate() {
        Client client = new Client("client1", 10, Maps.immutableEnumMap(ImmutableMap.of(
                StockType.A, 1,
                StockType.B, 2,
                StockType.C, 3,
                StockType.D, 4
        )));

        client.getOrders().add(new Order("client1", Operation.BUY, StockType.A, 10, 2));
        client.getOrders().add(new Order("client1", Operation.SALE, StockType.A, 10, 2));
        client.getOrders().add(new Order("client1", Operation.BUY, StockType.B, 3, 3));
        client = MergeServiceImpl.calculate(client);
        Assert.assertEquals(1, client.getBudget());
        Assert.assertEquals(1, client.getStocks().get(StockType.A).intValue());
        Assert.assertEquals(5, client.getStocks().get(StockType.B).intValue());
    }

    @Test
    public void mergeClient() throws Exception {
        File output = new MergeServiceImpl().calculateClient(
                new File(this.getClass().getResource("clients.txt").toURI()),
                new File(this.getClass().getResource("orders.txt").toURI())
        );

        Assert.assertTrue(output.isFile());

        List<String> list =  Files.readAllLines(output.toPath());
        System.out.println(Joiner.on("\n").join(list));
        Assert.assertEquals(9, list.size());
        Client c1 = Fixtures.parseClient(list.get(0));
        Assert.assertEquals("C1", c1.getId());
        Assert.assertEquals(11516, c1.getBudget());
        Assert.assertEquals(7, c1.getStocks().get(StockType.A).intValue());
        Assert.assertEquals(118, c1.getStocks().get(StockType.B).intValue());
        Assert.assertEquals(-2339 , c1.getStocks().get(StockType.C).intValue());
        Assert.assertEquals(170 , c1.getStocks().get(StockType.D).intValue());


        Client c2 = Fixtures.parseClient(list.get(1));
        Assert.assertEquals("C2", c2.getId());
        Assert.assertEquals(6855, c2.getBudget());
        Assert.assertEquals(651, c2.getStocks().get(StockType.A).intValue());
        Assert.assertEquals(1356, c2.getStocks().get(StockType.B).intValue());
        Assert.assertEquals(-1741 , c2.getStocks().get(StockType.C).intValue());
        Assert.assertEquals(1219 , c2.getStocks().get(StockType.D).intValue());


        Client c9 = Fixtures.parseClient(list.get(8));
        Assert.assertEquals("C9", c9.getId());
        Assert.assertEquals(-17325, c9.getBudget());
        Assert.assertEquals(2035, c9.getStocks().get(StockType.A).intValue());
        Assert.assertEquals(2177, c9.getStocks().get(StockType.B).intValue());
        Assert.assertEquals(2409 , c9.getStocks().get(StockType.C).intValue());
        Assert.assertEquals(2290 , c9.getStocks().get(StockType.D).intValue());

    }
}

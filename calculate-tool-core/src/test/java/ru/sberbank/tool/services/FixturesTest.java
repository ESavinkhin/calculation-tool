package ru.sberbank.tool.services;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.tool.model.Client;
import ru.sberbank.tool.model.Operation;
import ru.sberbank.tool.model.Order;
import ru.sberbank.tool.model.StockType;

import static org.hamcrest.core.Is.is;

public class FixturesTest {

    @Test
    public void byteToKbyte() {
        Assert.assertThat(Fixtures.byteToKbyte(1000), is(1L));
    }

    @Test
    public void parseClient() {
        Client client = Fixtures.parseClient("A\t10\t4\t1\t3\t6");
        Assert.assertThat("A", is(client.getId()));
        Assert.assertThat(10, is(client.getBudget()));
        Assert.assertEquals(4, client.getStocks().size());
        Assert.assertThat(4, is(client.getStocks().get(StockType.A)));
        Assert.assertThat(1, is(client.getStocks().get(StockType.B)));
        Assert.assertThat(3, is(client.getStocks().get(StockType.C)));
        Assert.assertThat(6, is(client.getStocks().get(StockType.D)));
    }

    @Test
    public void parseOrder() {
        Order order = Fixtures.parseOrder("A\tb\tA\t4\t2");
        Assert.assertEquals("A", order.getClientId());
        Assert.assertEquals(Operation.BUY, order.getOperation());
        Assert.assertEquals(StockType.A, order.getStockType());
        Assert.assertEquals(4, order.getCount());
        Assert.assertEquals(2, order.getPrice());
    }

    @Test
    public void clientToString() {
        Client client = new Client("client1", 10, Maps.immutableEnumMap(ImmutableMap.of(
                StockType.A, 1,
                StockType.B, 2,
                StockType.C, 3,
                StockType.D, 4
        )));
        Assert.assertEquals("client1\t10\t1\t2\t3\t4", Fixtures.clientToString(client));
    }
}
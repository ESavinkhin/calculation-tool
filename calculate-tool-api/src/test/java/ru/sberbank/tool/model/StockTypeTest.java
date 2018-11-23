package ru.sberbank.tool.model;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class StockTypeTest {

    @Test
    public void getCode() {
        Assert.assertThat(StockType.A.getCode(), is("A"));
        Assert.assertThat(StockType.B.getCode(), is("B"));
    }

    @Test
    public void getByCode() {
        Assert.assertThat(StockType.getByCode("A"), is(StockType.A));
        Assert.assertThat(StockType.getByCode("B"), is(StockType.B));
    }
}
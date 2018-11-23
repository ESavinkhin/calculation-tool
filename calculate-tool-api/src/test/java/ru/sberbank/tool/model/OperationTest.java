package ru.sberbank.tool.model;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OperationTest {

    @Test
    public void getCode() {
        Assert.assertThat(Operation.BUY.getCode(), is("b"));
        Assert.assertThat(Operation.SALE.getCode(), is("s"));
    }

    @Test
    public void getByCode() {
        Assert.assertThat(Operation.getByCode("b"), is(Operation.BUY));
        Assert.assertThat(Operation.getByCode("s"), is(Operation.SALE));
    }
}
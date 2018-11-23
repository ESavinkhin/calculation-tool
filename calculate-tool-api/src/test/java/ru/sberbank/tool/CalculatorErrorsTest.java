package ru.sberbank.tool;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class CalculatorErrorsTest {

    @Test
    public void error() {
        Assert.assertThat(CalculatorErrors.NOT_FOUND_STRATEGY.getCode(), is("JTE-01"));
    }

    @Test
    public void getMessage() {
        Assert.assertThat(CalculatorErrors.NOT_FOUND_STRATEGY.getMessage(), is("message1"));
    }

    @Test
    public void createException() {
        CalculatorException ex = CalculatorErrors.MERGE_ERROR.error();
        Assert.assertNotNull(ex);
        ex = CalculatorErrors.MERGE_ERROR.error("test");
        Assert.assertNotNull(ex);
    }
}
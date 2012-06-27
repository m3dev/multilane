package com.m3.multilane.action;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class NoInputTest {

    @Test
    public void type() throws Exception {
        assertThat(NoInput.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        NoInput target = new NoInput();
        assertThat(target, notNullValue());
    }

}

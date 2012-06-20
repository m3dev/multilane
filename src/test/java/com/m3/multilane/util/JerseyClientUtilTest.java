package com.m3.multilane.util;

import com.sun.jersey.api.client.Client;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class JerseyClientUtilTest {

    @Test
    public void type() throws Exception {
        assertThat(JerseyClientUtil.class, notNullValue());
    }

    @Test
    public void createClient_A$() throws Exception {
        Client actual = JerseyClientUtil.createClient();
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void createClient_A$Integer() throws Exception {
        Integer readTimeoutMillis = 12345;
        Client actual = JerseyClientUtil.createClient(readTimeoutMillis);
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void createClient_A$Integer$Integer() throws Exception {
        Integer connectTimeoutMillis = 123;
        Integer readTimeoutMillis = 234;
        Client actual = JerseyClientUtil.createClient(connectTimeoutMillis, readTimeoutMillis);
        assertThat(actual, is(notNullValue()));
    }

}

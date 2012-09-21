package com.m3.multilane;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DefaultRendezvousTest {

    @Test
    public void type() throws Exception {
        assertThat(DefaultRendezvous.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        DefaultRendezvous rendezvous = new DefaultRendezvous();
        assertThat(rendezvous, notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void start_A$Runnable$Integer_NullRunnable() throws Exception {
        DefaultRendezvous rendezvous = new DefaultRendezvous();
        Runnable runnable = null;
        Integer timeoutMillis = 123;
        rendezvous.start(runnable, timeoutMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void start_A$Runnable$Integer_NullMillis() throws Exception {
        DefaultRendezvous rendezvous = new DefaultRendezvous();
        Runnable runnable = new Runnable() {
            public void run() {
            }
        };
        Integer timeoutMillis = null;
        rendezvous.start(runnable, timeoutMillis);
    }

    String result;

    @Test
    public void start_A$Runnable$Integer() throws Exception {
        DefaultRendezvous rendezvous = new DefaultRendezvous();
        Runnable runnable = new Runnable() {
            public void run() {
                sleep(100L);
                result = "ok";
            }
        };
        Integer timeoutMillis = 100;
        rendezvous.start(runnable, timeoutMillis);
        assertThat(result, is(nullValue()));

        rendezvous.awaitAll();
        assertThat(result, is(equalTo("ok")));
    }

    @Test
    public void onFailure_A$Throwable() throws Exception {
        DefaultRendezvous rendezvous = new DefaultRendezvous();
        Throwable t = new NullPointerException();
        rendezvous.onFailure(t);
    }

    @Test
    public void awaitAll_A$() throws Exception {
        DefaultRendezvous rendezvous = new DefaultRendezvous();
        rendezvous.awaitAll();
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

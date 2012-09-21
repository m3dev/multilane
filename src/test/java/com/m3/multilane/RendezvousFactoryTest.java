package com.m3.multilane;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class RendezvousFactoryTest {

    @Test
    public void type() throws Exception {
        assertThat(RendezvousFactory.class, notNullValue());
    }

    String result1 = null;
    String result2 = null;

    @Test
    public void create_A$() throws Exception {
        result1 = null;
        result2 = null;
        Rendezvous rendezvous = RendezvousFactory.create();
        rendezvous.start(new Runnable() {
            public void run() {
                sleep(300L);
                result1 = "aaa";
            }
        }, 1000);
        rendezvous.start(new Runnable() {
            public void run() {
                sleep(300L);
                result2 = "bbb";
            }
        }, 1000);
        assertThat(result1, is(nullValue()));
        assertThat(result1, is(nullValue()));

        rendezvous.awaitAll();
        assertThat(result1, is(equalTo("aaa")));
        assertThat(result2, is(equalTo("bbb")));

    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

package com.m3.multilane;

import com.m3.scalaflavor4j.SInt;
import com.m3.scalaflavor4j.VoidF1;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class DefaultRendezvousTest {

    Logger log = LoggerFactory.getLogger(this.getClass());

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
        Runnable runnable1 = new Runnable() {
            public void run() {
                sleep(100L);
                result = "ok";
            }
        };
        Runnable runnable2 = new Runnable() {
            public void run() {
                sleep(10000000L);
                result = "ng";
            }
        };
        Integer timeoutMillis = 100;
        rendezvous.start(runnable1, timeoutMillis);
        rendezvous.start(runnable2, timeoutMillis);
        assertThat(result, is(nullValue()));

        rendezvous.awaitAll();
        assertThat(result, is(equalTo("ok")));
    }

    String result_start_A$Runnable$Integer_Timeout;

    @Test
    public void start_A$Runnable$Integer_AfterTimeout() throws Exception {
        DefaultRendezvous rendezvous = new DefaultRendezvous();
        Runnable runnable1 = new Runnable() {
            public void run() {
                sleep(500L);
                result_start_A$Runnable$Integer_Timeout = "after_timeout";
            }
        };
        result_start_A$Runnable$Integer_Timeout = null;
        Integer timeoutMillis = 100;
        rendezvous.start(runnable1, timeoutMillis);
        assertThat(result, is(nullValue()));

        rendezvous.awaitAll();
        assertThat(result, is(nullValue()));

        Thread.sleep(1000L);
        assertThat(result_start_A$Runnable$Integer_Timeout, is(equalTo("after_timeout")));
    }

    @Test
    public void stressTest() throws Exception {

        final Runnable runnable1 = new Runnable() {
            public void run() {
                sleep(100L);
            }
        };
        final Runnable runnable2 = new Runnable() {
            public void run() {
                sleep(10000000L);
            }
        };

        SInt._(1).to(10000).par().foreach(new VoidF1<Integer>() {
            public void _(Integer i) throws Exception {
                DefaultRendezvous rendezvous = new DefaultRendezvous();
                Integer timeoutMillis = 100;
                rendezvous.start(runnable1, timeoutMillis);
                rendezvous.start(runnable2, timeoutMillis);
                rendezvous.awaitAll();
            }
        });
        SInt._(1).to(1000).foreach(new VoidF1<Integer>() {
            public void _(Integer i) throws Exception {
                long size = Thread.getAllStackTraces().size();
                log.info("Thread.getAllStackTraces().size() -> " + size);
                if (size > 300) {
                    fail("Too many threads are created!");
                }
                Thread.sleep(10L);
            }
        });
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

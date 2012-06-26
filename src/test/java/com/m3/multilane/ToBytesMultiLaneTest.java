package com.m3.multilane;

import com.m3.multilane.action.HttpGetToBytesAction;
import com.m3.multilane.action.NoInputAction;
import com.m3.scalaflavor4j.Either;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ToBytesMultiLaneTest {

    TestServer server = new TestServer(8902);

    @Before
    public void setUp() throws Exception {
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void type() throws Exception {
        assertThat(ToBytesMultiLane.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        ToBytesMultiLane multiLane = new ToBytesMultiLane();
        assertThat(multiLane, notNullValue());
    }

    @Test
    public void collect_A$() throws Exception {

        ToBytesMultiLane multiLane = new ToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8902/?v=abc", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);
        multiLane.start("local", new NoInputAction<byte[]>(1000) {
            protected byte[] process() throws Exception {
                return "ABC".getBytes();
            }
        });

        Map<String, Either<Throwable, byte[]>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(3)));
        assertThat(results.get("req-1").right().getOrNull(), is(equalTo("abc".getBytes())));
        assertThat(results.get("req-2").right().getOrNull(), is(equalTo("abc".getBytes())));
        assertThat(results.get("local").right().getOrNull(), is(equalTo("ABC".getBytes())));
    }

    @Test
    public void collectValues_A$() throws Exception {

        ToBytesMultiLane multiLane = new ToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8902/?v=bcd", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);
        multiLane.start("local", new NoInputAction<byte[]>(1000) {
            protected byte[] process() throws Exception {
                return "ABC".getBytes();
            }
        });

        Map<String, byte[]> values = multiLane.collectValues();
        assertThat(values.size(), is(equalTo(3)));

        assertThat(values.get("req-1"), is(equalTo("bcd".getBytes())));
        assertThat(values.get("req-2"), is(equalTo("bcd".getBytes())));
        assertThat(values.get("local"), is(equalTo("ABC".getBytes())));
    }

    @Test
    public void collectValues_A$_TimeoutWithoutDefaultValues() throws Exception {

        ToBytesMultiLane multiLane = new ToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8902/", 1);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);
        multiLane.start("local", new NoInputAction<byte[]>(1) {
            protected byte[] process() throws Exception {
                Thread.sleep(1000L);
                return "ABC".getBytes();
            }
        });

        Map<String, Either<Throwable, byte[]>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(3)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));
        assertThat(results.get("local").isLeft(), is(true));

        Map<String, byte[]> values = multiLane.collectValues();
        assertThat(values.get("req-1"), is(nullValue()));
        assertThat(values.get("req-2"), is(nullValue()));
        assertThat(values.get("local"), is(nullValue()));
    }

    @Test
    public void collectValues_A$_TimeoutWithDefaultValues() throws Exception {

        ToBytesMultiLane multiLane = new ToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8902/", 1);
        multiLane.start("req-1", httpGet, "Unavailable".getBytes());
        multiLane.start("req-2", httpGet, "Unavailable".getBytes());
        multiLane.start("local", new NoInputAction<byte[]>(1) {
            protected byte[] process() throws Exception {
                Thread.sleep(1000L);
                return "ABC".getBytes();
            }
        }, "Unavailable".getBytes());

        Map<String, Either<Throwable, byte[]>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(3)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));
        assertThat(results.get("local").isLeft(), is(true));

        Map<String, byte[]> values = multiLane.collectValues();
        assertThat(values.get("req-1"), is(equalTo("Unavailable".getBytes())));
        assertThat(values.get("req-2"), is(equalTo("Unavailable".getBytes())));
        assertThat(values.get("local"), is(equalTo("Unavailable".getBytes())));

    }

}

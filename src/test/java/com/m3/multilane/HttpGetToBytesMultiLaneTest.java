package com.m3.multilane;

import com.m3.multilane.action.HttpGetToBytesAction;
import com.m3.scalaflavor4j.Either;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class HttpGetToBytesMultiLaneTest {

    TestServer server = new TestServer(8884);

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
        assertThat(HttpGetToBytesMultiLane.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        HttpGetToBytesMultiLane multiLane = new HttpGetToBytesMultiLane();
        assertThat(multiLane, notNullValue());
    }

    @Test
    public void collect_A$() throws Exception {
        HttpGetToBytesMultiLane multiLane = new HttpGetToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8884/?v=abc", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, Either<Throwable, byte[]>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(2)));
        assertThat(results.get("req-1").right().getOrNull(), is(equalTo("abc".getBytes())));
        assertThat(results.get("req-2").right().getOrNull(), is(equalTo("abc".getBytes())));
    }

    @Test
    public void collectValues_A$() throws Exception {
        HttpGetToBytesMultiLane multiLane = new HttpGetToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8884/?v=bcd", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, byte[]> values = multiLane.collectValues();
        assertThat(values.size(), is(equalTo(2)));

        assertThat(values.get("req-1"), is(equalTo("bcd".getBytes())));
        assertThat(values.get("req-2"), is(equalTo("bcd".getBytes())));
    }

    @Test
    public void collect_A$_TimeoutWithoutDefaultValues() throws Exception {
        HttpGetToBytesMultiLane multiLane = new HttpGetToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8884/", 1);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, Either<Throwable, byte[]>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(2)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));
    }

    @Test
    public void collectValues_A$_TimeoutWithoutDefaultValues() throws Exception {
        HttpGetToBytesMultiLane multiLane = new HttpGetToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8884/", 1);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, byte[]> values = multiLane.collectValues();
        assertThat(values.get("req-1"), is(nullValue()));
        assertThat(values.get("req-2"), is(nullValue()));
    }


    @Test
    public void collect_A$_TimeoutWithDefaultValues() throws Exception {
        HttpGetToBytesMultiLane multiLane = new HttpGetToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8884/", 1);
        multiLane.start("req-1", httpGet, "Unavailable".getBytes());
        multiLane.start("req-2", httpGet, "Unavailable".getBytes());

        Map<String, Either<Throwable, byte[]>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(2)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));
    }

    @Test
    public void collectValues_A$_TimeoutWithDefaultValues() throws Exception {
        HttpGetToBytesMultiLane multiLane = new HttpGetToBytesMultiLane();
        HttpGetToBytesAction httpGet = new HttpGetToBytesAction("http://localhost:8884/", 1);
        multiLane.start("req-1", httpGet, "Unavailable".getBytes());
        multiLane.start("req-2", httpGet, "Unavailable".getBytes());

        Map<String, byte[]> values = multiLane.collectValues();
        assertThat(values.get("req-1"), is(equalTo("Unavailable".getBytes())));
        assertThat(values.get("req-2"), is(equalTo("Unavailable".getBytes())));
    }

}

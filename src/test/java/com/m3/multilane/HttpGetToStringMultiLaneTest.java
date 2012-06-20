package com.m3.multilane;

import com.m3.multilane.action.HttpGetToStringAction;
import com.m3.scalaflavor4j.Either;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class HttpGetToStringMultiLaneTest {

    TestServer server = new TestServer(8881);

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
        assertThat(HttpGetToStringMultiLane.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        assertThat(multiLane, notNullValue());
    }

    @Test
    public void collect_A$() throws Exception {

        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8881/?v=abc", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, Either<Throwable, String>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(2)));
        assertThat(results.get("req-1").right().getOrElse(""), is(equalTo("abc")));
        assertThat(results.get("req-2").right().getOrElse(""), is(equalTo("abc")));
    }

    @Test
    public void collectValues_A$() throws Exception {

        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8881/?v=bcd", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, String> values = multiLane.collectValues();
        assertThat(values.size(), is(equalTo(2)));

        assertThat(values.get("req-1"), is(equalTo("bcd")));
        assertThat(values.get("req-2"), is(equalTo("bcd")));
    }

    @Test
    public void collectValues_A$_TimeoutWithoutDefaultValues() throws Exception {

        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8881/", 1);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, Either<Throwable, String>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(2)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));

        Map<String, String> values = multiLane.collectValues();
        assertThat(values.get("req-1"), is(nullValue()));
        assertThat(values.get("req-2"), is(nullValue()));

    }

    @Test
    public void collectValues_A$_TimeoutWithDefaultValues() throws Exception {

        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8881/", 1);
        multiLane.start("req-1", httpGet, "Unavailable");
        multiLane.start("req-2", httpGet, "Unavailable");

        Map<String, Either<Throwable, String>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(2)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));

        Map<String, String> values = multiLane.collectValues();
        assertThat(values.get("req-1"), is(equalTo("Unavailable")));
        assertThat(values.get("req-2"), is(equalTo("Unavailable")));

    }

}

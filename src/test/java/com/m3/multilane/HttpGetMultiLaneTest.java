package com.m3.multilane;

import com.m3.multilane.action.HttpGetAction;
import com.m3.scalaflavor4j.Either;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class HttpGetMultiLaneTest {

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
        assertThat(HttpGetMultiLane.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        HttpGetMultiLane multiLane = new HttpGetMultiLane();
        assertThat(multiLane, notNullValue());
    }

    @Test
    public void collect_A$() throws Exception {

        HttpGetMultiLane multiLane = new HttpGetMultiLane();
        HttpGetAction httpGet = new HttpGetAction("http://localhost:8881/", "UTF-8", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, Either<Throwable, String>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(2)));

    }

    @Test
    public void collectValues_A$() throws Exception {

        HttpGetMultiLane multiLane = new HttpGetMultiLane();
        HttpGetAction httpGet = new HttpGetAction("http://localhost:8881/", "UTF-8", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, String> values = multiLane.collectValues();
        assertThat(values.size(), is(equalTo(2)));

    }

    @Test
    public void collectValues_A$_defaultValues() throws Exception {

        HttpGetMultiLane multiLane = new HttpGetMultiLane();
        HttpGetAction httpGet = new HttpGetAction("http://localhost:8881/", "UTF-8", 1);
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

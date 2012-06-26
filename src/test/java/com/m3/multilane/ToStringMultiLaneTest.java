package com.m3.multilane;

import com.m3.multilane.action.HttpGetToStringAction;
import com.m3.multilane.action.NoInputAction;
import com.m3.scalaflavor4j.Either;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ToStringMultiLaneTest {

    TestServer server = new TestServer(8901);

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
        assertThat(ToStringMultiLane.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        ToStringMultiLane multiLane = new ToStringMultiLane();
        assertThat(multiLane, notNullValue());
    }

    @Test
    public void collect_A$() throws Exception {

        ToStringMultiLane multiLane = new ToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8901/?v=abc", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);
        multiLane.start("local", new NoInputAction<String>(1000) {
            protected String process() throws Exception {
                return "ABC";
            }
        });

        Map<String, Either<Throwable, String>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(3)));
        assertThat(results.get("req-1").right().getOrElse(""), is(equalTo("abc")));
        assertThat(results.get("req-2").right().getOrElse(""), is(equalTo("abc")));
        assertThat(results.get("local").right().getOrElse(""), is(equalTo("ABC")));
    }

    @Test
    public void collectValues_A$() throws Exception {

        ToStringMultiLane multiLane = new ToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8901/?v=bcd", 1000);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);
        multiLane.start("local", new NoInputAction<String>(1000) {
            protected String process() throws Exception {
                return "ABC";
            }
        });

        Map<String, String> values = multiLane.collectValues();
        assertThat(values.size(), is(equalTo(3)));

        assertThat(values.get("req-1"), is(equalTo("bcd")));
        assertThat(values.get("req-2"), is(equalTo("bcd")));
        assertThat(values.get("local"), is(equalTo("ABC")));
    }

    @Test
    public void collectValues_A$_TimeoutWithoutDefaultValues() throws Exception {

        ToStringMultiLane multiLane = new ToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8901/", 1);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);
        multiLane.start("local", new NoInputAction<String>(1) {
            protected String process() throws Exception {
                Thread.sleep(1000L);
                return "ABC";
            }
        });

        Map<String, Either<Throwable, String>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(3)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));
        assertThat(results.get("local").isLeft(), is(true));

        Map<String, String> values = multiLane.collectValues();
        assertThat(values.get("req-1"), is(nullValue()));
        assertThat(values.get("req-2"), is(nullValue()));
        assertThat(values.get("local"), is(nullValue()));
    }

    @Test
    public void collectValues_A$_TimeoutWithDefaultValues() throws Exception {

        ToStringMultiLane multiLane = new ToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8901/", 1);
        multiLane.start("req-1", httpGet, "Unavailable");
        multiLane.start("req-2", httpGet, "Unavailable");
        multiLane.start("local", new NoInputAction<String>(1) {
            protected String process() throws Exception {
                Thread.sleep(1000L);
                return "ABC";
            }
        }, "Unavailable");

        Map<String, Either<Throwable, String>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(3)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));
        assertThat(results.get("local").isLeft(), is(true));

        Map<String, String> values = multiLane.collectValues();
        assertThat(values.get("req-1"), is(equalTo("Unavailable")));
        assertThat(values.get("req-2"), is(equalTo("Unavailable")));
        assertThat(values.get("local"), is(equalTo("Unavailable")));
    }
}

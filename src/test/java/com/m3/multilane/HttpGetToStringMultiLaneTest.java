package com.m3.multilane;

import com.m3.multilane.action.HttpGetToStringAction;
import com.m3.scalaflavor4j.Either;
import com.m3.scalaflavor4j.SInt;
import com.m3.scalaflavor4j.VoidF1;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class HttpGetToStringMultiLaneTest {

    Logger log = LoggerFactory.getLogger(this.getClass());

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
    public void collect_A$_TimeoutWithoutDefaultValues() throws Exception {
        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8881/?v=abc", 1);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

        Map<String, Either<Throwable, String>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(2)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));
    }

    @Test
    public void collect_A$_TimeoutWithDefaultValues() throws Exception {
        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8881/", 1);
        multiLane.start("req-1", httpGet, "Unavailable");
        multiLane.start("req-2", httpGet, "Unavailable");

        Map<String, Either<Throwable, String>> results = multiLane.collect();
        assertThat(results.size(), is(equalTo(2)));
        assertThat(results.get("req-1").isLeft(), is(true));
        assertThat(results.get("req-2").isLeft(), is(true));
    }


    @Test
    public void collectValues_A$_SameAction() throws Exception {
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
    public void collectValues_A$() throws Exception {
        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        HttpGetToStringAction httpGet1 = new HttpGetToStringAction("http://localhost:8881/?v=bcd", 3000);
        HttpGetToStringAction httpGet2 = new HttpGetToStringAction("http://localhost:8881/?v=cde", 3000);
        multiLane.start("req-1", httpGet1);
        multiLane.start("req-2", httpGet2);

        Map<String, String> result = multiLane.collectValues();

        assertThat(result.get("req-1"), is(equalTo("bcd")));
        assertThat(result.get("req-2"), is(equalTo("cde")));
    }

    /**
     * https://github.com/m3dev/multilane/issues/1
     */
    @Test
    public void collectValues_A$_Issue1() throws Exception {
        final HttpGetToStringAction httpGet1 = new HttpGetToStringAction("http://localhost:8881/?v=bcd", 3000);
        final HttpGetToStringAction httpGet2 = new HttpGetToStringAction("http://localhost:8881/?v=cde", 3000);
        SInt._(1).to(10000).par().foreach(new VoidF1<Integer>() {
            public void _(Integer i) throws Exception {
                HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
                multiLane.start("req-1", httpGet1);
                multiLane.start("req-2", httpGet2);
                Map<String, String> result = multiLane.collectValues();
                assertThat(result.get("req-1"), is(equalTo("bcd")));
                assertThat(result.get("req-2"), is(equalTo("cde")));
            }
        });
        SInt._(1).to(1000).foreach(new VoidF1<Integer>() {
            public void _(Integer v1) throws Exception {
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
    public void collectValues_A$_TimeoutWithoutDefaultValues() throws Exception {
        HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();
        HttpGetToStringAction httpGet = new HttpGetToStringAction("http://localhost:8881/", 1);
        multiLane.start("req-1", httpGet);
        multiLane.start("req-2", httpGet);

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

        Map<String, String> values = multiLane.collectValues();
        assertThat(values.get("req-1"), is(equalTo("Unavailable")));
        assertThat(values.get("req-2"), is(equalTo("Unavailable")));
    }

}

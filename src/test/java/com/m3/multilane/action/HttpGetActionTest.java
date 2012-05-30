package com.m3.multilane.action;

import com.m3.multilane.TestServer;
import com.m3.scalaflavor4j.Either;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HttpGetActionTest {

    TestServer server = new TestServer();

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
        assertThat(HttpGetAction.class, notNullValue());
    }

    @Test
    public void getInstance_A$() throws Exception {
        HttpGetAction actual = new HttpGetAction(null, null, 1000);
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void apply_A$String() throws Exception {
        HttpGetAction get = new HttpGetAction("http://localhost:8888/", "UTF-8", 3000);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isRight(), is(true));
        assertThat(actual.right().getOrElse("").length(), is(greaterThan(0)));
    }

    @Test
    public void apply_A$String_NotFound() throws Exception {
        HttpGetAction get = new HttpGetAction("http://localhost:8888/404", "UTF-8", 3000);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isLeft(), is(true));
    }

    @Test
    public void apply_A$String_InternalServerError() throws Exception {
        HttpGetAction get = new HttpGetAction("http://localhost:8888/500", "UTF-8", 3000);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isLeft(), is(true));
    }

    @Test
    public void apply_A$String_ReadTimeout() throws Exception {
        HttpGetAction get = new HttpGetAction("http://localhost:8888/", "UTF-8", 1);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isLeft(), is(true));
    }

}

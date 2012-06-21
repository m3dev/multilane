package com.m3.multilane.action;

import com.m3.multilane.TestServer;
import com.m3.scalaflavor4j.Either;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URLEncoder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HttpGetToStringActionTest {

    public String urlEncode(String param) throws Exception {
        return URLEncoder.encode(param, "UTF-8");
    }

    TestServer server = new TestServer(8882);

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
        assertThat(HttpGetToStringAction.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        HttpGetToStringAction actual = new HttpGetToStringAction(null, 1000);
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void apply_A$() throws Exception {
        HttpGetToStringAction get = new HttpGetToStringAction("http://localhost:8882/?v=" + urlEncode("日本語") + "&charset=UTF-8", 3000);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isRight(), is(true));
        assertThat(actual.right().getOrElse(""), is(equalTo("日本語")));
    }

    @Test
    public void apply_A$_NoContent() throws Exception {
        HttpGetToStringAction get = new HttpGetToStringAction("http://localhost:8882/204", 3000);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isLeft(), is(true));
        assertThat(actual.left().getOrNull().getClass().getName(), is(equalTo("com.sun.jersey.api.client.UniformInterfaceException")));
    }

    @Test
    public void apply_A$_Found_follow() throws Exception {
        HttpGetToStringAction get = new HttpGetToStringAction("http://localhost:8882/302", 3000);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isRight(), is(true));
        assertThat(actual.right().getOrNull(), is(equalTo("redirected")));
    }

    @Test
    public void apply_A$_Found_neverFollow() throws Exception {
        HttpGetToStringAction get = new HttpGetToStringAction("http://localhost:8882/302", 3000);
        get.setFollowRedirect(false);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isLeft(), is(true));
        assertThat(actual.left().getOrNull().getClass().getName(), is(equalTo("com.sun.jersey.api.client.UniformInterfaceException")));
    }

    @Test
    public void apply_A$_NotFound() throws Exception {
        HttpGetToStringAction get = new HttpGetToStringAction("http://localhost:8882/404", 3000);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isLeft(), is(true));
    }

    @Test
    public void apply_A$_InternalServerError() throws Exception {
        HttpGetToStringAction get = new HttpGetToStringAction("http://localhost:8882/500", 3000);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isLeft(), is(true));
    }

    @Test
    public void apply_A$_ReadTimeout() throws Exception {
        HttpGetToStringAction get = new HttpGetToStringAction("http://localhost:8882/", 1);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isLeft(), is(true));
    }

    @Test
    public void apply_A$_InvalidEncoding() throws Exception {
        HttpGetToStringAction get = new HttpGetToStringAction("http://localhost:8882/?v=" + urlEncode("日本語") + "&charset=Shift_JIS", 3000);
        Either<Throwable, String> actual = get.apply();
        assertThat(actual.isRight(), is(true));
        assertThat(actual.right().getOrElse(""), is(equalTo("日本語")));
    }

    @Test
    public void getInput_A$() throws Exception {
        HttpGetToStringAction action = new HttpGetToStringAction("http://localhost:8882/", 3000);
        String actual = action.getInput();
        String expected = "http://localhost:8882/";
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setInput_A$String() throws Exception {
        HttpGetToStringAction action = new HttpGetToStringAction("http://localhost:8882/", 3000);
        String input = "xxx";
        action.setInput(input);
        String actual = action.getInput();
        String expected = "xxx";
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void getTimeoutMillis_A$() throws Exception {
        HttpGetToStringAction action = new HttpGetToStringAction("http://localhost:8882/", 3000);
        Integer actual = action.getTimeoutMillis();
        Integer expected = 3000;
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setTimeoutMillis_A$Integer() throws Exception {
        HttpGetToStringAction action = new HttpGetToStringAction("http://localhost:8882/", 3000);
        Integer millis = 123;
        action.setTimeoutMillis(millis);
        Integer actual = action.getTimeoutMillis();
        Integer expected = 123;
        assertThat(actual, is(equalTo(expected)));
    }

}

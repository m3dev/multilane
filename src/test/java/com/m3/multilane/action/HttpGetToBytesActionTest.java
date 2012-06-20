package com.m3.multilane.action;

import com.m3.multilane.TestServer;
import com.m3.scalaflavor4j.Either;
import com.m3.scalaflavor4j.Source;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HttpGetToBytesActionTest {

    public String urlEncode(String param) throws Exception {
        return URLEncoder.encode(param, "UTF-8");
    }

    TestServer server = new TestServer(8883);

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
        assertThat(HttpGetToBytesAction.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        String url = null;
        Integer timeoutMillis = null;
        HttpGetToBytesAction target = new HttpGetToBytesAction(url, timeoutMillis);
        assertThat(target, notNullValue());
    }

    @Test
    public void apply_A$_Text() throws Exception {
        HttpGetToBytesAction action = new HttpGetToBytesAction("http://localhost:8883/?v=" + urlEncode("日本語") + "&charset=UTF-8", 3000);
        Either<Throwable, byte[]> actual = action.apply();
        assertThat(new String(actual.right().getOrNull(), "UTF-8"), is(equalTo("日本語")));
    }

    @Test
    public void apply_A$_Binary() throws Exception {
        HttpGetToBytesAction action = new HttpGetToBytesAction("http://www.m3.com/images/shared/ci_02.gif", 3000);
        Either<Throwable, byte[]> actual = action.apply();
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("./m3com.gif"));
            os.write(actual.right().getOrNull());
            assertThat(new File("./m3com.gif").exists(), is(true));
            assertThat(Source.fromFile(new File("./m3com.gif"), null).toByteSeq().size(), is(greaterThan(0)));
        } finally {
            if (os != null) {
                os.close();
            }
            if (new File("./m3com.gif").exists()) {
                new File("./m3com.gif").deleteOnExit();
            }
        }
    }

    @Test
    public void getInput_A$() throws Exception {
        HttpGetToBytesAction action = new HttpGetToBytesAction("http://localhost:8883/", 3000);
        String actual = action.getInput();
        String expected = "http://localhost:8883/";
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setInput_A$String() throws Exception {
        HttpGetToBytesAction action = new HttpGetToBytesAction("http://localhost:8883/", 3000);
        String input = "xxx";
        action.setInput(input);
        assertThat(action.getInput(), is(equalTo("xxx")));
    }

    @Test
    public void getTimeoutMillis_A$() throws Exception {
        HttpGetToBytesAction action = new HttpGetToBytesAction("http://localhost:8883/", 12345);
        Integer actual = action.getTimeoutMillis();
        Integer expected = 12345;
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void setTimeoutMillis_A$Integer() throws Exception {
        HttpGetToBytesAction action = new HttpGetToBytesAction("http://localhost:8883/", 12345);
        action.setTimeoutMillis(300);
        Integer actual = action.getTimeoutMillis();
        assertThat(actual, is(equalTo(300)));
    }

}

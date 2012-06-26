package com.m3.multilane.action;

import com.m3.scalaflavor4j.Either;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


public class NoInputActionTest {

    static class HelloWorldAction extends NoInputAction<String> {

        public HelloWorldAction(Integer timeoutMillis) {
            super(timeoutMillis);
        }

        protected String process() throws Exception {
            Thread.sleep(200L);
            return "Hello, World!";
        }

    }

    @Test
    public void normal() throws Exception {
        HelloWorldAction action = new HelloWorldAction(500);
        Either<Throwable, String> result = action.apply();
        assertThat(result.isRight(), is(true));
        assertThat(result.right().getOrNull(), is(equalTo("Hello, World!")));
    }

    @Test
    public void timeout() throws Exception {
        HelloWorldAction action = new HelloWorldAction(100);
        Either<Throwable, String> result = action.apply();
        assertThat(result.isLeft(), is(true));
        assertThat(result.left().getOrNull().getClass().getName(), is(equalTo("java.util.concurrent.TimeoutException")));
    }

}

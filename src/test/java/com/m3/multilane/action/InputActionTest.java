package com.m3.multilane.action;

import com.m3.scalaflavor4j.Either;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class InputActionTest {

    static class HelloWorldAction extends InputAction<Integer, String> {

        public HelloWorldAction(Integer times, Integer timeoutMillis) {
            super(times, timeoutMillis);
        }

        protected String process(Integer times) throws Exception {
            Thread.sleep(200L);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < times; i++) {
                sb.append("Hello, World!");
            }
            return sb.toString();
        }

    }

    @Test
    public void normal() throws Exception {
        HelloWorldAction action = new HelloWorldAction(2, 500);
        Either<Throwable, String> result = action.apply();
        assertThat(result.isRight(), is(true));
        assertThat(result.right().getOrNull(), is(equalTo("Hello, World!Hello, World!")));
    }

    @Test
    public void timeout() throws Exception {
        HelloWorldAction action = new HelloWorldAction(3, 100);
        Either<Throwable, String> result = action.apply();
        assertThat(result.isLeft(), is(true));
        assertThat(result.left().getOrNull().getClass().getName(), is(equalTo("java.util.concurrent.TimeoutException")));
    }

}

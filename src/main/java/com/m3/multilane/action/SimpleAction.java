package com.m3.multilane.action;

/**
 * Simple Action template
 *
 * @param <INPUT>  input type
 * @param <OUTPUT> output type
 */
public abstract class SimpleAction<INPUT, OUTPUT> implements Action<INPUT, OUTPUT> {

    /**
     * Input value
     */
    private INPUT input;

    /**
     * Timeout milliseconds
     */
    private Integer timeoutMillis;

    public SimpleAction(INPUT input, Integer timeoutMillis) {
        setInput(input);
        setTimeoutMillis(timeoutMillis);
    }

    @Override
    public INPUT getInput() {
        return this.input;
    }

    @Override
    public void setInput(INPUT input) {
        this.input = input;
    }

    @Override
    public Integer getTimeoutMillis() {
        return this.timeoutMillis;
    }

    @Override
    public void setTimeoutMillis(Integer millis) {
        this.timeoutMillis = millis;
    }

}

package com.m3.multilane;

public interface Rendezvous {

    /**
     * Starts the specified action asynchronously
     *
     * @param runnable      runnable
     * @param timeoutMillis milliseconds to time out
     * @return self
     */
    Rendezvous start(Runnable runnable, Integer timeoutMillis);

    /**
     * Waits getting values or timing out for all the values
     */
    void awaitAll();


}

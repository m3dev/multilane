package com.m3.multilane;

import jsr166y.ForkJoinPool;

/**
 * ForkJoinPool provider
 */
public final class ForkJoinPoolProvider {

    private ForkJoinPoolProvider() {
    }

    /**
     * Fork/Join Pool
     */
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    /**
     * Returns ForkJoinPool
     *
     * @return ForkJoinPool
     */
    public static ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }


}

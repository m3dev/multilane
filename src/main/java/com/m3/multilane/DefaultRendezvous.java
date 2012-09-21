package com.m3.multilane;

import com.m3.scalaflavor4j.Seq;
import com.m3.scalaflavor4j.VoidF0;
import com.m3.scalaflavor4j.VoidF1;
import jsr166y.ForkJoinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class DefaultRendezvous implements Rendezvous {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Futures to collect later
     */
    protected List<VoidF0> futures = new ArrayList<VoidF0>();

    /**
     * Fork/Join Pool
     */
    protected ForkJoinPool forkJoinPool = new ForkJoinPool();

    @Override
    public Rendezvous start(Runnable runnable, final Integer timeoutMillis) {
        if (runnable == null) {
            throw new IllegalArgumentException("The Runnable value should not be null.");
        }
        if (timeoutMillis == null) {
            throw new IllegalArgumentException("The timeout millis should not be null.");
        }
        final FutureTask<Void> task = new FutureTask<Void>(runnable, null);
        forkJoinPool.execute(task);
        futures.add(new VoidF0() {
            public void _() {
                try {
                    task.get(timeoutMillis, TimeUnit.MILLISECONDS);
                } catch (Throwable t) {
                    onFailure(t);
                }
            }
        });
        return this;
    }

    /**
     * Callback when some Exception is thrown
     *
     * @param t exception
     */
    protected void onFailure(Throwable t) {
        if (log.isDebugEnabled()) {
            log.debug("Failed to execute task because " + t.getMessage(), t);
        }
    }

    @Override
    public void awaitAll() {
        Seq._(futures).foreach(new VoidF1<VoidF0>() {
            public void _(VoidF0 f) throws Exception {
                f.apply();
            }
        });
    }

}

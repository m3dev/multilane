/*
 * Copyright 2012 M3, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.m3.multilane;

import com.m3.multilane.action.Action;
import com.m3.scalaflavor4j.*;
import jsr166y.ForkJoinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Basic MultiLane implementation
 *
 * @param <A> action type
 * @param <R> result type
 */
public abstract class MultiLaneTemplate<A extends Action, R> implements MultiLane<A, R> {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Futures to collect later
     */
    protected Map<String, F0<Either<Throwable, R>>> futures = new ConcurrentHashMap<String, F0<Either<Throwable, R>>>();

    /**
     * Default values that will be used if results are Left.
     */
    protected Map<String, R> defaultValues = new ConcurrentHashMap<String, R>();

    /**
     * Timing results
     */
    protected Map<String, Long> spentTime = new ConcurrentHashMap<String, Long>();

    @Override
    public MultiLane<A, R> start(final String name, final A action) {
        return start(name, action, null);
    }

    @Override
    public MultiLane<A, R> start(final String name, final A action, final R defaultValue) {
        if (defaultValue != null) {
            defaultValues.put(name, defaultValue);
        }
        final FutureTask<Either<Throwable, R>> futureTask = new FutureTask<Either<Throwable, R>>(new Callable<Either<Throwable, R>>() {
            public Either<Throwable, R> call() throws Exception {
                long before = currentMillis();
                Either<Throwable, R> result = action.apply();
                spentTime.put(name, currentMillis() - before);
                return result;
            }
        });
        ForkJoinPoolProvider.getForkJoinPool().execute(futureTask);
        futures.put(name, new F0<Either<Throwable, R>>() {
            public Either<Throwable, R> _() {
                try {
                    return futureTask.get(action.getTimeoutMillis(), TimeUnit.MILLISECONDS);
                } catch (Throwable t) {
                    return Left._(t);
                }
            }
        });
        return this;
    }

    @Override
    public Map<String, Either<Throwable, R>> collect() {
        return SMap._(futures).map(new F1<Tuple2<String, F0<Either<Throwable, R>>>, Tuple2<String, Either<Throwable, R>>>() {
            public Tuple2<String, Either<Throwable, R>> _(Tuple2<String, F0<Either<Throwable, R>>> each) throws Exception {
                String name = each._1();
                Either<Throwable, R> result = each.getSecond().apply();
                return Tuple2._(name, result);
            }
        }).toMap();
    }

    @Override
    public Map<String, R> collectValues() {
        return SMap._(SMap._(collect()).toSeq().map(new F1<Tuple2<String, Either<Throwable, R>>, Tuple2<String, R>>() {
            public Tuple2<String, R> _(Tuple2<String, Either<Throwable, R>> each) throws Exception {
                final String name = each.getFirst();
                final Either<Throwable, R> result = each.getSecond();
                if (result.isLeft()) {
                    if (log.isDebugEnabled()) {
                        Throwable t = result.left().getOrNull();
                        log.debug("Failed to get '" + name + "' value because of " + t.getClass().getName(), t);
                    }
                }
                return Tuple._(name, result.right().getOrElse(new F0<R>() {
                    public R _() {
                        return defaultValues.get(name);
                    }
                }));
            }
        }).filter(new F1<Tuple2<String, R>, Boolean>() {
            public Boolean _(Tuple2<String, R> each) {
                return each._1() != null && each._2() != null;
            }
        })).toMap();
    }

    @Override
    public Map<String, Long> spentTime() {
        return spentTime;
    }

    @Override
    public Map<String, R> defaultValues() {
        return defaultValues;
    }

    private static long currentMillis() {
        return System.currentTimeMillis();
    }

}
